package physicalplan.expression.math;

import physicalplan.expression.Expression;

public class AddExpression extends MathExpression {
    public AddExpression(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    int evaluateI(int left, int right) {
        return left + right;
    }

    @Override
    long evaluateL(long left, long right) {
        return left + right;
    }

    @Override
    float evaluateF(float left, float right) {
        return left + right;
    }

    @Override
    double evaluateD(double left, double right) {
        return left + right;
    }
}
