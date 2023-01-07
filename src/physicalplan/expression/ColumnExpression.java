package physicalplan.expression;

import datatypes.ColumnVector;
import datatypes.RecordBatch;

public class ColumnExpression implements Expression {
    private final int value;

    public ColumnExpression(final int value) {
        this.value = value;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        return input.getField(this.value);
    }

    @Override
    public String toString() {
        return "#" + this.value;
    }
}
