package relation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import relation.column.Column;

public class ForeignKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Table foreignKeyingTable;
    private List<Column> foreignKeyColumns;
    private Map<String, Column> foreignKeyColumnDictionary;
    private Table referencedTable;
    private List<Column> referencedColumns;
    private Map<String, Column> referencedColumnDictionary;

    public ForeignKey() {
	foreignKeyColumns = new ArrayList<Column>();
	foreignKeyColumnDictionary = new HashMap<String, Column>();
	referencedColumns = new ArrayList<Column>();
	referencedColumnDictionary = new HashMap<String, Column>();
    }

    public void setForeignKeyingTable(Table foreignKeyingTable) {
	this.foreignKeyingTable = foreignKeyingTable;
    }

    public Table getForeignKeyingTable() {
	return foreignKeyingTable;
    }

    public void addForeignKeyColumn(Column column) {
	foreignKeyColumns.add(column);
	foreignKeyColumnDictionary.put(column.getColumnName(), column);
    }

    public List<Column> getForeignKeyColumns() {
	return foreignKeyColumns;
    }
    
    public Map<String, Column> getForeignKeyColumnDictionary() {
	return foreignKeyColumnDictionary;
    }

    public void setReferencedTable(Table referencedTable) {
	this.referencedTable = referencedTable;
    }

    public Table getReferencedTable() {
	return referencedTable;
    }

    public void addReferencedColumn(Column referencedColumn) {
	referencedColumns.add(referencedColumn);
	referencedColumnDictionary.put(referencedColumn.getColumnName(), referencedColumn);
    }

    public List<Column> getReferencedColumns() {
	return referencedColumns;
    }
    
    public Map<String, Column> getReferencedColumnDictionary() {
	return referencedColumnDictionary;
    }
}
