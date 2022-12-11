package logicalplan.binary;

import logicalplan.LogicalExpr;

public class Gt extends BooleanBinaryExpr {
    public Gt(LogicalExpr left, LogicalExpr right) {
        super("gt", ">", left, right);
    }
}
