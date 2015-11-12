package relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import predicate.WhereClause;
import relation.column.Column;
import relation.column.ReferencedColumn;
import relation.column.type.CharType;
import relation.column.value.ColumnValue;
import util.Message;

public class Table implements Serializable {
    private static final long serialVersionUID = 1L;

    transient private boolean isSetPrimaryKeyDone;
    transient private List<String> updateTables;

    private String tableName;
    private Map<String, Column> tableColumnDictionary;
    private List<Column> tableColumns;
    private Table referencingTable;

    private PrimaryKey primaryKey;
    private List<ForeignKey> foreignKeys;
    private List<ForeignKey> referencingForeignKeys;

    private List<Tuple> tuples;

    public Table() {
	this.isSetPrimaryKeyDone = false;

	tableColumnDictionary = new HashMap<String, Column>();
	tableColumns = new ArrayList<Column>();
	primaryKey = new PrimaryKey();
	foreignKeys = new ArrayList<ForeignKey>();
	referencingForeignKeys = new ArrayList<ForeignKey>();
	tuples = new ArrayList<Tuple>();

	updateTables = new ArrayList<String>();
    }

    public void setTableName(String tableName) {
	this.tableName = tableName;
    }

    public String getTableName() {
	return tableName;
    }

    public void addColumn(Column column) {
	if (tableColumnDictionary.containsKey(column.getColumnName())) {
	    if (tableColumnDictionary.get(column.getColumnName()).getTableName().equals(column.getTableName())) {
		// DUPLICATE_COMLUMN_DEF_ERROR
		Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_COLUMN_DEF_ERROR, null));
		return;
	    }
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

    public void addReferencingForeignKey(ForeignKey foreignKey) {
	referencingForeignKeys.add(foreignKey);
    }

    public List<ForeignKey> getReferencingForeignKey() {
	return referencingForeignKeys;
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
		    Message.getInstance()
		    .addSchemaError(new Message.Unit(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName));
		    return;
		} else if (primaryKey.getPrimaryKeyColumnDictionary().containsKey(columnName)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_COLUMN_DEF_ERROR, null));
		    return;
		} else {
		    Column column = tableColumnDictionary.get(columnName);

		    /* Set key type of column of primary key */
		    column.guaranteeNotNull(); // The column of primary key must
		    // not be null
		    if (column.getKeyType().equals("FOR"))
			column.setKeyType("PRI/FOR");
		    else
			column.setKeyType("PRI");
		    primaryKey.addPrimaryKeyColumn(column);
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
		Message.getInstance()
		.addSchemaError(new Message.Unit(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName));
		return;
	    } else if (foreignKey.getForeignKeyColumnDictionary().containsKey(columnName)) {
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
	if (!RecordManager.getInstance().isTableExist(referencedTableName)) {
	    Message.getInstance().addSchemaError(new Message.Unit(Message.REFERENCE_TABLE_EXISTENCE_ERROR, null));
	    return;
	} else {
	    Table referencedTable = RecordManager.getInstance().getTableDictionary().get(referencedTableName);
	    Map<String, Column> referencedTableColumnDictionary = referencedTable.getTableColumnDictionary();

	    if (referencedColumnNameList.size() != referencedTable.getPrimaryKey().getPrimaryKeyColumns().size()) {
		Message.getInstance().addSchemaError(
			new Message.Unit(Message.REFERENCE_COLUMNS_PARTIAL_PRIMARY_KEY_COLUMNS_ERROR, null));
		return;
	    }

	    for (int i = 0; i < referencedColumnNameList.size(); i++) {
		String referencedColumnName = referencedColumnNameList.get(i);
		Column referencedColumn = referencedTableColumnDictionary.get(referencedColumnName);
		Column foreignKeyColumn = foreignKey.getForeignKeyColumns().get(i);

		if (!referencedTableColumnDictionary.containsKey(referencedColumnName)) {
		    Message.getInstance()
		    .addSchemaError(new Message.Unit(Message.REFERENCE_COLUMN_EXISTENCE_ERROR, null));
		    return;
		} else if (foreignKey.getReferencedColumnDictionary().containsKey(referencedColumnName)) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.DUPLICATE_COLUMN_DEF_ERROR, null));
		    return;
		} else if (!foreignKeyColumn.getColumnType().toString()
			.equals(referencedColumn.getColumnType().toString())) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.REFERENCE_TYPE_ERROR, null));
		    return;
		} else if (!referencedTable.getPrimaryKey().getPrimaryKeyColumns().contains(referencedColumn)) {
		    Message.getInstance()
		    .addSchemaError(new Message.Unit(Message.REFERENCE_NON_PRIMARY_KEY_ERROR, null));
		    return;
		}

		foreignKey.addReferencedColumn(referencedColumn);
		foreignKeyColumn.setReferencedColumn(new ReferencedColumn(referencedTableName, referencedColumn));
	    }

	    foreignKey.setReferencedTable(referencedTable);
	    foreignKey.setForeignKeyingTable(this);
	    referencedTable.setReferencingTable(this);

	    referencedTable.addReferencingForeignKey(foreignKey);
	    updateTables.add(referencedTable.getTableName());
	}

	foreignKeys.add(foreignKey);
    }

    public List<Tuple> getTuples() {
	return tuples;
    }

    public void addTuple(ArrayList<String> columnNameList, ArrayList<ColumnValue> columnValueList) {
	Tuple tuple = new Tuple(tableName);
	List<Column> pickedColumns;

	if (columnNameList == null)
	    pickedColumns = tableColumns;
	else {
	    pickedColumns = new ArrayList<Column>();
	    for (String columnName : columnNameList)
		pickedColumns.add(tableColumnDictionary.get(columnName));
	}

	if (pickedColumns.size() != columnValueList.size()) {
	    Message.getInstance()
	    .addSchemaError(new Message.Unit(Message.INSERT_COLUMN_VALUE_CORRESPONDENCE_ERROR, null));
	    return;
	}

	for (int i = 0; i < columnValueList.size(); i++) {
	    Column pickedColumn = pickedColumns.get(i);
	    ColumnValue columnValue = columnValueList.get(i);

	    if (columnValue == null) {
		if (pickedColumn.isPrimaryKeyColumn()) {
		    Message.getInstance()
		    .addSchemaError(new Message.Unit(Message.INSERT_DUPLICATE_PRIMARY_KEY_ERROR, null));
		    return;
		} else if (!pickedColumn.canHaveNullValue()) {
		    Message.getInstance().addSchemaError(
			    new Message.Unit(Message.INSERT_COLUMN_NON_NULLABLE_ERROR, pickedColumn.getColumnName()));
		    return;
		}
	    } else {
		if (pickedColumn.isForeignKeyColumn()) {
		    Table referencedTable = RecordManager.getInstance()
			    .load(pickedColumn.getReferencedColumn().getTableName());
		    String referencedColumnName = pickedColumn.getReferencedColumn().getColumn().getColumnName();

		    if (!referencedTable.getTableColumn(referencedColumnName).containColumnValues(columnValue)) {
			Message.getInstance()
			.addSchemaError(new Message.Unit(Message.INSERT_REFERENTIAL_INTEGRITY_ERROR, null));
			return;
		    }
		} else if (!columnValue.getColumnType().toString().equals(pickedColumn.getColumnType().toString())) {
		    if (!(columnValue.getColumnType() instanceof CharType)
			    || !(pickedColumn.getColumnType() instanceof CharType)) {
			Message.getInstance()
			.addSchemaError(new Message.Unit(Message.INSERT_TYPE_MISMATCH_ERROR, null));
			return;
		    }
		}
	    }

	    columnValue.setTableName(getTableName());
	    columnValue.setColumnName(pickedColumn.getColumnName());
	    
	    pickedColumn.addColumnValue(columnValue);
	    tuple.addColumnValue(columnValue);
	}

	tuples.add(tuple);
    }

    public void setTuples(List<Tuple> tuples) {
	this.tuples = tuples;
    }

    public int deleteTuples(WhereClause whereClause) {
	updateTables = new ArrayList<String>();

	if (whereClause == null) {
	    int count = tuples.size();
	    tuples.clear();

	    List<ForeignKey> referencingForeignKeys = getReferencingForeignKey();
	    for (ForeignKey referencingForeignKey : referencingForeignKeys) {
		List<Column> foreignKeyColumns = referencingForeignKey.getForeignKeyColumns();

		boolean allCanHaveNullValue = true;
		for (Column foreignKeyColumn : foreignKeyColumns) {
		    if (!foreignKeyColumn.canHaveNullValue())
			allCanHaveNullValue = false;
		}

		if (allCanHaveNullValue) {
		    Table keyingTable = RecordManager.getInstance().getTableDictionary()
			    .get(referencingForeignKey.getForeignKeyingTable().getTableName());
		    updateTables.add(keyingTable.getTableName());

		    for (Tuple tuple : keyingTable.getTuples()) {
			int index = 0;
			for (ColumnValue columnValue : tuple.getColumnValues()) {
			    for (Column foreignKeyColumn : foreignKeyColumns) {
				if (foreignKeyColumn.getColumnName().equals(columnValue.getColumnName())) {
				    index = tuple.getColumnValues().indexOf(columnValue);
				}
			    }
			}
			tuple.getColumnValues().set(index, null);
		    }
		} else {
		    Message.getInstance().addSchemaError(
			    new Message.Unit(Message.DELETE_REFERENTIAL_INTEGRITY_PASSED, String.valueOf(count)));
		    return -1;
		}
	    }
	    return count;
	} else {
	    int count = 0;
	    List<Tuple> removeTuples = new ArrayList<Tuple>();
	    
	    for (Tuple tuple : tuples) {
		if (whereClause.compute(tuple)) {
		    removeTuples.add(tuple);
		    count++;
		}
	    }
	    for (Tuple tuple : removeTuples)
		tuples.remove(tuple);

	    return count;
	}
    }

    public Table cartesianProduct(Table rightTable) {
	if (rightTable == null)
	    return this;

	Table table = new Table();
	table.setTableName(tableName + " X " + rightTable.getTableName());
	List<Column> leftColumns = tableColumns;
	List<Column> rightColumns = rightTable.getTableColumns();

	for (Column column : leftColumns) {
	    table.addColumn(column);
	}
	for (Column column : rightColumns) {
	    table.addColumn(column);
	}

	List<Tuple> leftTuples = tuples;
	List<Tuple> rightTuples = rightTable.getTuples();
	List<Tuple> newTuples = new ArrayList<Tuple>();

	for (Tuple leftTuple : leftTuples) {
	    List<ColumnValue> leftValues = leftTuple.getColumnValues();

	    for (Tuple rightTuple : rightTuples) {
		List<ColumnValue> rightValues = rightTuple.getColumnValues();
		Tuple tuple = new Tuple(table.getTableName());

		for (ColumnValue columnValue : leftValues)
		    tuple.addColumnValue(columnValue);
		for (ColumnValue columnValue : rightValues)
		    tuple.addColumnValue(columnValue);

		newTuples.add(tuple);
	    }
	}

	table.setTuples(newTuples);
	return table;
    }

    public List<String> getUpdateTables() {
	return updateTables;
    }
}