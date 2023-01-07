package physicalplan;

import datatypes.RecordBatch;
import datatypes.Schema;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public interface PhysicalPlan {
    Schema getSchema();
    Iterator<RecordBatch> execute();
    List<PhysicalPlan> getChildren();

    default String format() {
        final StringBuilder sb = new StringBuilder();
        visit(sb, this, 0);
        return sb.toString();
    }

    private static void visit(final StringBuilder sb, final PhysicalPlan physicalPlan, final int indent) {
        IntStream.range(0, indent).forEach(i -> sb.append('\t'));
        sb.append(physicalPlan.toString());
        sb.append('\n');
        physicalPlan.getChildren().forEach(it -> visit(sb, it, indent + 1));
    }
}
