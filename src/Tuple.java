import java.util.ArrayList;
import java.util.List;

import column.value.ColumnValue;

public class Tuple {
    private List<ColumnValue> columnValues;

    public Tuple() {
	columnValues = new ArrayList<ColumnValue>();
    }

    public void addColumnValue(ColumnValue columnValue) {
	columnValues.add(columnValue);
    }
    
    public void replaceColumnValue(int index, ColumnValue columnValue) {
	columnValues.set(index, columnValue);
    }

    public List<ColumnValue> getColumnValues() {
	return columnValues;
    }
}
