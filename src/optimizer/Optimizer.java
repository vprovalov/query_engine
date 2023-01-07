package optimizer;

import logicalplan.*;
import logicalplan.binary.BinaryExpr;
import logicalplan.literal.*;

import java.util.List;

public class Optimizer {
    public static void extractColumns(final List<LogicalExpr> expr,
                                        final LogicalPlan input,
                                        final List<String> accum) {
        for (final LogicalExpr it: expr) {
            extractColumns(it, input, accum);
        }
    }

    public static void extractColumns(final LogicalExpr expr,
                                final LogicalPlan input,
                                final List<String> accum) {
        final String name;
        if (expr instanceof ColumnIndex) {
            accum.add(input.getSchema().getField(((ColumnIndex) expr).getIndex()).getName());
        } else if (expr instanceof Column) {
            accum.add(((Column) expr).getName());
        } else if (expr instanceof BinaryExpr) {
            extractColumns(((BinaryExpr) expr).getLeft(), input, accum);
            extractColumns(((BinaryExpr) expr).getRight(), input, accum);
        } else if (expr instanceof Alias) {
            extractColumns(((Alias) expr).getExpr(), input, accum);
        } else if (expr instanceof CastExpr) {
            extractColumns(((CastExpr) expr).getExpr(), input, accum);
        } else if (expr instanceof LiteralString || expr instanceof LiteralLong
                || expr instanceof LiteralDouble || expr instanceof LiteralBoolean
                || expr instanceof LiteralDateTime || expr instanceof LiteralFloat
                || expr instanceof LiteralInt) {
            // skip
        } else {
            throw new IllegalStateException("extractColumns does not support expression: " + expr.toString());
        }
    }
}
