import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import column.value.ColumnValue;

public class Tuple {
    private List<ColumnValue> columnValues;
    private Map<String, ColumnValue> columnValueDictionary;

    public Tuple() {
	columnValues = new ArrayList<ColumnValue>();
	columnValueDictionary = new HashMap<String, ColumnValue>();
    }

    public void addColumnValue(ColumnValue columnValue) {
	columnValues.add(columnValue);
	columnValueDictionary.put(columnValue.getColumnName(), columnValue);
    }
    
    public void replaceColumnValue(int index, ColumnValue columnValue) {
	String columnName = columnValues.get(index).getColumnName();
	
	columnValues.set(index, columnValue);
	columnValueDictionary.put(columnName, columnValue);
    }

    public List<ColumnValue> getColumnValues() {
	return columnValues;
    }
    
    public Map<String, ColumnValue> getColumnValueDictionary() {
	return columnValueDictionary;
    }
}
