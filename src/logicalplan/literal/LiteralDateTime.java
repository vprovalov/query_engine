package logicalplan.literal;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LiteralDateTime implements LogicalExpr {
    private final Date value;
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat();

    public LiteralDateTime(final Date value) {
        this.value = value;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.DateTime, dateFormat.format(this.value));
    }

    @Override
    public String toString() {
        return this.dateFormat.format(this.value);
    }

    public Date getValue() { return this.value; }
}
