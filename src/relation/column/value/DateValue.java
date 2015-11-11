package relation.column.value;

public class DateValue extends ColumnValue {
    private static final long serialVersionUID = 1L;
    
    private String value;
    
    public void setValue(String value) {
	this.value = value;
    }
    
    public String getValue() {
	return value;
    }
}
