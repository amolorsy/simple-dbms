package column;

import java.io.Serializable;

import column.type.ColumnType;

public class Column implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean canHaveNullValue;

    private String columnName;
    private ColumnType columnType;

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
}