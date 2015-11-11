package relation.column;

import java.io.Serializable;
import java.util.List;

import relation.column.type.ColumnType;
import relation.column.value.ColumnValue;

public class Column implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean canHaveNullValue;

    private String columnName;
    private ColumnType columnType;
    
    private List<ColumnValue> columnValues;

    private String keyType;

    public Column() {
	this.keyType = "";
	canHaveNullValue = true;
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

    public void setKeyType(String keyType) {
	this.keyType = keyType;
    }

    public String getKeyType() {
	return keyType;
    }

    public void guaranteeNotNull() {
	canHaveNullValue = false;
    }

    public boolean canHaveNullValue() {
	return canHaveNullValue;
    }
    
    public boolean isPrimaryKeyColumn() {
	return keyType.contains("PRI");
    }
    
    public boolean isForeignKeyColumn() {
	return keyType.contains("FOR");
    }
    
    public void addColumnValue(ColumnValue columnValue) {
	columnValues.add(columnValue);
    }
    
    public List<ColumnValue> getColumnValues() {
	return columnValues;
    }
}