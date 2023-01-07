package physicalplan.expression.literal;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.column.BooleanColumnVector;
import physicalplan.expression.Expression;

public class LiteralIntExpression implements Expression {
    private final int value;

    public LiteralIntExpression(int value) {
        this.value = value;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        return new LiteralIntVector(input.getRowCount());
    }

    private class LiteralIntVector implements ColumnVector {
        private int size;

        private LiteralIntVector(int size) {
            this.size = size;
        }

        @Override
        public FieldType getType() {
            return FieldType.Int32Type;
        }

        @Override
        public Object getValue(int index) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            return LiteralIntExpression.this.value;
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

            return new LiteralIntVector(bitVector.countPositives());
        }
    }
}
