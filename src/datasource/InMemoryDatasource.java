package datasource;

import datatypes.RecordBatch;
import datatypes.Schema;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryDatasource implements DataSource {
    private Schema schema;
    private List<RecordBatch> data;

    public InMemoryDatasource(final Schema schema, final List<RecordBatch> data) {
        this.schema = schema;
        this.data = data;
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public Iterator<RecordBatch> scan(List<String> projection) {
        return null;
    }

    private class InMemoryRecordBatchIterator implements Iterator<RecordBatch> {
        private int cursor;
        private List<Integer> projectionIndices;

        public InMemoryRecordBatchIterator(final List<Integer> projectionIndices) {
            this.cursor = 0;
            this.projectionIndices = projectionIndices;
        }

        @Override
        public boolean hasNext() {
            return this.cursor < InMemoryDatasource.this.data.size();
        }

        @Override
        public RecordBatch next() {
            final RecordBatch original = InMemoryDatasource.this.data.get(this.cursor);

            return new RecordBatch(
                    InMemoryDatasource.this.schema.project(this.projectionIndices),
                    this.projectionIndices.stream().map(idx -> original.getField(idx)).collect(Collectors.toList())
            );
        }
    }
}
