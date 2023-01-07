package datatypes;

import datatypes.column.BooleanColumnVector;

public interface ColumnVector {
    FieldType getType();
    Object getValue(int index);
    int getSize();
    ColumnVector filter(final BooleanColumnVector bitVector);
}
