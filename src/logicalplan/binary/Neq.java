package logicalplan.binary;

import logicalplan.LogicalExpr;

public class Neq extends BooleanBinaryExpr {
    public Neq(LogicalExpr left, LogicalExpr right) {
        super("neq", "!=", left, right);
    }
}
