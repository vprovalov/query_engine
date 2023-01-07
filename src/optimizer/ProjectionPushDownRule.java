package optimizer;

import logicalplan.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static optimizer.Optimizer.extractColumns;

public class ProjectionPushDownRule implements OptimizerRule {
    @Override
    public LogicalPlan optimize(LogicalPlan plan) {
        return pushDown(plan, new ArrayList<String>());
    }

    private LogicalPlan pushDown(final LogicalPlan plan, final List<String> columnNames) {
        if (plan instanceof Projection) {
            extractColumns(((Projection) plan).getExpr(), ((Projection) plan).getInput(), columnNames);
            final LogicalPlan input = pushDown(((Projection) plan).getInput(), columnNames);
            return new Projection(input, ((Projection) plan).getExpr());
        } else if (plan instanceof Selection) {
            extractColumns(((Selection) plan).getExpr(), ((Selection) plan).getInput(), columnNames);
            final LogicalPlan input = pushDown(((Selection) plan).getInput(), columnNames);
            return new Selection(input, ((Selection) plan).getExpr());
        } else if (plan instanceof Aggregate) {
            extractColumns(((Aggregate) plan).getGroupExpr(), ((Aggregate) plan).getInput(), columnNames);
            extractColumns(((Aggregate) plan).getAggregateExpr(), ((Aggregate) plan).getInput(), columnNames);
            final LogicalPlan input = pushDown(((Aggregate) plan).getInput(), columnNames);
            return new Aggregate(input, ((Aggregate) plan).getGroupExpr(), ((Aggregate) plan).getAggregateExpr());
        } else if (plan instanceof Scan) {
            final Set<String> validFieldNames = ((Scan) plan).getDataSource().getSchema().getFieldNames();
            final List<String> pushDown = validFieldNames.stream().filter(it -> columnNames.contains(it)).sorted().collect(Collectors.toList());
            return new Scan(((Scan) plan).getPath(), ((Scan) plan).getDataSource(), pushDown);
        } else {
            throw new IllegalStateException("pushdown does not support " + plan.toString());
        }
    }
}
