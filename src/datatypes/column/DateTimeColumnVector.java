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
}
