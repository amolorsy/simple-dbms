import java.io.Serializable;

public class Column implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean canHaveNullValue;
    
    private String columnName;
    private ColumnType columnType;
    
    public Column() { }
    
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
    
    public void guaranteeNotNull() {
	canHaveNullValue = false;
    }
    
    public boolean canHaveNullValue() {
  	return canHaveNullValue;
    }
}