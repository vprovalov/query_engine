package physicalplan.expression.binary;

import datatypes.FieldType;
import physicalplan.expression.Expression;

public class OrExpression extends BooleanExpression {
    public OrExpression(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    boolean evaluate(Object left, Object right, FieldType type) {
        switch (type) {
            case Int32Type:
                return (boolean)left || (boolean)right;
            default:
                throw new IllegalStateException("Field type not supported: " + type.toString());
        }
    }
}
