package logicalplan.binary;

import logicalplan.LogicalExpr;

public abstract class BinaryExpr implements LogicalExpr {
    protected final String name;
    protected final String op;
    protected final LogicalExpr left;
    protected final LogicalExpr right;

    public BinaryExpr(final String name, final String op, final LogicalExpr left, final LogicalExpr right) {
        this.name = name;
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return this.left.toString() + " " + this.op + " " + this.right.toString();
    }
}
