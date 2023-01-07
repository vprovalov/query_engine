package logicalplan;

import datatypes.Field;
import datatypes.FieldType;

public class CastExpr implements LogicalExpr {
    private LogicalExpr expr;
    private FieldType type;

    public CastExpr(final LogicalExpr expr, final FieldType type) {
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Field toField(final LogicalPlan input) {
        return new Field(this.type, this.expr.toField(input).getName());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CAST(");
        sb.append(this.expr.toString());
        sb.append(" AS ");
        sb.append(this.type);
        sb.append(")");
        return sb.toString();
    }

    public LogicalExpr getExpr() {
        return expr;
    }

    public FieldType getType() {
        return type;
    }
}
