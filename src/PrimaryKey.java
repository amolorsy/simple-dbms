import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrimaryKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Column> primaryKeyColumns;
    private Table primaryKeyingTable;
    
    public PrimaryKey() {
	primaryKeyColumns = new ArrayList<Column>();
    }
    
    public void addPrimaryKeyColumns(Column column) {
	primaryKeyColumns.add(column);
    }
    
    public List<Column> getPrimaryKeyColumns() {
	return primaryKeyColumns;
    }
    
    public void setPrimaryKeyingTable(Table primaryKeyingTable) {
	this.primaryKeyingTable = primaryKeyingTable;
    }
    
    public Table getPrimaryKeyingTable() {
	return primaryKeyingTable;
    }
}
