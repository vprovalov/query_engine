package physicalplan;

import com.opencsv.exceptions.CsvValidationException;
import datasource.CsvDataSource;
import datatypes.Field;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import physicalplan.expression.ColumnExpression;
import physicalplan.expression.aggregate.MaxExpression;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class HashAggregateExecTests {
    @Test
    public void toString_returnsCorrectString() throws CsvValidationException, IOException {
        // arrange:
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.empty(),
                true,
                10);

        final ScanExec scan = new ScanExec(dataSource, List.of("id","first_name","last_name","state","job_title","salary"));
        final Schema schema = new Schema(
                List.of(
                        new Field(FieldType.Int32Type, "max(salary)"),
                        new Field(FieldType.StringType, "job_title")));
        final HashAggregateExec exec = new HashAggregateExec(
                scan,
                List.of(new ColumnExpression(4)), // job_title
                List.of(new MaxExpression(new ColumnExpression(5))), // salary
                schema);

        // act:
        final String str = exec.toString();

        // assert:
        Assertions.assertEquals("HashAggregateExec: groupExpr=#4 aggregateExpr=MAX(#5)", str);
    }

    @Test
    public void execute_whenAggregateByJobTitle_returnsCorrectRecords() throws CsvValidationException, IOException {
        // arrange:
        final List<Field> fields = List.of(
                new Field(FieldType.Int32Type, "id"),
                new Field(FieldType.StringType, "first_name"),
                new Field(FieldType.StringType, "last_name"),
                new Field(FieldType.StringType, "state"),
                new Field(FieldType.StringType, "job_title"),
                new Field(FieldType.Int32Type, "salary")
        );
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.of(new Schema(fields)),
                true,
                10);

        final ScanExec scan = new ScanExec(dataSource, List.of("id","first_name","last_name","state","job_title","salary"));
        final Schema schema = new Schema(
                List.of(new Field(FieldType.StringType, "job_title"),
                        new Field(FieldType.Int32Type, "max(salary)")));
        final HashAggregateExec exec = new HashAggregateExec(
                scan,
                List.of(new ColumnExpression(4)), // job_title
                List.of(new MaxExpression(new ColumnExpression(5))), // salary
                schema);

        // act:
        final Iterator<RecordBatch> it = exec.execute();

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(2, batch.getColumnCount());
        Assertions.assertEquals(4, batch.getRowCount());

        Assertions.assertEquals("Driver", batch.getField(0).getValue(0));
        Assertions.assertEquals("Manager, Software", batch.getField(0).getValue(1));
        Assertions.assertEquals("Defensive End", batch.getField(0).getValue(2));
        Assertions.assertEquals("Manager", batch.getField(0).getValue(3));

        Assertions.assertEquals(10000, batch.getField(1).getValue(0));
        Assertions.assertEquals(11500, batch.getField(1).getValue(1));
        Assertions.assertEquals(11500, batch.getField(1).getValue(2));
        Assertions.assertEquals(12000, batch.getField(1).getValue(3));
    }

    @Test
    public void execute_whenAggregateByState_returnsCorrectRecords() throws CsvValidationException, IOException {
        // arrange:
        final List<Field> fields = List.of(
                new Field(FieldType.Int32Type, "id"),
                new Field(FieldType.StringType, "first_name"),
                new Field(FieldType.StringType, "last_name"),
                new Field(FieldType.StringType, "state"),
                new Field(FieldType.StringType, "job_title"),
                new Field(FieldType.Int32Type, "salary")
        );
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.of(new Schema(fields)),
                true,
                10);

        final ScanExec scan = new ScanExec(dataSource, List.of("id","first_name","last_name","state","job_title","salary"));
        final Schema schema = new Schema(
                List.of(new Field(FieldType.StringType, "state"),
                        new Field(FieldType.Int32Type, "max(salary)")));
        final HashAggregateExec exec = new HashAggregateExec(
                scan,
                List.of(new ColumnExpression(3)), // state
                List.of(new MaxExpression(new ColumnExpression(5))), // salary
                schema);

        // act:
        final Iterator<RecordBatch> it = exec.execute();

        // assert:
        Assertions.assertNotNull(it);
        Assertions.assertTrue(it.hasNext());
        final RecordBatch batch = it.next();
        Assertions.assertNotNull(batch);
        Assertions.assertEquals(2, batch.getColumnCount());
        Assertions.assertEquals(3, batch.getRowCount());

        Assertions.assertEquals("CO", batch.getField(0).getValue(0));
        Assertions.assertEquals("CA", batch.getField(0).getValue(1));
        Assertions.assertEquals("", batch.getField(0).getValue(2));

        Assertions.assertEquals(11500, batch.getField(1).getValue(0));
        Assertions.assertEquals(12000, batch.getField(1).getValue(1));
        Assertions.assertEquals(11500, batch.getField(1).getValue(2));
    }
}
