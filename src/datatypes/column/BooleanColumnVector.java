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

    public int countPositives() {
        int count = 0;
        for (boolean value : values) {
            if (value) {
                count++;
            }
        }
        return count;
    }

    @Override
    public ColumnVector filter(final BooleanColumnVector bitVector) {
        if (bitVector.getSize() != this.getSize()) {
            throw new IllegalStateException("Bit vector size doesn't match column size");
        }

        final int size = bitVector.countPositives();
        final boolean[] column = new boolean[size];
        int count = 0;
        for (int idx = 0; idx < this.getSize(); idx++) {
            if (bitVector.get(idx)) {
                column[count++] = this.values[idx];
            }
        }

        return new BooleanColumnVector(column);
    }

    public boolean get(final int idx) {
        return this.values[idx];
    }
}
