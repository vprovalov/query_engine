package logicalplan;

import datatypes.Schema;

import java.util.ArrayList;
import java.util.List;

public class Selection implements LogicalPlan {
    private final LogicalPlan input;
    private final LogicalExpr expr;

    public Selection(final LogicalPlan input, final LogicalExpr expr) {
        this.input = input;
        this.expr = expr;
    }

    @Override
    public Schema getSchema() {
        return this.input.getSchema();
    }

    @Override
    public List<LogicalPlan> getChildren() {
        final List<LogicalPlan> result = new ArrayList<>();
        result.add(this.input);
        return result;
    }

    public LogicalPlan getInput() { return this.input; }
    public LogicalExpr getExpr() { return this.expr; }

    @Override
    public String toString() {
        return "Filter: " + this.expr.toString();
    }
}
