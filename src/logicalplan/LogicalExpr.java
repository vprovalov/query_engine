package logicalplan;

import datatypes.Field;
import exceptions.QueryException;


public interface LogicalExpr {
    Field toField(final LogicalPlan logicalPlan);
}
