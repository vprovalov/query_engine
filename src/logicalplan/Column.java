package logicalplan;

import datatypes.Field;
import exceptions.QueryException;

public class Column implements LogicalExpr {
    private final String name;

    public Column(final String name) {
        this.name = name;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) throws QueryException {
        try {
            return logicalPlan.getSchema().getField(this.name);
        } catch (IllegalArgumentException ex) {
            throw new QueryException("No column named '" + name + "'");
        }
    }

    @Override
    public String toString() {
        return "#" + this.name;
    }

    public String getName() { return this.name; }
}
