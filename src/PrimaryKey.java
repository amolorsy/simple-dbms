import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimaryKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Column> primaryKeyColumns;
    private Map<String, Column> primaryKeyColumnDictionary;
    private Table primaryKeyingTable;
    
    public PrimaryKey() {
	primaryKeyColumns = new ArrayList<Column>();
	primaryKeyColumnDictionary = new HashMap<String, Column>();
    }
    
    public void addPrimaryKeyColumn(Column column) {
	primaryKeyColumns.add(column);
	primaryKeyColumnDictionary.put(column.getColumnName(), column);
    }
    
    public List<Column> getPrimaryKeyColumns() {
	return primaryKeyColumns;
    }
    
    public Map<String, Column> getPrimaryKeyColumnDictionary() {
	return primaryKeyColumnDictionary;
    }
    
    public void setPrimaryKeyingTable(Table primaryKeyingTable) {
	this.primaryKeyingTable = primaryKeyingTable;
    }
    
    public Table getPrimaryKeyingTable() {
	return primaryKeyingTable;
    }
}
