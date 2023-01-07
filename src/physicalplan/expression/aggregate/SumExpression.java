package physicalplan.expression.aggregate;

import physicalplan.expression.Expression;

public class SumExpression implements AggregateExpression {
    private final Expression expr;

    public SumExpression(Expression expr) {
        this.expr = expr;
    }

    @Override
    public Expression getInputExpr() {
        return this.expr;
    }

    @Override
    public Accumulator createAccumulator() {
        return new SumAccumulator();
    }

    @Override
    public String toString() {
        return "SUM(" + this.expr + ")";
    }

    private static class SumAccumulator implements Accumulator {
        private Object value;

        @Override
        public void accumulate(Object value) {
            if (this.value == null) {
                this.value = value;
            } else {
                if (value instanceof Integer) {
                    this.value = (int)this.value + (int)value;
                } else if (value instanceof Long) {
                    this.value = (long)this.value + (long)value;
                } else if (value instanceof Float) {
                    this.value = (float)this.value + (float)value;
                } else if (value instanceof Double) {
                    this.value = (double)this.value + (double)value;
                } else {
                    throw new IllegalStateException("Accumulator does not support type: " + value.getClass().getSimpleName());
                }
            }
        }

        @Override
        public Object finalValue() {
            return this.value;
        }
    }
}
