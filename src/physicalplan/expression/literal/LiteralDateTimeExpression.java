package physicalplan.expression.literal;

import datatypes.ColumnVector;
import datatypes.FieldType;
import datatypes.RecordBatch;
import datatypes.column.BooleanColumnVector;
import datatypes.column.DateTimeColumnVector;
import physicalplan.expression.Expression;

import java.util.Date;

public class LiteralDateTimeExpression implements Expression {
    private final Date value;

    public LiteralDateTimeExpression(Date value) {
        this.value = value;
    }

    @Override
    public ColumnVector evaluate(RecordBatch input) {
        return new LiteralDateTimeVector(input.getRowCount());
    }

    private class LiteralDateTimeVector implements ColumnVector {
        private int size;

        private LiteralDateTimeVector(int size) {
            this.size = size;
        }

        @Override
        public FieldType getType() {
            return FieldType.DateTime;
        }

        @Override
        public Object getValue(int index) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            return LiteralDateTimeExpression.this.value;
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

            return new LiteralDateTimeVector(bitVector.countPositives());
        }
    }
}
