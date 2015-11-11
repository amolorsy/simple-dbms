package predicate;

import java.util.List;

import relation.Tuple;
import relation.column.value.ColumnValue;
import util.Message;

public class CompOperand {
    private String tableName;
    private String columnName;
    
    public void setTableName(String tableName) {
	this.tableName = tableName;
    }

    public String getTableName() {
	return tableName;
    }
    
    public void setColumnName(String columnName) {
	this.columnName = columnName;
    }

    public String getColumnName() {
	return columnName;
    }
    
    public ColumnValue getColumnValue(Tuple tuple) {
	List<ColumnValue> columnValues = tuple.getColumnValues();
	    
	for (ColumnValue columnValue : columnValues) {
	    if (columnName.equals(columnValue.getColumnName()))
		return columnValue;
	}
	
	// When the column doesn't exist
	Message.getInstance().addSchemaError(new Message.Unit(Message.WHERE_COLUMN_NOT_EXIST, null));
	return null;
    }
}
