package predicate;

import relation.Tuple;
import relation.column.value.ColumnValue;

public abstract class CompOperand {
    public abstract ColumnValue getColumnValue(Tuple tuple);
}
