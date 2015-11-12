package relation.column.type;

import java.io.Serializable;

public class DateType extends ColumnType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "date";
    }
}
