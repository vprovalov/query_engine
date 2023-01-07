package physicalplan.expression.aggregate;

import physicalplan.expression.Expression;

public class MinExpression implements AggregateExpression {
    private final Expression expr;

    public MinExpression(Expression expr) {
        this.expr = expr;
    }

    @Override
    public Expression getInputExpr() {
        return this.expr;
    }

    @Override
    public Accumulator createAccumulator() {
        return new MinAccumulator();
    }

    @Override
    public String toString() {
        return "MIN(" + this.expr.toString() + ")";
    }

    private static class MinAccumulator implements Accumulator {
        private Object value;

        @Override
        public void accumulate(final Object value) {
            if (this.value == null) {
                this.value = value;
            } else {
                if (value instanceof Integer) {
                    if ((int) value < (int) this.value) {
                        this.value = value;
                    }
                } else if (value instanceof Long) {
                    if ((long) value < (long) this.value) {
                        this.value = value;
                    }
                } else if (value instanceof Float) {
                    if ((float) value < (float) this.value) {
                        this.value = value;
                    }
                } else if (value instanceof Double) {
                    if ((double) value < (double) this.value) {
                        this.value = value;
                    }
                } else if (value instanceof String) {
                    if (value.toString().compareTo(this.value.toString()) < 0) {
                        this.value = value;
                    }
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
