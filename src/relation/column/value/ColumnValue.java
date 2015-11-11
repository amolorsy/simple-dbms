package relation.column.value;

import java.io.Serializable;

import predicate.CompOperand;
import relation.column.type.ColumnType;

public abstract class ColumnValue extends CompOperand implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String tableName;
    protected String columnName;
    protected ColumnType columnType;
    
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

    public void setColumnType(ColumnType columnType) {
	this.columnType = columnType;
    }

    public ColumnType getColumnType() {
	return columnType;
    }
}
