package physicalplan;

import datatypes.ColumnVector;
import datatypes.RecordBatch;
import datatypes.Schema;
import datatypes.column.AnyValueColumn;
import datatypes.column.ColumnVectorBuilder;
import org.apache.hadoop.thirdparty.protobuf.MapEntry;
import physicalplan.expression.Expression;
import physicalplan.expression.aggregate.Accumulator;
import physicalplan.expression.aggregate.AggregateExpression;

import java.util.*;
import java.util.stream.Collectors;

public class HashAggregateExec implements PhysicalPlan {
    private final PhysicalPlan inputPlan;
    private final List<Expression> groupExpr;
    private final List<AggregateExpression> aggregateExpr;
    private final Schema schema;

    public HashAggregateExec(PhysicalPlan inputPlan, List<Expression> groupExpr, List<AggregateExpression> aggregateExpr, Schema schema) {
        this.inputPlan = inputPlan;
        this.groupExpr = groupExpr;
        this.aggregateExpr = aggregateExpr;
        this.schema = schema;
    }

    @Override
    public Schema getSchema() {
        return this.schema;
    }

    @Override
    public Iterator<RecordBatch> execute() {
        final HashMap<List<Object>, List<Accumulator>> map = new HashMap<>();
        final List<ColumnVectorBuilder> columnVectorBuilders = new ArrayList<>();

        final Iterator<RecordBatch> it = this.inputPlan.execute();
        while (it.hasNext()) {
            final RecordBatch batch = it.next();
            List<ColumnVector> groupKeys = groupExpr.stream().map(e -> e.evaluate(batch)).collect(Collectors.toList());
            List<ColumnVector> aggrInputValues = aggregateExpr.stream().map(e -> e.getInputExpr().evaluate(batch)).collect(Collectors.toList());

            if (columnVectorBuilders.size() == 0) {
                for (final ColumnVector col : groupKeys) {
                    columnVectorBuilders.add(new ColumnVectorBuilder(col.getType()));
                }
                for (final ColumnVector col : aggrInputValues) {
                    columnVectorBuilders.add(new ColumnVectorBuilder(col.getType()));
                }
            }

            for (int rowIndex = 0; rowIndex < batch.getRowCount(); rowIndex++) {
                final int currentIndex = rowIndex;
                final List<Object> rowKey = groupKeys.stream().map(k -> k.getValue(currentIndex)).collect(Collectors.toList());

                final List<Accumulator> accumulators;
                if (map.containsKey(rowKey)) {
                    accumulators = map.get(rowKey);
                } else {
                    accumulators = aggregateExpr.stream().map(e -> e.createAccumulator()).collect(Collectors.toList());
                    map.put(rowKey, accumulators);
                }

                for (int idx = 0; idx < accumulators.size(); idx++) {
                    final Accumulator accumulator = accumulators.get(idx);
                    final Object value = aggrInputValues.get(idx).getValue(currentIndex);
                    accumulator.accumulate(value);
                }
            }
        }

        for (final Map.Entry<List<Object>, List<Accumulator>> entry : map.entrySet()) {
            final List<Object> rowKey = entry.getKey();
            final List<Accumulator> accumulators = entry.getValue();
            int i = 0;
            for (final Object key: rowKey) {
                final ColumnVectorBuilder columnBuilder = columnVectorBuilders.get(i);
                columnBuilder.addValue(key);
                i++;
            }
            for (final Accumulator acc: accumulators) {
                final ColumnVectorBuilder columnBuilder = columnVectorBuilders.get(i);
                columnBuilder.addValue(acc.finalValue());
                i++;
            }
        }

        final RecordBatch result = new RecordBatch(
                this.schema,
                columnVectorBuilders.stream().map(builder -> builder.getColumn()).collect(Collectors.toList()));

        return new HashAggregateIterator(result);
    }

    @Override
    public List<PhysicalPlan> getChildren() {
        final ArrayList<PhysicalPlan> result = new ArrayList<>(1);
        result.add(this.inputPlan);
        return result;
    }

    @Override
    public String toString() {
        return "HashAggregateExec: groupExpr=" +
                String.join(", ", groupExpr.stream().map(it -> it.toString()).collect(Collectors.toList())) +
                " aggregateExpr=" +
                String.join(", ", aggregateExpr.stream().map(it -> it.toString()).collect(Collectors.toList()));
    }

    private class HashAggregateIterator implements Iterator<RecordBatch> {
        private final RecordBatch batch;
        private boolean hasNext;

        private HashAggregateIterator(RecordBatch batch) {
            this.batch = batch;
            this.hasNext = true;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public RecordBatch next() {
            this.hasNext = false;
            return this.batch;
        }
    }
}
