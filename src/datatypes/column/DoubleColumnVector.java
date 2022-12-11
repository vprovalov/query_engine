package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

public class DoubleColumnVector implements ColumnVector {
    private final double[] values;

    public DoubleColumnVector(final double[] values) {
        if (values == null) throw new IllegalArgumentException("values can't be null");
        this.values = values;
    }

    @Override
    public FieldType getType() {
        return FieldType.DoubleType;
    }

    @Override
    public Object getValue(int index) {
        return this.values[index];
    }

    @Override
    public int getSize() {
        return this.values.length;
    }
}
