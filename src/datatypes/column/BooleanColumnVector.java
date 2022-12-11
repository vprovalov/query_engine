package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

public class BooleanColumnVector implements ColumnVector {
    private final boolean[] values;

    public BooleanColumnVector(final boolean[] values) {
        if (values == null) throw new IllegalArgumentException("values can't be null");
        this.values = values;
    }

    @Override
    public FieldType getType() {
        return FieldType.BooleanType;
    }

    @Override
    public Object getValue(int index) {
        return values[index];
    }

    @Override
    public int getSize() {
        return values.length;
    }
}
