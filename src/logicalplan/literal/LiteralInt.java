package logicalplan.literal;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class LiteralInt implements LogicalExpr {
    private final int value;

    public LiteralInt(final int value) {
        this.value = value;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.Int32Type, Integer.toString(this.value));
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    public int getValue() { return this.value; }
}
