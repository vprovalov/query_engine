package logicalplan.literal;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class LiteralString implements LogicalExpr {
    private final String value;

    public LiteralString(final String value) {
        this.value = value;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.StringType, this.value);
    }

    @Override
    public String toString() {
        return "'" + this.value + "'";
    }
}
