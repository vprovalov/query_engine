package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ColumnVectorBuilder {
    private final FieldType type;
    private final List<Object> values;

    public ColumnVectorBuilder(final FieldType type) {
        this.type = type;
        this.values = new ArrayList<>();
    }

    public void addValue(final Object value) {
        this.values.add(value);
    }

    public ColumnVector getColumn() {
        switch (this.type) {
            case BooleanType: {
                final boolean[] column = new boolean[values.size()];
                for (int i = 0; i < this.values.size(); i++) {
                    column[i] = (boolean) this.values.get(i);
                }
                return new BooleanColumnVector(column);
            }
            case Int32Type: {
                final int[] column = new int[values.size()];
                for (int i = 0; i < this.values.size(); i++) {
                    column[i] = (int) this.values.get(i);
                }
                return new Int32ColumnVector(column);
            }
            case Int64Type: {
                final long[] column = new long[values.size()];
                for (int i = 0; i < this.values.size(); i++) {
                    column[i] = (long) this.values.get(i);
                }
                return new Int64ColumnVector(column);
            }
            case FloatType: {
                final float[] column = new float[values.size()];
                for (int i = 0; i < this.values.size(); i++) {
                    column[i] = (float) this.values.get(i);
                }
                return new FloatColumnVector(column);
            }
            case DoubleType:{
                final double[] column = new double[values.size()];
                for (int i = 0; i < this.values.size(); i++) {
                    column[i] = (double) this.values.get(i);
                }
                return new DoubleColumnVector(column);
            }
            case StringType: {
                final String[] column = new String[values.size()];
                for (int i = 0; i < this.values.size(); i++) {
                    column[i] = (String) this.values.get(i);
                }
                return new StringColumnVector(column);
            }
            case DateTime: {
                final Date[] column = new Date[values.size()];
                for (int i = 0; i < this.values.size(); i++) {
                    column[i] = (Date) this.values.get(i);
                }
                return new DateTimeColumnVector(column);
            }
            default:
                throw new IllegalStateException("Unexpected column type: " + this.type);
        }
    }
}
