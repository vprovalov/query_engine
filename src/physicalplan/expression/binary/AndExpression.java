package physicalplan.expression.binary;

import datatypes.FieldType;
import physicalplan.expression.Expression;

public class AndExpression extends BooleanExpression {
    public AndExpression(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    boolean evaluate(Object left, Object right, FieldType type) {
        switch (type) {
            case BooleanType:
                return (boolean)left && (boolean)right;
            default:
                throw new IllegalStateException("Field type not supported: " + type.toString());
        }
    }
}
