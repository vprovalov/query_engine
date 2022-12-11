package logicalplan.binary;

import logicalplan.LogicalExpr;

public class GtEq extends BooleanBinaryExpr {
    public GtEq(LogicalExpr left, LogicalExpr right) {
        super("gteq", ">=", left, right);
    }
}
