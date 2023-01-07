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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("Field(");
        sb.append("name=");
        sb.append(this.name);
        sb.append(", ");
        sb.append(this.type.name());
        sb.append(")");
        return sb.toString();
    }
}
