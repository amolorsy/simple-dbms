public class Column {
    private boolean canHaveNullValue;
    private boolean isPrimaryKey;
    
    private String columnName;
    private String columnType;
    private Table keyingTable;
    private Column referencingColumn;
    private Table referencingTable;
    
    public Column(String columnType) {
	this.columnType = columnType;
    }
    
    public String getColumnName() {
	return columnName;
    }
    
    public String getColumnType() {
	return columnType;
    }
    
    public boolean canHaveNullValue() {
  	return canHaveNullValue;
    }
    
    public void setReferencingColumn(Column referencingColumn) {
	this.referencingColumn = referencingColumn;
    }
    
    public Column getReferencingColumn() {
   	return referencingColumn;
    }
    
    public void setReferencingTable(Table referencingTable) {
	this.referencingTable = referencingTable;
    }
    
    public Table getReferencingTable() {
	return referencingTable;
    }
    
    public boolean isPrimaryKey() {
	return isPrimaryKey;
    }
    
    public void setPrimaryKey(Table table) {
	keyingTable = table;
    }
    
    public void guaranteeNotNull() {
	canHaveNullValue = false;
    }
    
    public boolean isEqualColumnTypeWithReferencingColumn() {
	return columnType.equals(referencingColumn.getColumnType());
    }
}
