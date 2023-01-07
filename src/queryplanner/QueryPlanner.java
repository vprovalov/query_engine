package queryplanner;

import datatypes.Schema;
import exceptions.QueryException;
import logicalplan.*;
import logicalplan.aggregate.*;
import logicalplan.binary.*;
import logicalplan.binary.math.*;
import logicalplan.literal.*;
import physicalplan.*;
import physicalplan.expression.ColumnExpression;
import physicalplan.expression.Expression;
import physicalplan.expression.aggregate.*;
import physicalplan.expression.binary.*;
import physicalplan.expression.literal.*;
import physicalplan.expression.math.*;

import java.util.List;
import java.util.stream.Collectors;

public class QueryPlanner {
    public PhysicalPlan createPhysicalPlan(final LogicalPlan logicalPlan) {
        if (logicalPlan instanceof Scan) {
            final Scan scan = (Scan) logicalPlan;
            return new ScanExec(scan.getDataSource(), scan.getProjection());
        } else if (logicalPlan instanceof Selection) {
            final Selection selection = (Selection) logicalPlan;
            final PhysicalPlan input = createPhysicalPlan(selection.getInput());
            final Expression expr = createPhysicalExpr(selection.getExpr(), selection.getInput());
            return new SelectionExec(input, expr);
        } else if (logicalPlan instanceof Projection) {
            final Projection projection = (Projection) logicalPlan;
            final PhysicalPlan input = createPhysicalPlan(projection.getInput());
            final List<Expression> expr = projection.getExpr().stream().map(it -> createPhysicalExpr(it, projection.getInput())).collect(Collectors.toList());
            final Schema schema = new Schema(projection.getExpr().stream().map(it -> it.toField(projection.getInput())).collect(Collectors.toList()));
            return new ProjectionExec(input, schema, expr);
        } else if (logicalPlan instanceof Aggregate) {
            final Aggregate aggregate = (Aggregate) logicalPlan;
            final PhysicalPlan input = createPhysicalPlan(aggregate.getInput());
            final List<Expression> groupAggr = aggregate.getGroupExpr().stream().map(it -> createPhysicalExpr(it, aggregate.getInput())).collect(Collectors.toList());
            final List<AggregateExpression> aggregateExpr = aggregate.getAggregateExpr().stream().map(it -> {
                    if (it instanceof Max) {
                        return new MaxExpression(createPhysicalExpr(((Max) it).getExpr(), aggregate.getInput()));
                    } else if (it instanceof Min) {
                        return new MinExpression(createPhysicalExpr(((Min) it).getExpr(), aggregate.getInput()));
                    } else if (it instanceof Average) {
                        return new AverageExpression(createPhysicalExpr(((Average) it).getExpr(), aggregate.getInput()));
                    } else if (it instanceof Count) {
                        return new CountExpression(createPhysicalExpr(((Count) it).getExpr(), aggregate.getInput()));
                    } else if (it instanceof Sum) {
                        return new SumExpression(createPhysicalExpr(((Sum) it).getExpr(), aggregate.getInput()));
                    } else {
                        throw new IllegalStateException("Unsupported aggregate function " + it.toString());
                    }
                }).collect(Collectors.toList());
            return new HashAggregateExec(input, groupAggr, aggregateExpr, aggregate.getSchema());
        } else {
            throw new IllegalStateException("Unexpected logical plan: " + logicalPlan);
        }
    }

    public Expression createPhysicalExpr(final LogicalExpr expr, final LogicalPlan inputPlan) {
        if (expr instanceof LiteralLong) {
            final LiteralLong literalLong = (LiteralLong) expr;
            return new LiteralLongExpression(literalLong.getValue());
        } else if (expr instanceof LiteralInt) {
            final LiteralInt literalInt = (LiteralInt) expr;
            return new LiteralIntExpression(literalInt.getValue());
        } else if (expr instanceof LiteralFloat) {
            final LiteralFloat literalFloat = (LiteralFloat) expr;
            return new LiteralFloatExpression(literalFloat.getValue());
        } else if (expr instanceof LiteralDouble) {
            final LiteralDouble literalDouble = (LiteralDouble) expr;
            return new LiteralDoubleExpression(literalDouble.getValue());
        } else if (expr instanceof LiteralBoolean) {
            final LiteralBoolean literalBoolean = (LiteralBoolean)expr;
            return new LiteralBooleanExpression(literalBoolean.getValue());
        } else if (expr instanceof LiteralDateTime) {
            final LiteralDateTime literalDateTime = (LiteralDateTime) expr;
            return new LiteralDateTimeExpression(literalDateTime.getValue());
        } else if (expr instanceof LiteralString) {
            final LiteralString literalString = (LiteralString) expr;
            return new LiteralStringExpression(literalString.getValue());
        } else if (expr instanceof ColumnIndex) {
            final ColumnIndex columnIndex = (ColumnIndex) expr;
            return new ColumnExpression(columnIndex.getIndex());
        } else if (expr instanceof Column) {
            final Column column = (Column)expr;
            final int index = inputPlan.getSchema().findIndex(column.getName());
            if (index == -1) {
                throw new QueryException("No column named '" + column.getName() + "'");
            }
            return new ColumnExpression(index);
        } else if (expr instanceof BinaryExpr) {
            final BinaryExpr binaryExpr = (BinaryExpr) expr;
            final Expression left = createPhysicalExpr(binaryExpr.getLeft(), inputPlan);
            final Expression right = createPhysicalExpr(binaryExpr.getRight(), inputPlan);

            if (expr instanceof Eq) {
                return new EqExpression(left, right);
            } else if (expr instanceof Neq) {
                return new NeqExpression(left, right);
            } else if (expr instanceof And) {
                return new AndExpression(left, right);
            } else if (expr instanceof Gt) {
                return new GtExpression(left, right);
            } else if (expr instanceof GtEq) {
                return new GtEqExpression(left, right);
            } else if (expr instanceof Lt) {
                return new LtExpression(left, right);
            } else if (expr instanceof LtEq) {
                return new LtEqExpression(left, right);
            } else if (expr instanceof Or) {
                return new OrExpression(left, right);
            } else if (expr instanceof Add) {
                return new AddExpression(left, right);
            } else if (expr instanceof Divide) {
                return new DivideExpression(left, right);
            } else if (expr instanceof Modulus) {
                return new ModulusExpression(left, right);
            } else if (expr instanceof Multiply) {
                return new MultiplyExpression(left, right);
            } else if (expr instanceof Subtract) {
                return new SubtractExpression(left, right);
            } else {
                throw new IllegalStateException("Unsupported binary expression: " + expr.toString());
            }
        } else {
            throw new IllegalStateException("Unsupported logical expression: " + expr.toString());
        }
    }
}
