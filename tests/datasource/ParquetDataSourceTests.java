package datasource;

import datatypes.Field;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParquetDataSourceTests {
    @Test
    public void getSchema_returnCorrectSchema() {
        // arrange:
        final ParquetDataSource dataSource = new ParquetDataSource(Paths.get("./testdata/alltypes_plain.parquet"), 10);

        // act:
        final Schema schema = dataSource.getSchema();

        // assert:
        Assertions.assertNotNull(schema);
        final String[] expected = new String[] { "id", "bool_col", "tinyint_col", "smallint_col", "int_col", "bigint_col", "float_col", "double_col", "date_string_col", "string_col", "timestamp_col" };
        final FieldType[] expectedTypes = new FieldType[] { FieldType.Int32Type, FieldType.BooleanType, FieldType.Int32Type, FieldType.Int32Type, FieldType.Int32Type, FieldType.Int64Type, FieldType.FloatType, FieldType.DoubleType, FieldType.StringType, FieldType.Int64Type };
        Assertions.assertEquals(expected.length, schema.getFieldsCount());
        for (int i = 0; i < expected.length; i++) {
            final Field field = schema.getField(i);
            Assertions.assertEquals(field.getName(), expected[i]);
        }
    }

    @Test
    public void scan_returnsCorrectRecords() {
        // arrange:
        final ParquetDataSource dataSource = new ParquetDataSource(Paths.get("./testdata/alltypes_plain.parquet"), 10);
        final List<String> projection = Stream.of(
                "id", "bool_col", "tinyint_col", "smallint_col", "int_col",
                "bigint_col", "float_col", "double_col", "date_string_col", "string_col",
                "timestamp_col").collect(Collectors.toList());

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(projection);

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(8, batch.getRowCount());
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    public void scan_whenProjectionContainsSubsetOfFiles_returnsCorrectRecords2() {
        // arrange:
        final ParquetDataSource dataSource = new ParquetDataSource(Paths.get("./testdata/employee.parquet"), 10);
        final List<String> projection = Stream.of(
                "id", "first_name", "last_name").collect(Collectors.toList());

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(projection);

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(4, batch.getRowCount());
        Assertions.assertFalse(it.hasNext());

        final String[] expectedFirstNames = new String[] {"Bill", "Gregg", "John", "Von"};
        final String[] expectedLastNames = new String[] {"Hopkins", "Langford", "Travis", "Mill"};
        for (int i = 0; i < expectedFirstNames.length; i++) {
            Assertions.assertEquals(expectedFirstNames[i], batch.getField(1).getValue(i));
            Assertions.assertEquals(expectedLastNames[i], batch.getField(2).getValue(i));
        }
    }

    @Test
    public void scan_whenProjectionOrderDoesNotMatchSourceOrder_returnsCorrectRecords2() {
        // arrange:
        final ParquetDataSource dataSource = new ParquetDataSource(Paths.get("./testdata/employee.parquet"), 10);
        final List<String> projection = Stream.of(
                "last_name", "id", "first_name" ).collect(Collectors.toList());

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(projection);

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(4, batch.getRowCount());
        Assertions.assertEquals(3, batch.getColumnCount());
        Assertions.assertFalse(it.hasNext());

        final String[] expectedFirstNames = new String[] {"Bill", "Gregg", "John", "Von"};
        final String[] expectedLastNames = new String[] {"Hopkins", "Langford", "Travis", "Mill"};
        for (int i = 0; i < expectedFirstNames.length; i++) {
            Assertions.assertEquals(expectedLastNames[i], batch.getField(0).getValue(i));
            Assertions.assertEquals(expectedFirstNames[i], batch.getField(2).getValue(i));
        }
    }
}



