package dataframe;

import logicalplan.Column;
import logicalplan.LogicalPlan;
import logicalplan.LogicalPlanHelper;
import logicalplan.binary.Eq;
import logicalplan.literal.LiteralString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static dataframe.ExecutionContext.col;
import static dataframe.ExecutionContext.lit;

public class ExecutionContextTests {
    @Test
    public void csv_whenFilterAndSelectSpecified_returnsCorretPlan() {
        // arrange:
        final ExecutionContext ctx = new ExecutionContext();

        // act:
        final LogicalPlan plan = ctx.csv(Path.of("./testdata/employee.csv"))
                .filter(new Eq(new Column("state"), new LiteralString("CO")))
                .project(List.of(new Column("id"),
                        new Column("first_name"),
                        new Column("last_name"),
                        new Column("state"),
                        new Column("salary")))
                .logicalPlan();

        // assert:
        Assertions.assertNotNull(plan);
        final String str = LogicalPlanHelper.format(plan);
        Assertions.assertEquals("Projection: #id, #first_name, #last_name, #state, #salary\n" +
                "\tFilter: #state = 'CO'\n" +
                "\t\tScan: employee.csv; projection=none\n", str);
    }

    @Test
    public void csv_whenUsedHelperFunctions_returnsCorretPlan() {
        // arrange:
        final ExecutionContext ctx = new ExecutionContext();

        // act:
        final LogicalPlan plan = ctx.csv(Path.of("./testdata/employee.csv"))
                .filter(col("state").eq(lit("CO")))
                .project(List.of(col("id"),
                        col("first_name"),
                        col("last_name"),
                        col("state"),
                        col("salary")))
                .logicalPlan();

        // assert:
        Assertions.assertNotNull(plan);
        final String str = LogicalPlanHelper.format(plan);
        Assertions.assertEquals("Projection: #id, #first_name, #last_name, #state, #salary\n" +
                "\tFilter: #state = 'CO'\n" +
                "\t\tScan: employee.csv; projection=none\n", str);
    }
}
