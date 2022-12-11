package logicalplan.binary.math;

import logicalplan.LogicalExpr;

public class Modulus extends MathExpr {
    public Modulus(LogicalExpr left, LogicalExpr right) {
        super("mod", "%", left, right);
    }
}
