package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

public class Int64ColumnVector implements ColumnVector {
    private final long[] values;

    public Int64ColumnVector(final long[] values) {
        if (values == null) throw new IllegalArgumentException("values can't be null");
        this.values = values;
    }

    @Override
    public FieldType getType() {
        return FieldType.Int64Type;
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
