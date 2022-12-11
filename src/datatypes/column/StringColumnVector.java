package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

public class StringColumnVector implements ColumnVector {
    private final String[] values;

    public StringColumnVector(final String[] values) {
        this.values = values;
    }

    @Override
    public FieldType getType() {
        return FieldType.StringType;
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
