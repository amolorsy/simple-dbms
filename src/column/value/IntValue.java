package column.value;

import column.type.ColumnType;

public class IntValue extends ColumnValue {
    private static final long serialVersionUID = 1L;
    
    private Integer value;
    
    public void setValue(int value) {
	this.value = value;
	
    }

    public Integer getValue() {
	return value;
    }
    
    @Override
    public void setColumnName(String columnName) {
	super.columnName = columnName;
    }

    @Override
    public String getColumnName() {
	return super.columnName;
    }

    @Override
    public void setColumnType(ColumnType columnType) {
	super.columnType = columnType;
    }

    @Override
    public ColumnType getColumnType() {
	return super.columnType;
    }
}
