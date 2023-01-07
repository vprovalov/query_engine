package physicalplan.expression.literal;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.column.BooleanColumnVector;
import physicalplan.expression.Expression;

public class LiteralFloatExpression implements Expression {
    private float value;

    public LiteralFloatExpression(float value) {
        this.value = value;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        return new LiteralFloatVector(input.getRowCount());
    }

    private class LiteralFloatVector implements ColumnVector {
        private int size;

        private LiteralFloatVector(int size) {
            this.size = size;
        }

        @Override
        public FieldType getType() {
            return FieldType.StringType;
        }

        @Override
        public Object getValue(int index) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            return LiteralFloatExpression.this.value;
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

            return new LiteralFloatVector(bitVector.countPositives());
        }
    }
}
