package physicalplan.expression.literal;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.column.BooleanColumnVector;
import physicalplan.expression.Expression;

public class LiteralStringExpression implements Expression {
    private final String value;

    public LiteralStringExpression(String value) {
        this.value = value;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        return null;
    }

    private class LiteralStringVector implements ColumnVector {
        private final int size;

        private LiteralStringVector(int size) {
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
            return LiteralStringExpression.this.value;
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

            return new LiteralStringVector(bitVector.countPositives());
        }
    }
}
