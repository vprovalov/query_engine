package logicalplan;

import com.opencsv.exceptions.CsvValidationException;
import datasource.CsvDataSource;
import logicalplan.binary.Eq;
import logicalplan.literal.LiteralString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogicalPlanHelperTests {
    @Test
    public void format_withProjection_returnsCorrectString() throws CsvValidationException, IOException {
        // arrange:
        final LogicalPlan scan = new Scan("employee", new CsvDataSource(Paths.get("./testdata/employee.csv"), Optional.empty(), true, 10), new ArrayList<>());

        final LogicalExpr stateColumn = new Column("state");
        final LogicalExpr stringLiteral = new LiteralString("CO");
        final LogicalExpr filterExpr = new Eq(stateColumn, stringLiteral);
        final LogicalPlan filter = new Selection(scan, filterExpr);

        final List<LogicalExpr> projectionExpr = new ArrayList<>();
        projectionExpr.add(new Column("id"));
        projectionExpr.add(new Column("first_name"));
        projectionExpr.add(new Column("last_name"));
        projectionExpr.add(new Column("state"));
        projectionExpr.add(new Column("salary"));
        final LogicalPlan projection = new Projection(filter, projectionExpr);

        // act:
        final String str = LogicalPlanHelper.format(projection);

        // assert:
        Assertions.assertNotNull(str);
        final String expected= "Projection: #id, #first_name, #last_name, #state, #salary\n" +
                        "\tFilter: #state = 'CO'\n" +
                        "\t\tScan: employee; projection=none\n";
        Assertions.assertEquals(expected, str);
    }
}
