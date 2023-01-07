package physicalplan.expression.binary;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.column.BooleanColumnVector;
import physicalplan.expression.Expression;

public abstract class BooleanExpression extends BinaryExpression {
    public BooleanExpression(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    public ColumnVector evaluate(ColumnVector left, ColumnVector right) {
        final boolean[] column = new boolean[left.getSize()];
        final int size = left.getSize();
        final FieldType type = left.getType();

        for (int i = 0; i < size; i++) {
            column[i] = this.evaluate(left.getValue(i), right.getValue(i), type);
        }

        return new BooleanColumnVector(column);
    }

    abstract boolean evaluate(final Object left, final Object right, final FieldType type);
}
