package logicalplan.binary;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class BooleanBinaryExpr extends BinaryExpr {
    public BooleanBinaryExpr(final String name, final String op, final LogicalExpr left, final LogicalExpr right) {
        super(name, op, left, right);
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.BooleanType, this.name);
    }
}
