package logicalplan.binary.math;

import logicalplan.LogicalExpr;

public class Subtract extends MathExpr {
    public Subtract(LogicalExpr left, LogicalExpr right) {
        super("subtract", "-", left, right);
    }
}
