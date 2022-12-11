package logicalplan;

import com.opencsv.exceptions.CsvValidationException;
import datasource.CsvDataSource;
import logicalplan.binary.And;
import logicalplan.literal.LiteralBoolean;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogicalExprTests {
    @Test
    public void toString_whenCalledAnd_returnsCorrectString() {
        // arrange:
        final LogicalExpr left = new LiteralBoolean(true);
        final LogicalExpr right = new LiteralBoolean(false);
        final LogicalExpr and = new And(left, right);

        // act:
        final String str = and.toString();

        // assert:
        Assertions.assertNotNull(str);
        Assertions.assertEquals("true AND false", str);
    }

    @Test
    public void toString_whenCalledProjection_returnsCorrectString() throws CsvValidationException, IOException {
        // arrange:
        final LogicalPlan input = new Scan("employee.csv", new CsvDataSource(Paths.get("./testdata/employee.csv"), Optional.empty(), true, 10), new ArrayList<>());
        final List<LogicalExpr> expr = new ArrayList<>();
        expr.add(new Column("id"));
        expr.add(new Column("first_name"));
        expr.add(new Column("last_name"));
        expr.add(new Column("state"));
        expr.add(new Column("salary"));
        final LogicalPlan projection = new Projection(input, expr);

        // act:
        final String str = projection.toString();

        // assert:
        Assertions.assertNotNull(str);
        Assertions.assertEquals("Projection: #id, #first_name, #last_name, #state, #salary", str);
    }
}
