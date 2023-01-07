package physicalplan;

import datatypes.ColumnVector;
import datatypes.RecordBatch;
import datatypes.Schema;
import datatypes.column.BooleanColumnVector;
import physicalplan.expression.Expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SelectionExec implements PhysicalPlan {
    private final PhysicalPlan inputPlan;
    private final Expression expr;

    public SelectionExec(PhysicalPlan inputPlan, Expression expr) {
        this.inputPlan = inputPlan;
        this.expr = expr;
    }

    @Override
    public Schema getSchema() {
        return inputPlan.getSchema();
    }

    @Override
    public Iterator<RecordBatch> execute() {
        final Iterator<RecordBatch> it = this.inputPlan.execute();
        return new Iterator<RecordBatch>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public RecordBatch next() {
                final RecordBatch batch = it.next();
                final BooleanColumnVector bitVector = (BooleanColumnVector) SelectionExec.this.expr.evaluate(batch);
                final List<ColumnVector> fields = IntStream.range(0, batch.getColumnCount())
                        .mapToObj(idx -> batch.getField(idx).filter(bitVector))
                        .collect(Collectors.toList());

                return new RecordBatch(batch.getSchema(), fields);
            }
        };
    }

    @Override
    public List<PhysicalPlan> getChildren() {
        final List<PhysicalPlan> result = new ArrayList<>(1);
        result.add(this.inputPlan);
        return result;
    }
}
