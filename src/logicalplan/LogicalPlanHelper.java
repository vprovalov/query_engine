package logicalplan;

import java.util.stream.IntStream;

public class LogicalPlanHelper {
    public static String format(final LogicalPlan logicalPlan) {
        final StringBuilder sb = new StringBuilder();
        visit(sb, logicalPlan, 0);
        return sb.toString();
    }

    private static void visit(final StringBuilder sb, final LogicalPlan logicalPlan, final int indent) {
        IntStream.range(0, indent).forEach(i -> sb.append('\t'));
        sb.append(logicalPlan.toString());
        sb.append('\n');
        logicalPlan.getChildren().forEach(it -> visit(sb, it, indent + 1));
    }
}
