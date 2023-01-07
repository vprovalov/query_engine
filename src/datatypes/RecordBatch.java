package datatypes;

import java.util.List;

public class RecordBatch {
    private final Schema schema;
    private final List<ColumnVector> fields;

    public RecordBatch(final Schema schema, final List<ColumnVector> fields) {
        this.schema = schema;
        this.fields = fields;
    }

    public int getRowCount() {
        if (this.fields.size() > 0) {
            return this.fields.get(0).getSize();
        }

        return 0;
    }

    public int getColumnCount() {
        return this.fields.size();
    }

    public ColumnVector getField(int index) {
        return this.fields.get(index);
    }
    public Schema getSchema() { return this.schema; }
}
