package logicalplan.binary;

import logicalplan.LogicalExpr;

public class Lt extends BooleanBinaryExpr {
    public Lt(LogicalExpr left, LogicalExpr right) {
        super("lt", "<", left, right);
    }
}
