package physicalplan.expression.aggregate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import physicalplan.expression.ColumnExpression;

import java.util.Arrays;

public class AggegateExpressionTests {
    @Test
    public void minExpression_int_accumulator() {
        // arrange:
        final Accumulator a = new MinExpression(new ColumnExpression(0)).createAccumulator();
        final int[] values = new int[] {10, 14, 4};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(4, a.finalValue());
    }

    @Test
    public void minExpression_long_accumulator() {
        // arrange:
        final Accumulator a = new MinExpression(new ColumnExpression(0)).createAccumulator();
        final long[] values = new long[] {10, 14, 4};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(4L, a.finalValue());
    }

    @Test
    public void minExpression_double_accumulator() {
        // arrange:
        final Accumulator a = new MinExpression(new ColumnExpression(0)).createAccumulator();
        final double[] values = new double[] {10.1, 14.2, 4.3};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(4.3, a.finalValue());
    }

    @Test
    public void maxExpression_int_accumulator() {
        // arrange:
        final Accumulator a = new MaxExpression(new ColumnExpression(0)).createAccumulator();
        final int[] values = new int[] {10, 14, 4};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(14, a.finalValue());
    }

    @Test
    public void maxExpression_long_accumulator() {
        // arrange:
        final Accumulator a = new MaxExpression(new ColumnExpression(0)).createAccumulator();
        final long[] values = new long[] {10, 14, 4};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(14L, a.finalValue());
    }

    @Test
    public void maxExpression_double_accumulator() {
        // arrange:
        final Accumulator a = new MaxExpression(new ColumnExpression(0)).createAccumulator();
        final double[] values = new double[] {10.1, 14.2, 4.3};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(14.2, a.finalValue());
    }

    @Test
    public void sumExpression_int_accumulator() {
        // arrange:
        final Accumulator a = new SumExpression(new ColumnExpression(0)).createAccumulator();
        final int[] values = new int[] {10, 14, 4};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(28, a.finalValue());
    }

    @Test
    public void sumExpression_long_accumulator() {
        // arrange:
        final Accumulator a = new SumExpression(new ColumnExpression(0)).createAccumulator();
        final long[] values = new long[] {10, 14, 4};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(28L, a.finalValue());
    }

    @Test
    public void sumExpression_double_accumulator() {
        // arrange:
        final Accumulator a = new SumExpression(new ColumnExpression(0)).createAccumulator();
        final double[] values = new double[] {10.0, 14.0, 4.0};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(28.0, a.finalValue());
    }

    @Test
    public void countExpression_int_accumulator() {
        // arrange:
        final Accumulator a = new CountExpression(new ColumnExpression(0)).createAccumulator();
        final int[] values = new int[] {10, 14, 4};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(3, a.finalValue());
    }

    @Test
    public void countExpression_long_accumulator() {
        // arrange:
        final Accumulator a = new CountExpression(new ColumnExpression(0)).createAccumulator();
        final long[] values = new long[] {10, 14, 4};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(3, a.finalValue());
    }

    @Test
    public void countExpression_double_accumulator() {
        // arrange:
        final Accumulator a = new CountExpression(new ColumnExpression(0)).createAccumulator();
        final double[] values = new double[] {10.0, 14.0, 4.0};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(3, a.finalValue());
    }

    @Test
    public void avgExpression_int_accumulator() {
        // arrange:
        final Accumulator a = new AverageExpression(new ColumnExpression(0)).createAccumulator();
        final int[] values = new int[] {1, 2, 3};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(2, a.finalValue());
    }

    @Test
    public void avgExpression_long_accumulator() {
        // arrange:
        final Accumulator a = new AverageExpression(new ColumnExpression(0)).createAccumulator();
        final long[] values = new long[] {1, 2, 3};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(2L, a.finalValue());
    }

    @Test
    public void avgExpression_double_accumulator() {
        // arrange:
        final Accumulator a = new AverageExpression(new ColumnExpression(0)).createAccumulator();
        final double[] values = new double[] {1.0, 2.0, 3.0};

        // act:
        Arrays.stream(values).forEach(it -> a.accumulate(it));

        // assert:
        Assertions.assertEquals(2.0, a.finalValue());
    }
}
