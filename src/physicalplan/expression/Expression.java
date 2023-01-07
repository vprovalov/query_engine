package physicalplan.expression;

import datatypes.ColumnVector;
import datatypes.RecordBatch;

public interface Expression {
    ColumnVector evaluate(final RecordBatch input);
}
