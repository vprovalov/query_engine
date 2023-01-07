package logicalplan;

import datatypes.Field;
import exceptions.QueryException;
import logicalplan.binary.*;
import logicalplan.binary.math.*;


public interface LogicalExpr {
    Field toField(final LogicalPlan logicalPlan);

    default LogicalExpr eq(final LogicalExpr right) {
        return new Eq(this, right);
    }

    default LogicalExpr neq(final LogicalExpr right) {
        return new Neq(this, right);
    }

    default LogicalExpr gt(final LogicalExpr right) {
        return new Gt(this, right);
    }

    default LogicalExpr gteq(final LogicalExpr right) {
        return new GtEq(this, right);
    }

    default LogicalExpr lt(final LogicalExpr right) {
        return new Lt(this, right);
    }

    default LogicalExpr lteq(final LogicalExpr right) {
        return new LtEq(this, right);
    }

    static LogicalExpr add(final LogicalExpr left, final LogicalExpr right) {
        return new Add(left, right);
    }

    static LogicalExpr sub(final LogicalExpr left, final LogicalExpr right) {
        return new Subtract(left, right);
    }

    static LogicalExpr div(final LogicalExpr left, final LogicalExpr right) {
        return new Divide(left, right);
    }

    static LogicalExpr mul(final LogicalExpr left, final LogicalExpr right) {
        return new Multiply(left, right);
    }

    static LogicalExpr mod(final LogicalExpr left, final LogicalExpr right) {
        return new Modulus(left, right);
    }

    static LogicalExpr alias(final LogicalExpr expr, final String alias) {
        return new Alias(expr, alias);
    }
}
