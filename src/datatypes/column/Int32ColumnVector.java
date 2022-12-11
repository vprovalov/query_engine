package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

public class Int32ColumnVector implements ColumnVector {
    private final int[] values;

    public Int32ColumnVector(final int[] values) {
        if (values == null) throw new IllegalArgumentException("values can't be null");
        this.values = values;
    }

    @Override
    public FieldType getType() {
        return FieldType.Int32Type;
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
