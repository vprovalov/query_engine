package physicalplan;

import datasource.DataSource;
import datatypes.RecordBatch;
import datatypes.Schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScanExec implements PhysicalPlan{
    private final DataSource ds;
    private final List<String> projection;

    public ScanExec(DataSource ds, List<String> projection) {
        this.ds = ds;
        this.projection = projection;
    }

    @Override
    public Schema getSchema() {
        return ds.getSchema().select(projection);
    }

    @Override
    public Iterator<RecordBatch> execute() {
        return this.ds.scan(this.projection);
    }

    @Override
    public List<PhysicalPlan> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ScanExec: schema=" + this.getSchema() + ", projection=" + String.join(", ", this.projection);
    }
}
