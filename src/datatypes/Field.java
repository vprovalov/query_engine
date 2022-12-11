package datatypes;

public class Field {
    private final FieldType type;
    private final String name;

    public String getName() {
        return this.name;
    }

    public FieldType getType() {
        return this.type;
    }

    public Field(final FieldType type, final String name) {
        this.type = type;
        this.name = name;
    }
}
