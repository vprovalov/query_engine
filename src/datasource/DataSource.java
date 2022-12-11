package datasource;

import datatypes.RecordBatch;
import datatypes.Schema;

import java.util.Iterator;
import java.util.List;

public interface DataSource {
    Schema getSchema();
    Iterator<RecordBatch> scan(final List<String> projection);
}
