package queryplanner;

import dataframe.DataFrame;
import dataframe.DataFrameImpl;
import datasource.DataSource;
import datasource.InMemoryDatasource;
import datatypes.FieldType;
import datatypes.Schema;
import datatypes.Field;
import logicalplan.LogicalPlan;
import logicalplan.LogicalPlanHelper;
import logicalplan.Scan;
import org.junit.jupiter.api.Test;
import physicalplan.PhysicalPlan;

import java.util.List;

import static dataframe.ExecutionContext.col;
import static dataframe.ExecutionContext.max;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryPlannerTests {
    @Test
    public void createPhysicalPlan_whenAggregateDataframe_returnsCorrectPhysicalPlan() {
        // arrange:
        final Schema schema = new Schema(List.of(
                new Field(FieldType.Int32Type, "passanger_count"),
                new Field(FieldType.DoubleType, "max_fare")));

        final DataSource dataSource = new InMemoryDatasource(schema, List.of());

        final DataFrame df = new DataFrameImpl(new Scan("", dataSource, List.of()));

        final LogicalPlan plan = df.aggregate(List.of(col("passanger_count")),
                List.of(max(col("max_fare")))).logicalPlan();

        final QueryPlanner queryPlanner = new QueryPlanner();

        // act:
        final PhysicalPlan physicalPlan = queryPlanner.createPhysicalPlan(plan);

        // assert:
        assertEquals(
                "Aggregate: groutpExpr=[#passanger_count] aggregateExpr=[MAX(#max_fare)]\n" +
                "\tScan: ; projection=none\n",
                LogicalPlanHelper.format(plan));
        assertEquals(
                "HashAggregateExec: groupExpr=#0 aggregateExpr=MAX(#1)\n" +
                "\tScanExec: schema=Schema(), projection=\n",
                physicalPlan.format());
    }
}
