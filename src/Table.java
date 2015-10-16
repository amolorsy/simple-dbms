import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {
    private boolean isSetPrimaryKeyDone;
    
    private Map<String, Column> columnDictionary;
    private ArrayList<Column> primaryKeyColumns;
    
    public Table() {
	columnDictionary = new HashMap<String, Column>();
    }
    
    private boolean isColumnExist(String columnName) {
	return columnDictionary.get(columnName) != null ? true : false;
    }
    
    public void setPrimaryKey(ArrayList<String> columnNameList) throws ParseException {
	if (isSetPrimaryKeyDone) {
	    Message.print(Message.DUPLICATE_PRIMARY_KEY_DEF_ERROR, null);
	    throw new ParseException();
	}
	else {
	    for (String columnName : columnNameList) {
		if (!isColumnExist(columnName)) {
		    Message.print(Message.NON_EXISTING_COLUMN_DEF_ERROR, columnName);
		    throw new ParseException();
		}
		else {
		    Column column = columnDictionary.get(columnName);
		    
		    if (!column.canHaveNullValue()) { 
			column.guaranteeNotNull();
			setPrimaryKeyColumn(column);
		    }
		}
	    }
	}
    }
    
    private void setPrimaryKeyColumn(Column column) {
	primaryKeyColumns.add(column);
    }
}
