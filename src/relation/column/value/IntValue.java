package relation.column.value;

import java.io.Serializable;

public class IntValue extends ColumnValue implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer value;
    
    public void setValue(int value) {
	this.value = value;
	
    }

    public Integer getValue() {
	return value;
    }
}
