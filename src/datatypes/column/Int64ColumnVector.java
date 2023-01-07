package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

import java.util.Date;

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

    @Override
    public ColumnVector filter(final BooleanColumnVector bitVector) {
        if (bitVector.getSize() != this.getSize()) {
            throw new IllegalStateException("Bit vector size doesn't match column size");
        }

        final int size = bitVector.countPositives();
        final long[] column = new long[size];
        int count = 0;
        for (int idx = 0; idx < this.getSize(); idx++) {
            if (bitVector.get(idx)) {
                column[count++] = this.values[idx];
            }
        }

        return new Int64ColumnVector(column);
    }
}
