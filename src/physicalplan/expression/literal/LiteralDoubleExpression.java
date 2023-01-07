package physicalplan.expression.literal;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.column.BooleanColumnVector;
import datatypes.column.DoubleColumnVector;
import physicalplan.expression.Expression;

public class LiteralDoubleExpression implements Expression {
    private final double value;

    public LiteralDoubleExpression(double value) {
        this.value = value;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        return new LiteralDoubleVector(input.getRowCount());
    }

    private class LiteralDoubleVector implements ColumnVector {
        private final int size;

        private LiteralDoubleVector(int size) {
            this.size = size;
        }

        @Override
        public FieldType getType() {
            return FieldType.DoubleType;
        }

        @Override
        public Object getValue(int index) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            return LiteralDoubleExpression.this.value;
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

            return new LiteralDoubleVector(bitVector.countPositives());
        }
    }
}
