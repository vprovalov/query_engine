package logicalplan;

import datatypes.Schema;
import datatypes.Field;

import java.util.ArrayList;
import java.util.List;

public class Aggregate implements LogicalPlan {
    private final LogicalPlan input;
    private final LogicalExpr groupExpr;
    private final LogicalExpr aggregateExpr;

    public Aggregate(final LogicalPlan input, final LogicalExpr groupExpr, final LogicalExpr aggregateExpr) {
        this.input = input;
        this.groupExpr = groupExpr;
        this.aggregateExpr = aggregateExpr;
    }

    @Override
    public Schema getSchema() {
        final List<Field> fields = new ArrayList<>();
        fields.add(this.groupExpr.toField(this.input));
        fields.add(this.aggregateExpr.toField(this.input));
        return null;
    }

    @Override
    public List<LogicalPlan> getChildren() {
        final List<LogicalPlan> plans = new ArrayList<>();
        plans.add(this.input);
        return plans;
    }

    @Override
    public String toString() {
        return "Aggregate: groutpExpr=" + this.groupExpr.toString() + " aggregateExpr=" + this.aggregateExpr.toString();
    }
}
