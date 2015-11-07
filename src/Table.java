import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import column.Column;

public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    
    transient private RecordManager recordManager;
    transient private boolean isSetPrimaryKeyDone;

    private String tableName;
    private Map<String, Column> tableColumnDictionary;
    private List<Column> tableColumns;
    private Table referencingTable;

    private PrimaryKey primaryKey;
    private List<ForeignKey> foreignKeys;
    
    private List<Tuple> tuples;

    public Table(RecordManager recordManager) {
	this.recordManager = recordManager;
	this.isSetPrimaryKeyDone = false;

	tableColumnDictionary = new HashMap<String, Column>();
	tableColumns = new ArrayList<Column>();
	
	primaryKey = new PrimaryKey();
	foreignKeys = new ArrayList<ForeignKey>();
	
	tuples = new ArrayList<Tuple>();
    }

    public void setTableName(String tableName) {
	this.tableName = tableName;
    }
    
    public String getTableName() {
	return tableName;
    }

    public void addColumn(Column column) {
	if (tableColumnDictionary.containsKey(column.getColumnName())) {
	    // DUPLICATE_COMLUMN_DEF_ERROR
	    Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_COLUMN_DEF_ERROR, null));
	    return;
	}

	// Insert column
	tableColumnDictionary.put(column.getColumnName(), column);
	tableColumns.add(column);
    }

    public Map<String, Column> getTableColumnDictionary() {
	return tableColumnDictionary;
    }
    
    public List<Column> getTableColumns() {
	return tableColumns;
    }
    
    public Column getTableColumn(String columnName) {
	return tableColumnDictionary.get(columnName);
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
	// Check if primary key definition done
	if (isSetPrimaryKeyDone) {
	    Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_PRIMARY_KEY_DEF_ERROR, null));
	    return;
	} else {
	    /* Start primary key definition */
	    
	    for (String columnName : columnNameList) {
		if (!tableColumnDictionary.containsKey(columnName)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName));
		    return;
		}
		else if (primaryKey.getPrimaryKeyColumnDictionary().containsKey(columnName)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_COLUMN_DEF_ERROR, null));
		    return;
		}
		else {
		    Column column = tableColumnDictionary.get(columnName);

		    /* Set key type of column of primary key */
		    if (!column.canHaveNullValue()) {
			// The column of primary key must not be null
			if (column.getColumnType().equals("FOR"))
			    column.setKeyType("PRI/FOR");
			else
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
	    else if (foreignKey.getForeignKeyColumnDictionary().containsKey(columnName)) {
		Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_COLUMN_DEF_ERROR, null));
		return;
	    }

	    Column column = tableColumnDictionary.get(columnName);
	    
	    /* Set key type of column of foreign key */
	    if (column.getKeyType().equals("PRI"))
		column.setKeyType("PRI/FOR");
	    else
		column.setKeyType("FOR");
	    foreignKey.addForeignKeyColumn(column);
	}

	// Check if referenced table exists on Berkeley DB
	if (!recordManager.isTableExist(referencedTableName)) {
	    Message.getInstance().addSchemaError(new Message.Unit(Message.REFERENCE_TABLE_EXISTENCE_ERROR, null));
	    return;
	} else {
	    Table referencedTable = recordManager.getTableDictionary().get(referencedTableName);
	    Map<String, Column> referencedTableColumnDictionary = referencedTable.getTableColumnDictionary();

	    for (String referencedColumnName : referencedColumnNameList) {
		if (!referencedTableColumnDictionary.containsKey(referencedColumnName)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.REFERENCE_COLUMN_EXISTENCE_ERROR, null));
		    return;
		}
		else if (foreignKey.getReferencedColumnDictionary().containsKey(referencedColumnName)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_COLUMN_DEF_ERROR, null));
		    return;
		}

		Column referencedColumn = referencedTableColumnDictionary.get(referencedColumnName);

		for (Column column : foreignKey.getForeignKeyColumns()) {
		    if (!column.getColumnType().toString().equals(referencedColumn.getColumnType().toString())) {
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
    
    public void addTuple(Tuple tuple) {
	tuples.add(tuple);
    }
    
    public List<Tuple> getTuples() {
	return tuples;
    }
}
