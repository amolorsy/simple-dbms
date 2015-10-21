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
    private List<Column> tableColumns;
    private Table referencingTable;

    private PrimaryKey primaryKey;
    private List<ForeignKey> foreignKeys;

    public Table(Record record) {
	this.record = record;
	this.isSetPrimaryKeyDone = false;

	tableColumnDictionary = new HashMap<String, Column>();
	tableColumns = new ArrayList<Column>();
	
	primaryKey = new PrimaryKey();
	foreignKeys = new ArrayList<ForeignKey>();
    }

    public void setTableName(String tableName) {
	this.tableName = tableName;
    }
    
    public String getTableName() {
	return tableName;
    }

    public void addColumn(Column column) {
	if (tableColumnDictionary.containsKey(column.getColumnName())) {
	    Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_COLUMN_DEF_ERROR, null));
	    return;
	}

	tableColumnDictionary.put(column.getColumnName(), column);
	tableColumns.add(column);
    }

    public Map<String, Column> getTableColumnDictionary() {
	return tableColumnDictionary;
    }
    
    public List<Column> getTableColumns() {
	return tableColumns;
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

    public void setPrimaryKey(ArrayList<String> columnNameList) {
	if (isSetPrimaryKeyDone) {
	    Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_PRIMARY_KEY_DEF_ERROR, null));
	    return;
	} else {
	    for (String columnName : columnNameList) {
		if (!tableColumnDictionary.containsKey(columnName)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName));
		    return;
		} else {
		    Column column = tableColumnDictionary.get(columnName);

		    if (!column.canHaveNullValue()) {
			column.setKeyType("PRI");
			primaryKey.addPrimaryKeyColumn(column);
		    }
		}
	    }

	    primaryKey.setPrimaryKeyingTable(this);
	    isSetPrimaryKeyDone = true;
	}
    }

    public void setForeignKey(ArrayList<String> columnNameList, String referencedTableName,
	    ArrayList<String> referencedColumnNameList) {
	ForeignKey foreignKey = new ForeignKey();

	for (String columnName : columnNameList) {
	    if (!tableColumnDictionary.containsKey(columnName)) {
		Message.getInstance().addSchemaError(new Message.Unit(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName));
		return;
	    }

	    Column column = tableColumnDictionary.get(columnName);
	    column.setKeyType("FOR");
	    foreignKey.addForeignKeyColumn(column);
	}

	if (!record.isTableExist(referencedTableName)) {
	    Message.getInstance().addSchemaError(new Message.Unit(Message.REFERENCE_TABLE_EXISTENCE_ERROR, null));
	    return;
	} else {
	    Table referencedTable = record.getTableDictionary().get(referencedTableName);
	    Map<String, Column> referencedTableColumnDictionary = referencedTable.getTableColumnDictionary();

	    for (String referencedColumnName : referencedColumnNameList) {
		if (!referencedTableColumnDictionary.containsKey(referencedColumnName)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.REFERENCE_COLUMN_EXISTENCE_ERROR, null));
		    return;
		}

		Column referencedColumn = referencedTableColumnDictionary.get(referencedColumnName);

		for (Column column : foreignKey.getForeignKeyColumns()) {
		    if (column.getColumnType() != referencedColumn.getColumnType()) {
			Message.getInstance().addSchemaError(new Message.Unit(Message.REFERENCE_TYPE_ERROR, null));
			return;
		    }
		}

		if (!referencedTable.getPrimaryKey().getPrimaryKeyColumns().contains(referencedColumn)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.REFERENCE_NON_PRIMARY_KEY_ERROR, null));
		    return;
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
