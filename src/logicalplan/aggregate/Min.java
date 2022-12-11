package logicalplan.aggregate;

import logicalplan.LogicalExpr;

public class Min extends AggregateExpr {
    public Min(LogicalExpr expr) {
        super("MIN", expr);
    }
}
