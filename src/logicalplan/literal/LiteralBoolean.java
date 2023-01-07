package logicalplan.literal;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class LiteralBoolean implements LogicalExpr {
    private final boolean value;

    public LiteralBoolean(final boolean value) {
        this.value = value;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.BooleanType, Boolean.toString(this.value));
    }

    @Override
    public String toString() {
        return Boolean.toString(this.value);
    }

    public boolean getValue() { return this.value; }
}
