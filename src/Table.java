import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table {
    private Record record;
    
    private boolean isSetPrimaryKeyDone;
    
    private String tableName;
    private List<Column> primaryKeyColumns;
    private List<Column> foreignKeyColumns;
    
    public Table(Record record) {
	this.record = record;
	
	primaryKeyColumns = new ArrayList<Column>();
	foreignKeyColumns = new ArrayList<Column>();
    }
    
    public String getTableName() {
	return tableName;
    }
    
    public void setPrimaryKey(ArrayList<String> columnNameList) throws ParseException {
	if (isSetPrimaryKeyDone) {
	    Message.print(Message.DUPLICATE_PRIMARY_KEY_DEF_ERROR, null);
	    throw new ParseException();
	}
	else {
	    Map<String, Column> columnDictionary = record.getColumnDictionary();
	    
	    for (String columnName : columnNameList) {
		if (!record.isColumnExist(columnName)) {
		    Message.print(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName);
		    throw new ParseException();
		}
		else {
		    Column column = columnDictionary.get(columnName);
		    
		    if (!column.canHaveNullValue()) { 
			column.guaranteeNotNull();
			column.setPrimaryKey(this);
			primaryKeyColumns.add(column);
		    }
		}
	    }
	}
    }
    
    public void setForeignKey(ArrayList<String> columnNameList,
	    String referencingTableName,
	    ArrayList<String> referencingColumnNameList) throws ParseException {
	Map<String, Column> columnDictionary = record.getColumnDictionary();
	
	for (String columnName : columnNameList) {   
	    if (!record.isColumnExist(columnName)) {
		Message.print(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName);
	    }
	    else {
		Column column = columnDictionary.get(columnName);
		
		if (!column.isEqualColumnTypeWithReferencingColumn()) {
		    Message.print(Message.REFERENCE_TYPE_ERROR, null);
		    throw new ParseException();
		}
		else if (!column.getReferencingColumn().isPrimaryKey()) {
		    Message.print(Message.REFERENCE_NON_PRIMARY_KEY_ERROR, null);
		    throw new ParseException();
		}
		else if (!record.isColumnExist(column.getReferencingColumn().getColumnName())) {
		    Message.print(Message.REFERENCE_COLUMN_EXISTENCE_ERROR, null);
		    throw new ParseException();
		}
		else if (!record.isTableExist(column.getReferencingTable().getTableName())) {
		    Message.print(Message.REFERENCE_TABLE_EXISTENCE_ERROR, null);
		}
	    }
	}
    }
}
