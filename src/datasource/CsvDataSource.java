package datasource;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import datatypes.*;
import datatypes.column.AnyValueColumn;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static datatypes.FieldType.StringType;

public class CsvDataSource implements DataSource {
    private final Path filename;
    private final Schema schema;
    private final boolean hasHeaders;
    private final int batchSize;
    private final CSVReader reader;

    public CsvDataSource(final Path filename, final Optional<Schema> schema, boolean hasHeaders, int batchSize)
            throws IOException, CsvValidationException {
        this.filename = filename;
        this.hasHeaders = hasHeaders;

        if (batchSize <= 0) {
            throw new IllegalArgumentException("batchSize shoudl be > 0");
        }
        this.batchSize = batchSize;

        this.reader = new CSVReaderBuilder(new FileReader(this.filename.toFile())).build();

        if (schema.isPresent()) {
            this.schema = schema.get();
            if (hasHeaders) {
                // skip header
                this.reader.readNext();
            }
        } else {
            this.schema = inferSchema();
        }
    }

    private Schema inferSchema() throws IOException, CsvValidationException {
        if (this.hasHeaders) {
            final String[] values = this.reader.readNext();
            return new Schema(Arrays.stream(values).map(name -> new Field(StringType, name))
                    .collect(Collectors.toList()));
        }

        final String[] values = this.reader.peek();
        return new Schema(IntStream
                .range(0, values.length)
                .mapToObj(i -> new Field(StringType, "field" + i))
                .collect(Collectors.toList()));
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public Iterator<RecordBatch> scan(List<String> projection) {
        return new CsvDataSourceIterator(this.reader, this.schema, projection, this.batchSize);
    }

    public class CsvDataSourceIterator implements Iterator<RecordBatch> {
        private final CSVReader reader;
        private String[] nextLine;
        private boolean reachedEnd;
        private final Schema schema;
        private final int[] projection;
        private final int batchSize;


        public CsvDataSourceIterator(final CSVReader reader,
                                     final Schema schema,
                                     final List<String> projection,
                                     final int batchSize) {
            this.reader = reader;
            this.nextLine = null;
            this.reachedEnd = false;
            this.schema = schema;
            this.projection = projection.stream().mapToInt(name -> schema.findIndex(name)).toArray();
            this.batchSize = batchSize;
        }

        @Override
        public boolean hasNext() {
            if (this.nextLine != null) {
                return true;
            } else if (this.reachedEnd) {
                return false;
            } else {
                this.nextLine = readNext();
                if (this.nextLine != null) {
                    return true;
                } else {
                    this.reachedEnd = true;
                    return false;
                }
            }
        }

        @Override
        public RecordBatch next() {
            List<AnyValueColumn> arrays = Arrays.stream(projection)
                    .mapToObj(idx -> new AnyValueColumn(schema.getField(idx).getType(), this.batchSize) {
                    })
                    .collect(Collectors.toList());

            String[] line = this.nextLine;
            if (line != null) {
                this.nextLine = null;
            } else {
                this.nextLine = readNext();
            }

            int count = 0;
            while (line != null) {
                for (int idx = 0; idx < projection.length; idx++) {
                    final FieldType fieldType = schema.getField(projection[idx]).getType();
                    final Object value = TypeConverter.toObject(fieldType.clazz, line[projection[idx]]);
                    arrays.get(idx).add(value);
                }
                count++;

                if (count == this.batchSize) break;
                line = readNext();
            }

            final List<ColumnVector> columns = new ArrayList<>(projection.length);
            for (int idx = 0; idx < projection.length; idx++) {
                columns.add(arrays.get(idx).createColumn());
            }

            return new RecordBatch(this.schema.project(this.projection), columns);
        }

        private String[] readNext() {
            try {
                return this.reader.readNext();
            } catch (Exception ex) {
                throw new RuntimeException("Error reading csv", ex);
            }
        }
    }
}
