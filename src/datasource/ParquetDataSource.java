package datasource;

import datatypes.*;
import datatypes.column.AnyValueColumn;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.Int96Value;
import org.apache.parquet.example.data.simple.NanoTime;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.MessageColumnIO;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ParquetDataSource implements DataSource {
    private final ParquetFileReader reader;
    private final Schema schema;
    private final int batchSize;

    public ParquetDataSource(final Path source, final int batchSize) {
        try {
            this.reader = ParquetFileReader.open(HadoopInputFile.fromPath(new org.apache.hadoop.fs.Path(source.toString()), new Configuration()));
        } catch(IOException ex) {
            throw new RuntimeException("Error reading parquet file", ex);
        }

        this.schema = fromParquet();
        this.batchSize = batchSize;
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public Iterator<RecordBatch> scan(List<String> projection) {
        return new ParquetDataSourceIterator(this.reader, projection, this.batchSize);
    }

    private Schema fromParquet() {
        final List<Field> fields = new ArrayList<>();
        final MessageType parquetSchema = this.reader.getFileMetaData().getSchema();
        for (final Type type : parquetSchema.getFields()) {
            final Field field = new Field(convertType(type.asPrimitiveType()), type.getName());
            fields.add(field);
        }
        return new Schema(fields);
    }

    private FieldType convertType(final PrimitiveType type) {
        switch (type.getPrimitiveTypeName()) {
            case INT32:
                return FieldType.Int32Type;
            case INT64:
                return FieldType.Int64Type;
            case FLOAT:
                return FieldType.FloatType;
            case DOUBLE:
                return FieldType.DoubleType;
            case BOOLEAN:
                return FieldType.BooleanType;
            case BINARY:
                return FieldType.StringType;
            case INT96:
                return FieldType.DateTime;
            default:
                throw new IllegalArgumentException("Unexpected parquet type: " + type.getPrimitiveTypeName());
        }
    }

    public class ParquetDataSourceIterator implements Iterator<RecordBatch> {
        private final ParquetFileReader reader;
        private RecordReader currentRecordReader;
        private Group currentGroup;
        private final MessageType schema;
        private long position;
        private final int batchSize;
        private final long rowCount;

        public ParquetDataSourceIterator(final ParquetFileReader reader, final List<String> projection, final int batchSize) {
            this.reader = reader;
            final MessageType schema = reader.getFileMetaData().getSchema();
            this.schema = new MessageType(schema.getName(),
                    projection.stream().map(fieldName -> findFieldByName(schema.getFields(), fieldName))
                    .collect(Collectors.toList()));

            this.rowCount = reader.getRecordCount();
            this.position = 0;
            this.batchSize = batchSize;
        }

        private Type findFieldByName(final List<Type> types, final String name) {
            Optional<Type> result = types.stream().filter(type -> name.equals(type.getName())).findFirst();

            if (!result.isPresent()) {
                throw new IllegalArgumentException("Can't find field by name: " + name);
            }

            return result.get();
        }

        @Override
        public boolean hasNext() {
            return this.position < this.rowCount;
        }

        @Override
        public RecordBatch next() {
            try {
                int count = 0;
                List<AnyValueColumn> tmpColumns =
                        this.schema.getFields().stream()
                                .map(field -> new AnyValueColumn(convertType(field.asPrimitiveType()), this.batchSize))
                                .collect(Collectors.toList());

                while (count < this.batchSize && this.position < this.rowCount) {
                    if (this.currentRecordReader == null) {
                        final PageReadStore pages = reader.readNextRowGroup();
                        if (pages == null) break;
                        final MessageColumnIO massageColumnIO = new ColumnIOFactory().getColumnIO(this.schema);
                        this.currentRecordReader = massageColumnIO.getRecordReader(pages, new GroupRecordConverter(this.schema));
                    }

                    this.currentGroup = (Group) this.currentRecordReader.read();

                    if (this.currentGroup != null) {
                        int fieldCount = this.currentGroup.getType().getFieldCount();
                        for (int field = 0; field < fieldCount; field++) {
                            final AnyValueColumn column = tmpColumns.get(field);
                            column.add(getParquetValue(this.currentGroup, field, column.getType()));
                        }

                        this.position += 1;
                    } else {
                        this.currentRecordReader = null;
                        this.currentGroup = null;
                    }
                }

                final List<ColumnVector> columns = new ArrayList<>(tmpColumns.size());
                for (int idx = 0; idx < tmpColumns.size(); idx++) {
                    columns.add(tmpColumns.get(idx).createColumn());
                }

                final Schema projectedSchema = new Schema(this.schema.getFields().stream()
                        .map(field -> new Field(convertType(field.asPrimitiveType()), field.getName()))
                        .collect(Collectors.toList()));

                return new RecordBatch(projectedSchema, columns);
            } catch (IOException ex) {
                throw new RuntimeException("Error reading parquet file", ex);
            }
        }

        private Object getParquetValue(final Group group, final int index, final FieldType type) {
            switch (type) {
                case StringType:
                    return group.getValueToString(index, 0);
                case Int32Type:
                    return group.getInteger(index, 0);
                case Int64Type:
                    return group.getLong(index, 0);
                case BooleanType:
                    return group.getBoolean(index, 0);
                case FloatType:
                    return group.getFloat(index, 0);
                case DoubleType:
                    return group.getDouble(index, 0);
                case DateTime:
                    // source: https://stackoverflow.com/questions/53690299/int96value-to-date-string
                    final Int96Value int96Value = new Int96Value(group.getInt96(index, 0));
                    NanoTime nanoTime = NanoTime.fromInt96(int96Value);
                    long nanosecondsSinceUnixEpoch = (nanoTime.getJulianDay() - 2440588) * (86400 * 1000 * 1000 * 1000) + nanoTime.getTimeOfDayNanos();
                    return new Date(nanosecondsSinceUnixEpoch / (1000 * 1000));
                default:
                    throw new IllegalArgumentException("unsupported field type");
            }
        }
    }
}
