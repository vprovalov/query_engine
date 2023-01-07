package logicalplan.literal;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class LiteralLong implements LogicalExpr {
    private final long value;

    public LiteralLong(final long value) {
        this.value = value;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.Int64Type, Long.toString(this.value));
    }

    @Override
    public String toString() {
        return Long.toString(this.value);
    }

    public long getValue() { return this.value; }
}
