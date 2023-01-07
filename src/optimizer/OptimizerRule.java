package optimizer;

import logicalplan.LogicalPlan;

public interface OptimizerRule {
    LogicalPlan optimize(final LogicalPlan plan);
}
