package column.value;

import column.type.ColumnType;

public class DateValue extends ColumnValue {
    private static final long serialVersionUID = 1L;
    
    private String value;
    
    public void setValue(String value) {
	this.value = value;
    }
    
    public String getValue() {
	return value;
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
