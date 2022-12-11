package logicalplan.binary.math;

import logicalplan.LogicalExpr;

public class Add extends MathExpr {
    public Add(LogicalExpr left, LogicalExpr right) {
        super("add", "+", left, right);
    }
}
