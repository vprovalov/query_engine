package physicalplan;

import datatypes.ColumnVector;
import datatypes.RecordBatch;
import datatypes.Schema;
import physicalplan.expression.Expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectionExec implements PhysicalPlan {
    private final PhysicalPlan inputPlan;
    private final Schema schema;
    private final List<Expression> expr;

    public ProjectionExec(PhysicalPlan inputPlan, Schema schema, List<Expression> expr) {
        this.inputPlan = inputPlan;
        this.schema = schema;
        this.expr = expr;
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public Iterator<RecordBatch> execute() {
        final Iterator<RecordBatch> it = this.inputPlan.execute();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public RecordBatch next() {
                final RecordBatch batch = it.next();
                final List<ColumnVector> columns =
                        ProjectionExec.this.expr.stream()
                                .map(it -> it.evaluate(batch))
                                .collect(Collectors.toList());

                return new RecordBatch(ProjectionExec.this.schema, columns);
            }
        };
    }

    @Override
    public List<PhysicalPlan> getChildren() {
        List<PhysicalPlan> result = new ArrayList<>(1);
        result.add(this.inputPlan);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectionExec: input=" + inputPlan.toString() + " expr=" +
                String.join(", ", this.expr.stream().map(it -> it.toString()).collect(Collectors.toList()));
    }
}
