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

    public void setForeignKey(ArrayList<String> columnNameList, String referencingTableName,
	    ArrayList<String> referencingColumnNameList) throws ParseException {
	ForeignKey foreignKey = new ForeignKey();

	for (String columnName : columnNameList) {
	    if (!tableColumnDictionary.containsKey(columnName)) {
		Message.print(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName);
		throw new ParseException();
	    }

	    Column column = tableColumnDictionary.get(columnName);
	    foreignKey.addForeignKeyColumn(column);
	}

	if (!record.isTableExist(referencingTableName)) {
	    Message.print(Message.REFERENCE_TABLE_EXISTENCE_ERROR, null);
	    throw new ParseException();
	} else {
	    Table referencingTable = record.getTableDictionary().get(referencingTableName);
	    Map<String, Column> referencingTableColumnDictionary = referencingTable.getTableColumnDictionary();

	    for (String referencingColumnName : referencingColumnNameList) {
		if (!referencingTableColumnDictionary.containsKey(referencingColumnName)) {
		    Message.print(Message.REFERENCE_COLUMN_EXISTENCE_ERROR, null);
		    throw new ParseException();
		}

		Column referencingColumn = referencingTableColumnDictionary.get(referencingColumnName);

		for (Column column : foreignKey.getForeignKeyColumns()) {
		    if (column.getColumnType() != referencingColumn.getColumnType()) {
			Message.print(Message.REFERENCE_TYPE_ERROR, null);
			throw new ParseException();
		    }
		}

		if (!referencingTable.getPrimaryKey().getPrimaryKeyColumns().contains(referencingColumn)) {
		    Message.print(Message.REFERENCE_NON_PRIMARY_KEY_ERROR, null);
		    throw new ParseException();
		}

		foreignKey.addReferencingColumn(referencingColumn);
	    }

	    foreignKey.setForeignKeyingTable(this);
	    foreignKey.setReferencingTable(referencingTable);
	}

	foreignKeys.add(foreignKey);
    }

}
