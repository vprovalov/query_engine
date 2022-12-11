package logicalplan.binary;

import logicalplan.LogicalExpr;

public class Or extends BooleanBinaryExpr {
    public Or(LogicalExpr left, LogicalExpr right) {
        super("or", "OR", left, right);
    }
}
