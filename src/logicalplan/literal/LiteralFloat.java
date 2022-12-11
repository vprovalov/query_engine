package logicalplan.literal;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class LiteralFloat implements LogicalExpr {
    private final float value;

    public LiteralFloat(final float value) {
        this.value = value;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.FloatType, Float.toString(this.value));
    }

    @Override
    public String toString() {
        return Float.toString(this.value);
    }
}
