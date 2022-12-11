package logicalplan.aggregate;

import logicalplan.LogicalExpr;

public class Max extends AggregateExpr {
    public Max(LogicalExpr expr) {
        super("MAX", expr);
    }
}
