package logicalplan.binary;

import logicalplan.LogicalExpr;

public class LtEq extends BooleanBinaryExpr {
    public LtEq(LogicalExpr left, LogicalExpr right) {
        super("lteq", "<=", left, right);
    }
}
