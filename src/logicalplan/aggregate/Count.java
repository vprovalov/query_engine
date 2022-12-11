package logicalplan.aggregate;

import datatypes.Field;
import datatypes.FieldType;
import exceptions.QueryException;
import logicalplan.LogicalExpr;
import logicalplan.LogicalPlan;

public class Count extends AggregateExpr {
    public Count(LogicalExpr expr) {
        super("COUNT", expr);
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        return new Field(FieldType.Int32Type, this.name);
    }
}
