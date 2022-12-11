package logicalplan.binary.math;

import logicalplan.LogicalExpr;

public class Divide extends MathExpr {
    public Divide(LogicalExpr left, LogicalExpr right) {
        super("div", "/", left, right);
    }
}
