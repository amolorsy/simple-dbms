package relation.column;

public class SelectColumn {
    private String tableName;
    private String tableNickName;
    private String columnName;
    private String nickName;
    
    public void setTableName(String tableName) {
	this.tableName = tableName;
    }
    
    public String getTableName() {
	return tableName;
    }
    
    public void setTableNickName(String tableNickName) {
	this.tableNickName = tableNickName;
    }
    
    public String getTableNickName() {
	return tableNickName;
    }
    
    public void setColumnName(String columnName) {
	this.columnName = columnName;
    }
    
    public String getColumnName() {
	return columnName;
    }
    
    public void setNickName(String nickName) {
	this.nickName = nickName;
    }
    
    public String getNickName() {
	return nickName;
    } 
}
