package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

import java.util.Date;

public class DateTimeColumnVector implements ColumnVector {
    private final Date[] values;

    public DateTimeColumnVector(final Date[] values) {
        if (values == null) throw new IllegalArgumentException("values can't be null");
        this.values = values;
    }

    @Override
    public FieldType getType() {
        return null;
    }

    @Override
    public Object getValue(int index) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ColumnVector filter(final BooleanColumnVector bitVector) {
        if (bitVector.getSize() != this.getSize()) {
            throw new IllegalStateException("Bit vector size doesn't match column size");
        }

        final int size = bitVector.countPositives();
        final Date[] column = new Date[size];
        int count = 0;
        for (int idx = 0; idx < this.getSize(); idx++) {
            if (bitVector.get(idx)) {
                column[count++] = this.values[idx];
            }
        }

        return new DateTimeColumnVector(column);
    }
}
