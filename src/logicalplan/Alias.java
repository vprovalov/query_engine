package logicalplan;

import datatypes.Field;

public class Alias implements LogicalExpr {
    private LogicalExpr expr;
    private String alias;

    public Alias(final LogicalExpr expr, final String alias) {
        this.expr = expr;
        this.alias = alias;
    }

    @Override
    public Field toField(final LogicalPlan logicalPlan) {
        return new Field(expr.toField(logicalPlan).getType(), alias);
    }

    public LogicalExpr getExpr() {
        return this.expr;
    }

    public String getAlias() {
        return this.alias;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.expr.toString());
        sb.append(" as ");
        sb.append(this.alias);
        return sb.toString();
    }
}
