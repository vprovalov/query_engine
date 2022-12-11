package datatypes;

public interface ColumnVector {
    FieldType getType();
    Object getValue(int index);
    int getSize();
}
