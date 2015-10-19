import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ForeignKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Table foreignKeyingTable;
    private List<Column> foreignKeyColumns;
    private Table referencingTable;
    private List<Column> referencingColumns;

    public ForeignKey() {
	foreignKeyColumns = new ArrayList<Column>();
	referencingColumns = new ArrayList<Column>();
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

    public void setReferencingTable(Table referencingTable) {
	this.referencingTable = referencingTable;
    }

    public Table getReferencingTable() {
	return referencingTable;
    }

    public void addReferencingColumn(Column referencingColumn) {
	referencingColumns.add(referencingColumn);
    }

    public List<Column> getReferencingColumns() {
	return referencingColumns;
    }
}
