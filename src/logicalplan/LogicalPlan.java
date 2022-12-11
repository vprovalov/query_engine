package logicalplan;

import datatypes.Schema;

import java.util.List;

public interface LogicalPlan {
    Schema getSchema();
    List<LogicalPlan> getChildren();
}
