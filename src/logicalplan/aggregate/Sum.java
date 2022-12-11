package logicalplan.aggregate;

import logicalplan.LogicalExpr;

public class Sum extends AggregateExpr {
    public Sum(LogicalExpr expr) {
        super("SUM", expr);
    }
}
