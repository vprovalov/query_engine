package logicalplan.aggregate;

import logicalplan.LogicalExpr;

public class Average extends AggregateExpr {
    public Average(LogicalExpr expr) {
        super("AVG", expr);
    }
}
