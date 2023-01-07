package logicalplan.aggregate;

import datatypes.Field;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class AggregateExpr implements LogicalExpr {
    protected final String name;
    protected final LogicalExpr expr;

    public AggregateExpr(final String name, final LogicalExpr expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(expr.toField(logicalPlan).getType(), this.name);
    }

    public LogicalExpr getExpr() {
        return this.expr;
    }

    @Override
    public String toString() {
        return this.name + "(" + this.expr.toString() + ")";
    }
}
