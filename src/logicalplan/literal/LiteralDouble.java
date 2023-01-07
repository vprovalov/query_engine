package logicalplan.literal;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class LiteralDouble implements LogicalExpr {
    private final double value;

    public LiteralDouble(final double value) {
        this.value = value;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.DoubleType, Double.toString(this.value));
    }

    @Override
    public String toString() {
        return Double.toString(this.value);
    }

    public double getValue() { return this.value; }
}
