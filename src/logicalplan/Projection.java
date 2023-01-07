package logicalplan;

import datatypes.Schema;
import datatypes.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Projection implements LogicalPlan {
    private final LogicalPlan input;
    private final List<LogicalExpr> expr;

    public Projection(final LogicalPlan input, final List<LogicalExpr> expr) {
        this.input = input;
        this.expr = expr;
    }

    @Override
    public Schema getSchema() {
        final List<Field> fields = new ArrayList<>();
        for (final LogicalExpr expr : this.expr) {
            fields.add(expr.toField(this.input));
        }

        return new Schema(fields);
    }

    @Override
    public List<LogicalPlan> getChildren() {
        final List<LogicalPlan> result = new ArrayList<>();
        result.add(this.input);
        return result;
    }

    public LogicalPlan getInput() { return this.input; }

    public List<LogicalExpr> getExpr() { return this.expr; }

    @Override
    public String toString() {
        return "Projection: " + String.join(", ", expr.stream().map(it -> it.toString()).collect(Collectors.toList()));
    }
}
