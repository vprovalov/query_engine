package physicalplan.expression.aggregate;

import physicalplan.expression.Expression;

public class CountExpression implements AggregateExpression {
    private final Expression expr;

    public CountExpression(Expression expr) {
        this.expr = expr;
    }

    @Override
    public Expression getInputExpr() {
        return this.expr;
    }

    @Override
    public Accumulator createAccumulator() {
        return new CountAccumulator();
    }

    @Override
    public String toString() {
        return "COUNT(" + this.expr + ")";
    }

    private static class CountAccumulator implements Accumulator {
        private int value = 0;

        @Override
        public void accumulate(Object value) {
            this.value += 1;
        }

        @Override
        public Object finalValue() {
            return this.value;
        }
    }
}
