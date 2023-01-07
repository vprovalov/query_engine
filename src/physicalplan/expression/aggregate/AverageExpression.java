package physicalplan.expression.aggregate;

import physicalplan.expression.Expression;

public class AverageExpression implements AggregateExpression {
    private final Expression expr;

    public AverageExpression(Expression expr) {
        this.expr = expr;
    }

    @Override
    public Expression getInputExpr() {
        return this.expr;
    }

    @Override
    public Accumulator createAccumulator() {
        return new AverageAccumulator();
    }

    @Override
    public String toString() {
        return "AVG(" + this.expr + ")";
    }

    private static class AverageAccumulator implements Accumulator {
        private Object value;
        private int count = 0;

        @Override
        public void accumulate(Object value) {
            if (this.value == null) {
                this.value = value;
                this.count = 1;
            } else {
                if (value instanceof Integer) {
                    this.value = (int)this.value + (int)value;
                    this.count++;
                } else if (value instanceof Long) {
                    this.value = (long)this.value + (long)value;
                    this.count++;
                } else if (value instanceof Float) {
                    this.value = (float)this.value + (float)value;
                    this.count++;
                } else if (value instanceof Double) {
                    this.value = (double)this.value + (double)value;
                    this.count++;
                } else {
                    throw new IllegalStateException("Accumulator does not support type: " + value.getClass().getSimpleName());
                }
            }
        }

        @Override
        public Object finalValue() {
            if (value instanceof Integer) {
                return (int) this.value / this.count;
            } else if (value instanceof Long) {
                return (long) this.value / this.count;
            } else if (value instanceof Float) {
                return (float) this.value / this.count;
            } else if (value instanceof Double) {
                return (double) this.value / this.count;
            }
            throw new IllegalStateException("Accumulator does not support type: " + this.value.getClass().getSimpleName());
        }
    }
}
