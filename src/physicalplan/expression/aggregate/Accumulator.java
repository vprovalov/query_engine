package physicalplan.expression.aggregate;

public interface Accumulator {
    void accumulate(final Object value);
    Object finalValue();
}
