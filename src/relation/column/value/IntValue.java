package relation.column.value;

public class IntValue extends ColumnValue {
    private static final long serialVersionUID = 1L;
    
    private Integer value;
    
    public void setValue(int value) {
	this.value = value;
	
    }

    public Integer getValue() {
	return value;
    }
}
