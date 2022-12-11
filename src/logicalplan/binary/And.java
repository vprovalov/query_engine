package logicalplan.binary;

import logicalplan.LogicalExpr;

public class And extends BooleanBinaryExpr {
    public And(LogicalExpr left, LogicalExpr right) {
        super("and", "AND", left, right);
    }
}
