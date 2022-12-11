package datatypes.column;

import datatypes.ColumnVector;
import datatypes.FieldType;

import java.util.Arrays;
import java.util.Date;

public class AnyValueColumn {
    private final FieldType type;
    private Object array;
    private int index;
    private final int size;

    public AnyValueColumn(final FieldType type, final int maxSize) {
        switch (type) {
            case StringType:
                this.array = new String[maxSize];
                break;
            case Int32Type:
                this.array = new int[maxSize];
                break;
            case Int64Type:
                this.array = new long[maxSize];
                break;
            case BooleanType:
                this.array = new boolean[maxSize];
                break;
            case FloatType:
                this.array = new float[maxSize];
                break;
            case DoubleType:
                this.array = new double[maxSize];
                break;
            case DateTime:
                this.array = new Date[maxSize];
                break;
            default:
                throw new IllegalArgumentException("unsupported field type");
        }
        this.type = type;
        this.size = maxSize;
    }

    public FieldType getType() {
        return this.type;
    }

    public void add(final Object value) {
        if (this.index >= size) throw new IllegalStateException("can't add more items to column");

        switch (type) {
            case StringType:
                ((String[]) array)[index] = (String) value;
                break;
            case Int32Type:
                ((int[]) array)[index] = (int) value;
                break;
            case Int64Type:
                ((long[]) array)[index] = (long) value;
                break;
            case BooleanType:
                ((boolean[]) array)[index] = (boolean) value;
                break;
            case FloatType:
                ((float[]) array)[index] = (float) value;
                break;
            case DoubleType:
                ((double[]) array)[index] = (double) value;
                break;
            case DateTime:
                ((Date[]) array)[index] = (Date) value;
                break;
            default:
                throw new IllegalArgumentException("unsupported field type");
        }
        this.index++;
    }

    public ColumnVector createColumn() {
        if (this.index < this.size) {
            shrinkArray();
        }

        switch (type) {
            case StringType:
                return new StringColumnVector((String[]) array);
            case Int32Type:
                return new Int32ColumnVector((int[]) array);
            case Int64Type:
                return new Int64ColumnVector((long[]) array);
            case BooleanType:
                return new BooleanColumnVector((boolean[]) array);
            case FloatType:
                return new FloatColumnVector((float[]) array);
            case DoubleType:
                return new DoubleColumnVector((double[]) array);
            case DateTime:
                return new DateTimeColumnVector((Date[]) array);
            default:
                throw new IllegalArgumentException("unsupported field type");
        }
    }

    private void shrinkArray() {
        switch (type) {
            case StringType:
                this.array = Arrays.copyOf((String[]) array, index);
                break;
            case Int32Type:
                this.array = Arrays.copyOf((int[]) array, index);
                break;
            case Int64Type:
                this.array = Arrays.copyOf((long[]) array, index);
                break;
            case BooleanType:
                this.array = Arrays.copyOf((boolean[]) array, index);
                break;
            case FloatType:
                this.array = Arrays.copyOf((float[]) array, index);
                break;
            case DoubleType:
                this.array = Arrays.copyOf((double[]) array, index);
                break;
            case DateTime:
                this.array = Arrays.copyOf((Date[]) array, index);
                break;
            default:
                throw new IllegalArgumentException("unsupported field type");
        }
    }
}
