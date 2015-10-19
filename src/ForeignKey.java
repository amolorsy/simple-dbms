import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ForeignKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Table foreignKeyingTable;
    private List<Column> foreignKeyColumns;
    private Table referencedTable;
    private List<Column> referencedColumns;

    public ForeignKey() {
	foreignKeyColumns = new ArrayList<Column>();
	referencedColumns = new ArrayList<Column>();
    }

    public void setForeignKeyingTable(Table foreignKeyingTable) {
	this.foreignKeyingTable = foreignKeyingTable;
    }

    public Table getForeignKeyingTable() {
	return foreignKeyingTable;
    }

    public void addForeignKeyColumn(Column column) {
	foreignKeyColumns.add(column);
    }

    public List<Column> getForeignKeyColumns() {
	return foreignKeyColumns;
    }

    public void setReferencedTable(Table referencedTable) {
	this.referencedTable = referencedTable;
    }

    public Table getReferencedTable() {
	return referencedTable;
    }

    public void addReferencedColumn(Column referencedColumn) {
	referencedColumns.add(referencedColumn);
    }

    public List<Column> getReferencedColumns() {
	return referencedColumns;
    }
}
