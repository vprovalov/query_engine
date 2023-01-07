package physicalplan.expression.literal;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.column.BooleanColumnVector;
import datatypes.column.DateTimeColumnVector;
import physicalplan.expression.Expression;

import java.util.Date;

public class LiteralBooleanExpression implements Expression {
    private final boolean value;

    public LiteralBooleanExpression(boolean value) {
        this.value = value;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        return new LiteralBooleanVector(input.getRowCount());
    }

    private class LiteralBooleanVector implements ColumnVector {
        private final int size;

        private LiteralBooleanVector(int size) {
            this.size = size;
        }

        @Override
        public FieldType getType() {
            return FieldType.BooleanType;
        }

        @Override
        public Object getValue(int index) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            return LiteralBooleanExpression.this.value;
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

            return new LiteralBooleanVector(bitVector.countPositives());
        }
    }
}
