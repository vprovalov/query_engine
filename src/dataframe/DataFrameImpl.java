package dataframe;

import datatypes.Schema;
import logicalplan.*;

import java.util.List;

public class DataFrameImpl implements DataFrame {
    private final LogicalPlan plan;

    public DataFrameImpl(final LogicalPlan plan) {
        this.plan = plan;
    }

    @Override
    public DataFrame project(List<LogicalExpr> expr) {
        return new DataFrameImpl(new Projection(this.plan, expr));
    }

    @Override
    public DataFrame filter(LogicalExpr expr) {
        return new DataFrameImpl(new Selection(this.plan, expr));
    }

    @Override
    public DataFrame aggregate(List<LogicalExpr> groupBy, List<LogicalExpr> aggregate) {
        return new DataFrameImpl(new Aggregate(this.plan, groupBy, aggregate));
    }

    @Override
    public Schema schema() {
        return this.plan.getSchema();
    }

    @Override
    public LogicalPlan logicalPlan() {
        return this.plan;
    }
}
