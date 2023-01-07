package logicalplan;

import datatypes.Field;

public class ColumnIndex implements LogicalExpr {
    private final int index;

    public ColumnIndex(int index) {
        this.index = index;
    }

    @Override
    public Field toField(LogicalPlan logicalPlan) {
        return logicalPlan.getSchema().getField(this.index);
    }

    @Override
    public String toString() {
        return "#" + this.index;
    }

    public int getIndex() {
        return this.index;
    }
}
