package datatypes;

public class LiteralValueVector implements ColumnVector {
    private final FieldType type;
    private final int size;
    private final Object value;

    public LiteralValueVector(final FieldType type, final int size, final Object value) {
        this.type = type;
        this.size = size;
        this.value = value;
    }

    @Override
    public FieldType getType() {
        return type;
    }

    @Override
    public Object getValue(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        }

        return value;
    }

    @Override
    public int getSize() {
        return this.size;
    }
}
