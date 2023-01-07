package physicalplan.expression.literal;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.column.BooleanColumnVector;
import physicalplan.expression.Expression;

public class LiteralLongExpression implements Expression {
    private long value;

    public LiteralLongExpression(long value) {
        this.value = value;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        return new LiteralLongVector(input.getRowCount());
    }

    private class LiteralLongVector implements ColumnVector {
        private int size;

        private LiteralLongVector(int size) {
            this.size = size;
        }

        @Override
        public FieldType getType() {
            return FieldType.Int64Type;
        }

        @Override
        public Object getValue(int index) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            return LiteralLongExpression.this.value;
        }

        @Override
        public int getSize() {
            return this.size;
        }

        @Override
        public ColumnVector filter(final BooleanColumnVector bitVector) {
            if (bitVector.getSize() != this.getSize()) {
                throw new IllegalStateException("Bit vector size doesn't match column size");
            }

            return new LiteralLongVector(bitVector.countPositives());
        }
    }
}
