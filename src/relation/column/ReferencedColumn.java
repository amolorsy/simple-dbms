package relation.column;

import java.io.Serializable;

public class ReferencedColumn implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tableName;
    private Column column;

    public ReferencedColumn(String tableName, Column column) {
	this.tableName = tableName;
	this.column = column;
    }

    public String getTableName() {
	return tableName;
    }

    public Column getColumn() {
	return column;
    }
}
