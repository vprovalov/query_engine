package logicalplan;

import datasource.DataSource;
import datatypes.Schema;

import java.util.ArrayList;
import java.util.List;

public class Scan implements LogicalPlan {
    private final Schema schema;
    private final String path;
    private final DataSource dataSource;
    private final List<String> projection;

    public Scan(final String path, final DataSource dataSource, final List<String> projection) {
        this.path = path;
        this.dataSource = dataSource;
        this.projection = projection;
        this.schema = deriveSchema();
    }

    private Schema deriveSchema() {
        if (projection.size() > 0) {
            this.dataSource.getSchema().select(this.projection);
        }
        return this.dataSource.getSchema();
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public List<LogicalPlan> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.projection.size() > 0 ?
            "Scan: " + this.path + "; projection=" + String.join(", ", this.projection) :
            "Scan: " + this.path + "; projection=none";
    }
}
