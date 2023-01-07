package physicalplan.expression.math;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.column.DoubleColumnVector;
import datatypes.column.FloatColumnVector;
import datatypes.column.Int32ColumnVector;
import datatypes.column.Int64ColumnVector;
import physicalplan.expression.Expression;
import physicalplan.expression.binary.BinaryExpression;

public abstract class MathExpression extends BinaryExpression {
    public MathExpression(Expression leftExpr, Expression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    protected ColumnVector evaluate(ColumnVector left, ColumnVector right) {
        final FieldType type = left.getType();
        final int size = left.getSize();
        final ColumnVector result;
        switch (type) {
            case Int32Type: {
                final int[] column = new int[size];
                for (int i = 0; i < size; i++) {
                    column[i] = evaluateI((int) left.getValue(i), (int) right.getValue(i));
                }
                result = new Int32ColumnVector(column);
                break;
            }
            case Int64Type: {
                final long[] column = new long[size];
                for (int i = 0; i < size; i++) {
                    column[i] = evaluateL((long) left.getValue(i), (long) right.getValue(i));
                }
                result = new Int64ColumnVector(column);
                break;
            }
            case FloatType: {
                final float[] column = new float[size];
                for (int i = 0; i < size; i++) {
                    column[i] = evaluateF((float) left.getValue(i), (float) right.getValue(i));
                }
                result = new FloatColumnVector(column);
                break;
            }
            case DoubleType: {
                final double[] column = new double[size];
                for (int i = 0; i < size; i++) {
                    column[i] = evaluateD((double) left.getValue(i), (double) right.getValue(i));
                }
                result = new DoubleColumnVector(column);
                break;
            }
            default:
                throw new IllegalStateException("Math expression does not support type: " + type);
        }
        return result;
    }

    abstract int evaluateI(final int left, final int right);
    abstract long evaluateL(final long left, final long right);
    abstract float evaluateF(final float left, final float right);
    abstract double evaluateD(final double left, final double right);
}
