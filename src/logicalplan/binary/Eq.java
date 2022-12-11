package logicalplan.binary;

import logicalplan.LogicalExpr;

public class Eq extends BooleanBinaryExpr {
    public Eq(LogicalExpr left, LogicalExpr right) {
        super("eq", "=", left, right);
    }
}
