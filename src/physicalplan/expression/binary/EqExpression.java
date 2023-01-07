package physicalplan.expression.binary;

import datatypes.ColumnVector;
import datatypes.FieldType;
import physicalplan.expression.Expression;

public class EqExpression extends BooleanExpression {
    public EqExpression(final Expression leftExpr, final Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    boolean evaluate(final Object left, final Object right, final FieldType type) {
        switch (type) {
            case BooleanType:
                return (boolean)left == (boolean)right;
            case Int32Type:
                return (int)left == (int)right;
            case Int64Type:
                return (long)left == (long)right;
            case FloatType:
                return (float)left == (float)right;
            case DoubleType:
                return (double)left == (double)right;
            case StringType:
            case DateTime:
                return (left == null && right == null) || (left != null && left.equals(right));
            default:
                throw new IllegalStateException("Field type not supported: " + type.toString());
        }
    }
}
