package physicalplan.expression.binary;

import datatypes.ColumnVector;
import datatypes.RecordBatch;
import physicalplan.expression.Expression;

public abstract class BinaryExpression implements Expression {
    private Expression leftExpr;
    private Expression rightExpr;

    public BinaryExpression(Expression leftExpr, Expression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        final ColumnVector ll = leftExpr.evaluate(input);
        final ColumnVector rr = rightExpr.evaluate(input);
        if (ll.getSize() != rr.getSize()) {
            throw new IllegalStateException("Binary expression operands do not have same size: " +
                    ll.getSize() + " != " + rr.getSize());
        }
        if (ll.getType() != rr.getType()) {
            throw new IllegalStateException("Binary expression operands do not have same type: " +
                    ll.getType() + " != " + rr.getType());
        }

        return evaluate(ll, rr);
    }

    protected abstract ColumnVector evaluate(final ColumnVector left, final ColumnVector right);
}
