package logicalplan.binary.math;

import logicalplan.LogicalExpr;

public class Multiply extends MathExpr {
    public Multiply(LogicalExpr left, LogicalExpr right) {
        super("mult", "*", left, right);
    }
}
