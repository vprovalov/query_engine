package physicalplan.expression.binary;

import datatypes.FieldType;
import physicalplan.expression.Expression;

import java.util.Date;

public class LtEqExpression extends BooleanExpression {
    public LtEqExpression(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    boolean evaluate(Object left, Object right, FieldType type) {
        switch (type) {
            case Int32Type:
                return (int)left <= (int)right;
            case Int64Type:
                return (long)left <= (long)right;
            case FloatType:
                return (float)left <= (float)right;
            case DoubleType:
                return (double)left <= (double)right;
            case StringType:
                return ((String)left).compareTo((String)right) <= 0;
            case DateTime:
                return ((Date)left).compareTo((Date)right) <= 0;
            default:
                throw new IllegalStateException("Field type not supported: " + type.toString());
        }
    }
}
