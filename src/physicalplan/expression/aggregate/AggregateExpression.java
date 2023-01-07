package physicalplan.expression.aggregate;

import physicalplan.expression.Expression;

public interface AggregateExpression {
    Expression getInputExpr();
    Accumulator createAccumulator();
}
