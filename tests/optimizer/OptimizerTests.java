package optimizer;

import com.opencsv.exceptions.CsvValidationException;
import dataframe.DataFrame;
import dataframe.DataFrameImpl;
import datasource.CsvDataSource;
import logicalplan.LogicalPlan;
import logicalplan.LogicalPlanHelper;
import logicalplan.Scan;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static dataframe.ExecutionContext.col;
import static dataframe.ExecutionContext.lit;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizerTests {
    @Test
    public void projectionPushDownWithSelection() throws CsvValidationException, IOException {
        final DataFrame df = csv()
                .filter(col("state").eq(lit("CO")))
                .project(List.of(col("id"), col("first_name"), col("last_name")));

        assertEquals("Projection: #id, #first_name, #last_name\n" +
                "\tFilter: #state = 'CO'\n" +
                "\t\tScan: employee; projection=none\n",
                LogicalPlanHelper.format(df.logicalPlan()));

        final OptimizerRule rule = new ProjectionPushDownRule();
        final LogicalPlan optimizedPlan = rule.optimize(df.logicalPlan());

        assertEquals("Projection: #id, #first_name, #last_name\n" +
                "\tFilter: #state = 'CO'\n" +
                "\t\tScan: employee; projection=[first_name, id, last_name, state]\n",
                LogicalPlanHelper.format(optimizedPlan));
    }

    private static DataFrame csv() throws CsvValidationException, IOException {
        final CsvDataSource dataSource = new CsvDataSource(
                Paths.get("./testdata/employee.csv"),
                Optional.empty(),
                true,
                10);

        return new DataFrameImpl(new Scan("employee", dataSource, List.of()));
    }
}
