package relation.column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import relation.column.type.CharType;
import relation.column.type.ColumnType;
import relation.column.type.DateType;
import relation.column.type.IntType;
import relation.column.value.CharValue;
import relation.column.value.ColumnValue;
import relation.column.value.DateValue;
import relation.column.value.IntValue;

public class Column implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean canHaveNullValue;

    private String tableName;
    private String columnName;
    private ColumnType columnType;
    private List<ColumnValue> columnValues;
    
    private ReferencedColumn referencedColumn;

    private String keyType;

    public Column() {
	this.keyType = "";
	canHaveNullValue = true;
	columnValues = new ArrayList<ColumnValue>();
    }
    
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
    
    public boolean containColumnValues(ColumnValue columnValue) {
	for (ColumnValue value : columnValues) {
	    if (value.getColumnType() instanceof CharType && columnValue.getColumnType() instanceof CharType) {
		if (((CharValue) value).getValue().compareTo(((CharValue) columnValue).getValue()) == 0)
		    return true;
	    }
	    else if (value.getColumnType() instanceof IntType && columnValue.getColumnType() instanceof IntType) {
		if (((IntValue) value).getValue().compareTo(((IntValue) columnValue).getValue()) == 0)
		    return true;
	    }
	    else if (value.getColumnType() instanceof DateType && columnValue.getColumnType() instanceof DateType) {
		if (((DateValue) value).getValue().compareTo(((DateValue) columnValue).getValue()) == 0)
		    return true;
	    }
	}
	
	return false;
    }
    
    public void setReferencedColumn(ReferencedColumn referencedColumn) {
	this.referencedColumn = referencedColumn;
    }
    
    public ReferencedColumn getReferencedColumn() {
	return referencedColumn;
    }
}