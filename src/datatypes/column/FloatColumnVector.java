package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

public class FloatColumnVector implements ColumnVector {
    private final float[] values;

    public FloatColumnVector(final float[] values) {
        if (values == null) throw new IllegalArgumentException("values can't be null");
        this.values = values;
    }

    @Override
    public FieldType getType() {
        return FieldType.FloatType;
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
