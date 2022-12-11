package datasource;

import com.opencsv.exceptions.CsvValidationException;
import datatypes.ColumnVector;
import datatypes.Field;
import datatypes.RecordBatch;
import datatypes.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static datatypes.FieldType.Int32Type;
import static datatypes.FieldType.StringType;

public class CsvDataSourceTests {
    @Test
    public void getSchema_whenFileHasHeader_returnCorrectSchema() throws CsvValidationException, IOException {
        // arrange:
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.empty(),
                true,
                10);

        // act:
        final Schema schema = dataSource.getSchema();

        // assert:
        Assertions.assertNotNull(schema);
        final String[] expected = new String[] { "id","first_name","last_name","state","job_title","salary" };
        Assertions.assertEquals(expected.length, schema.getFieldsCount());
        for (int i = 0; i < expected.length; i++) {
            final Field field = schema.getField(i);
            Assertions.assertEquals(expected[i], field.getName());
            Assertions.assertEquals(StringType, field.getType());
        }
    }

    @Test
    public void getSchema_whenFileDoesNotHaveHeader_returnCorrectSchema() throws CsvValidationException, IOException {
        // arrange:
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee_no_header.csv"),
                Optional.empty(),
                false,
                10);

        // act:
        final Schema schema = dataSource.getSchema();

        // assert:
        Assertions.assertNotNull(schema);
        final String[] expected = new String[] { "field0","field1","field2","field3","field4","field5" };
        Assertions.assertEquals(expected.length, schema.getFieldsCount());
        for (int i = 0; i < expected.length; i++) {
            final Field field = schema.getField(i);
            Assertions.assertEquals(expected[i], field.getName());
            Assertions.assertEquals(StringType, field.getType());
        }
    }

    @Test
    public void scan_whenFileHasHeader_returnAllRecordBatches() throws CsvValidationException, IOException {
        // arrange:
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.empty(),
                true,
                10);
        final String[] projection = new String[] { "id","first_name","last_name","state","job_title","salary" };

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(Arrays.stream(projection).collect(Collectors.toList()));

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(6, batch.getColumnCount());
        Assertions.assertEquals(4, batch.getRowCount());
        assertColumnEquals(batch.getField(0), new String[] { "1", "2", "3", "4" });
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    public void scan_whenFileDoesNotHaveHeader_returnAllRecordBatches() throws CsvValidationException, IOException {
        // arrange:
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee_no_header.csv"),
                Optional.empty(),
                false,
                10);
        final String[] projection = new String[] { "field0","field1","field2","field3","field4","field5" };

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(Arrays.stream(projection).collect(Collectors.toList()));

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(6, batch.getColumnCount());
        Assertions.assertEquals(4, batch.getRowCount());
        assertColumnEquals(batch.getField(0), new String[] { "1", "2", "3", "4" });
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    public void scan_whenFileHasHeader_returnProjectedRecordsOnly() throws CsvValidationException, IOException {
        // arrange:
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.empty(),
                true,
                10);
        final String[] projection = new String[] { "id","first_name" };

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(Arrays.stream(projection).collect(Collectors.toList()));

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(2, batch.getColumnCount());
        Assertions.assertEquals(4, batch.getRowCount());
        assertColumnEquals(batch.getField(0), new String[] { "1", "2", "3", "4" });
        assertColumnEquals(batch.getField(1), new String[] { "Bill", "Gregg", "John", "Von" });
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    public void scan_whenSchemaSpecified_returnCorrectTypes() throws CsvValidationException, IOException {
        // arrange:
        final List<Field> fields = new ArrayList<>();
        fields.add(new Field(Int32Type, "id"));
        fields.add(new Field(StringType, "first_name"));
        fields.add(new Field(StringType, "last_name"));
        fields.add(new Field(StringType, "state"));
        fields.add(new Field(StringType, "job_title"));
        fields.add(new Field(Int32Type, "salary"));

        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.of(new Schema(fields)),
                true,
                10);
        final String[] projection = new String[] { "id","first_name","last_name","state","job_title","salary" };

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(Arrays.stream(projection).collect(Collectors.toList()));

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(6, batch.getColumnCount());
        Assertions.assertEquals(4, batch.getRowCount());
        assertColumnEquals(batch.getField(0), new Integer[] { 1, 2, 3, 4 });
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    public void scan_whenProjectionOrderDoesNotMatchSourceOrder_returnCorrectValues() throws CsvValidationException, IOException {
        // arrange:
        final List<Field> fields = new ArrayList<>();
        fields.add(new Field(Int32Type, "id"));
        fields.add(new Field(StringType, "first_name"));
        fields.add(new Field(StringType, "last_name"));
        fields.add(new Field(StringType, "state"));
        fields.add(new Field(StringType, "job_title"));
        fields.add(new Field(Int32Type, "salary"));

        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.of(new Schema(fields)),
                true,
                10);
        final String[] projection = new String[] { "id", "last_name", "first_name" };

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(Arrays.stream(projection).collect(Collectors.toList()));

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(3, batch.getColumnCount());
        Assertions.assertEquals(4, batch.getRowCount());

        final String[] expectedFirstNames = new String[] {"Bill", "Gregg", "John", "Von"};
        final String[] expectedLastNames = new String[] {"Hopkins", "Langford", "Travis", "Mill"};
        for (int i = 0; i < expectedFirstNames.length; i++) {
            Assertions.assertEquals(expectedLastNames[i], batch.getField(1).getValue(i));
            Assertions.assertEquals(expectedFirstNames[i], batch.getField(2).getValue(i));
        }
    }

    @Test
    public void scan_whenFileHasHeader_readInBatches() throws CsvValidationException, IOException {
        // arrange:
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.empty(),
                true,
                2);
        final String[] projection = new String[] { "id","first_name","last_name","state","job_title","salary" };

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(Arrays.stream(projection).collect(Collectors.toList()));

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(2, batch.getRowCount());
        Assertions.assertTrue(it.hasNext());
        batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(2, batch.getRowCount());
        Assertions.assertFalse(it.hasNext());
    }

    @Test
    public void scan_whenLargeFile_readInBatches() throws CsvValidationException, IOException {
        // arrange:
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/large.csv"),
                Optional.empty(),
                true,
                100);
        final String[] projection = new String[] { "id", "name", "title", "region", "category" };

        // act:
        final Iterator<RecordBatch> it = dataSource.scan(Arrays.stream(projection).collect(Collectors.toList()));

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(100, batch.getRowCount());
        Assertions.assertTrue(it.hasNext());
        batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(100, batch.getRowCount());
        Assertions.assertTrue(it.hasNext());
    }

    private static <T> void assertColumnEquals(final ColumnVector columnVector, final T[] values) {
        Assertions.assertEquals(values.length, columnVector.getSize());
        for (int i = 0; i < values.length; i++) {
            Assertions.assertEquals(values[i], columnVector.getValue(i));
        }
    }
}
