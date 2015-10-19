import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    
    transient private Record record;
    transient private boolean isSetPrimaryKeyDone;

    private String tableName;
    private Map<String, Column> tableColumnDictionary;
    private Table referencingTable;

    private PrimaryKey primaryKey;
    private List<ForeignKey> foreignKeys;

    public Table(Record record) {
	this.record = record;

	this.isSetPrimaryKeyDone = false;

	tableColumnDictionary = new HashMap<String, Column>();
	primaryKey = new PrimaryKey();
	foreignKeys = new ArrayList<ForeignKey>();
    }

    public void setTableName(String tableName) {
	this.tableName = tableName;
    }
    
    public String getTableName() {
	return tableName;
    }

    public void addColumn(Column column) throws ParseException {
	if (tableColumnDictionary.containsKey(column.getColumnName())) {
	    Message.print(Message.DUPLICATE_COLUMN_DEF_ERROR, null);
	    throw new ParseException();
	}

	tableColumnDictionary.put(column.getColumnName(), column);
    }

    public Map<String, Column> getTableColumnDictionary() {
	return tableColumnDictionary;
    }
    
    public void setReferencingTable(Table referencingTable) {
	this.referencingTable = referencingTable;
    }
    
    public Table getReferencingTable() {
	return referencingTable;
    }

    public PrimaryKey getPrimaryKey() {
	return primaryKey;
    }

    public List<ForeignKey> getForeignKeys() {
	return foreignKeys;
    }

    public void setPrimaryKey(ArrayList<String> columnNameList) throws ParseException {
	if (isSetPrimaryKeyDone) {
	    Message.print(Message.DUPLICATE_PRIMARY_KEY_DEF_ERROR, null);
	    throw new ParseException();
	} else {
	    for (String columnName : columnNameList) {
		if (!tableColumnDictionary.containsKey(columnName)) {
		    Message.print(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName);
		    throw new ParseException();
		} else {
		    Column column = tableColumnDictionary.get(columnName);

		    if (!column.canHaveNullValue()) {
			primaryKey.addPrimaryKeyColumns(column);
		    }
		}
	    }

	    primaryKey.setPrimaryKeyingTable(this);
	    isSetPrimaryKeyDone = true;
	}
    }

    public void setForeignKey(ArrayList<String> columnNameList, String referencedTableName,
	    ArrayList<String> referencedColumnNameList) throws ParseException {
	ForeignKey foreignKey = new ForeignKey();

	for (String columnName : columnNameList) {
	    if (!tableColumnDictionary.containsKey(columnName)) {
		Message.print(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName);
		throw new ParseException();
	    }

	    Column column = tableColumnDictionary.get(columnName);
	    foreignKey.addForeignKeyColumn(column);
	}

	if (!record.isTableExist(referencedTableName)) {
	    Message.print(Message.REFERENCE_TABLE_EXISTENCE_ERROR, null);
	    throw new ParseException();
	} else {
	    Table referencedTable = record.getTableDictionary().get(referencedTableName);
	    Map<String, Column> referencedTableColumnDictionary = referencedTable.getTableColumnDictionary();

	    for (String referencedColumnName : referencedColumnNameList) {
		if (!referencedTableColumnDictionary.containsKey(referencedColumnName)) {
		    Message.print(Message.REFERENCE_COLUMN_EXISTENCE_ERROR, null);
		    throw new ParseException();
		}

		Column referencedColumn = referencedTableColumnDictionary.get(referencedColumnName);

		for (Column column : foreignKey.getForeignKeyColumns()) {
		    if (column.getColumnType() != referencedColumn.getColumnType()) {
			Message.print(Message.REFERENCE_TYPE_ERROR, null);
			throw new ParseException();
		    }
		}

		if (!referencedTable.getPrimaryKey().getPrimaryKeyColumns().contains(referencedColumn)) {
		    Message.print(Message.REFERENCE_NON_PRIMARY_KEY_ERROR, null);
		    throw new ParseException();
		}

		foreignKey.addReferencedColumn(referencedColumn);
	    }

	    foreignKey.setForeignKeyingTable(this);
	    foreignKey.setReferencedTable(referencedTable);
	    referencedTable.setReferencingTable(this);
	}

	foreignKeys.add(foreignKey);
    }
}
