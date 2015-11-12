package relation.column.value;

import java.io.Serializable;
import java.util.List;

import predicate.CompOperand;
import relation.Tuple;
import relation.column.type.ColumnType;
import util.Message;

public class ColumnValue extends CompOperand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String tableName;
    protected String columnName;
    protected ColumnType columnType;
    
    public void setTableName(String tableName) {
	this.tableName = tableName;
    }

    public String getTableName() {
	return this.tableName;
    }
    
    public void setColumnName(String columnName) {
	this.columnName = columnName;
    }

    public String getColumnName() {
	return this.columnName;
    }

    public void setColumnType(ColumnType columnType) {
	this.columnType = columnType;
    }

    public ColumnType getColumnType() {
	return this.columnType;
    }

    public ColumnValue getColumnValue(Tuple tuple) {
	if (columnName == null)
	    return this;
	
	List<ColumnValue> columnValues = tuple.getColumnValues();
	for (ColumnValue columnValue : columnValues) {
	    if (columnName.equals(columnValue.getColumnName())) {
		if (tableName != null) {
		    if (tableName.equals(columnValue.getTableName()))
			return columnValue;
		}
		else {
		    return columnValue;
		}
	    }
	}
	
	// When the column doesn't exist
	Message.getInstance().addSchemaError(new Message.Unit(Message.WHERE_COLUMN_NOT_EXIST, null));
	return null;
    }
}
