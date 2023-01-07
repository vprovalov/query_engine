package datatypes;

import java.util.*;
import java.util.stream.Collectors;

public class Schema {
    private final List<Field> fields;

    public Schema(final List<Field> fields) {
        this.fields = fields;
    }

    public Schema project(final List<Integer> fieldIndices) {
        return new Schema(fieldIndices.stream().map(i -> this.fields.get(i)).collect(Collectors.toList()));
    }

    public Schema project(final int[] fieldIndices) {
        return new Schema(Arrays.stream(fieldIndices).mapToObj(i -> this.fields.get(i)).collect(Collectors.toList()));
    }

    public Schema select(final List<String> fieldNames) {
        return new Schema(fieldNames.stream()
                            .map(it -> this.getField(it))
                            .collect(Collectors.toList()));
    }

    public Field getField(final int index) {
        return this.fields.get(index);
    }

    public Field getField(final String name) {
        Optional<Field> result = this.fields.stream().filter(it -> name.equals(it.getName())).findFirst();
        if (!result.isPresent()) {
            throw new IllegalArgumentException("Field not found: " + name);
        }
        return result.get();
    }

    public int getFieldsCount() {
        return this.fields.size();
    }

    public int findIndex(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Field name can't be null");
        }

        for (int idx = 0; idx < fields.size(); idx++) {
            final Field field = fields.get(idx);
            if (name.equals(field.getName())) {
                return idx;
            }
        }

        return -1;
    }

    public Set<String> getFieldNames() {
        return this.fields.stream().map(field -> field.getName()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("Schema(");
        sb.append(String.join(", ", this.fields.stream().map(field -> field.toString()).collect(Collectors.toList())));
        sb.append(")");

        return sb.toString();
    }
}
