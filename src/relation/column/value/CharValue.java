package relation.column.value;

import java.io.Serializable;

public class CharValue extends ColumnValue implements Serializable {
    private static final long serialVersionUID = 1L;

    private String value;

    public CharValue(String value) {
	this.value = value.substring(1, value.length() - 1);
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }
}
