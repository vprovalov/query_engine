package logicalplan;

import datatypes.Schema;
import datatypes.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregate implements LogicalPlan {
    private final LogicalPlan input;
    private final List<LogicalExpr> groupExpr;
    private final List<LogicalExpr> aggregateExpr;

    public Aggregate(final LogicalPlan input, final List<LogicalExpr> groupExpr, final List<LogicalExpr> aggregateExpr) {
        this.input = input;
        this.groupExpr = groupExpr;
        this.aggregateExpr = aggregateExpr;
    }

    @Override
    public Schema getSchema() {
        return new Schema(
                Stream.concat(
                    this.groupExpr.stream().map(it -> it.toField(this.input)),
                    this.aggregateExpr.stream().map(it -> it.toField(this.input)))
                .collect(Collectors.toList()));
    }

    @Override
    public List<LogicalPlan> getChildren() {
        final List<LogicalPlan> plans = new ArrayList<>();
        plans.add(this.input);
        return plans;
    }

    public LogicalPlan getInput() { return this.input; }

    public List<LogicalExpr> getGroupExpr() { return this.groupExpr; }
    public List<LogicalExpr> getAggregateExpr() { return this.aggregateExpr; }

    @Override
    public String toString() {
        return "Aggregate: groutpExpr=" + this.groupExpr.toString() + " aggregateExpr=" + this.aggregateExpr.toString();
    }
}
