package relation.column.type;

import java.io.Serializable;

public class CharType extends ColumnType implements Serializable {
    private static final long serialVersionUID = 1L;

    private int size;

    public CharType() {
    }

    public CharType(int size) {
	this.size = size;
    }

    @Override
    public String toString() {
	return "char(" + size + ")";
    }

    public int getSize() {
	return size;
    }
}
