package logicalplan;

import datasource.DataSource;
import datatypes.Schema;

import java.nio.file.Path;
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

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public List<String> getProjection() {
        return this.projection;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Scan: ");
        sb.append(this.path);
        sb.append("; projection=");

        if (this.projection.size() > 0) {
            sb.append("[");
            sb.append(String.join(", ", this.projection));
            sb.append("]");
        } else {
            sb.append("none");
        }

        return sb.toString();
    }

    public String getPath() {
        return path;
    }
}
