package dataframe;

import datatypes.Schema;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

import java.util.List;

public interface DataFrame {
    DataFrame project(final List<LogicalExpr> expr);
    DataFrame filter(final LogicalExpr expr);
    DataFrame aggregate(final List<LogicalExpr> groupBy, final List<LogicalExpr> aggregate);
    Schema schema();
    LogicalPlan logicalPlan();
}
