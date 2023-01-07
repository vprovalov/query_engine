package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

import java.util.Date;

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

    @Override
    public ColumnVector filter(final BooleanColumnVector bitVector) {
        if (bitVector.getSize() != this.getSize()) {
            throw new IllegalStateException("Bit vector size doesn't match column size");
        }

        final int size = bitVector.countPositives();
        final double[] column = new double[size];
        int count = 0;
        for (int idx = 0; idx < this.getSize(); idx++) {
            if (bitVector.get(idx)) {
                column[count++] = this.values[idx];
            }
        }

        return new DoubleColumnVector(column);
    }
}
