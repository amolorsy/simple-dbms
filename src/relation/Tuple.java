package relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import relation.column.value.ColumnValue;

public class Tuple implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tableName;
    private List<ColumnValue> columnValues;
    private Map<String, ColumnValue> columnValueDictionary;

    public Tuple(String tableName) {
        this.tableName = tableName;
        columnValues = new ArrayList<ColumnValue>();
        columnValueDictionary = new HashMap<String, ColumnValue>();
    }

    public String getTableName() {
        return tableName;
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
