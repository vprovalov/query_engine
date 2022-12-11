package logicalplan.binary.math;

import datatypes.Field;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;
import logicalplan.binary.BinaryExpr;

public class MathExpr extends BinaryExpr {
    public MathExpr(String name, String op, LogicalExpr left, LogicalExpr right) {
        super(name, op, left, right);
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(left.toField(logicalPlan).getType(), this.name);
    }
}
