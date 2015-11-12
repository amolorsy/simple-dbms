package relation;

public class ReferedTable {
    private String tableName;
    private String nickName;

    public ReferedTable() {
    }

    public ReferedTable(String tableName) {
	this.tableName = tableName;
    }

    public ReferedTable(String tableName, String nickName) {
	this.tableName = tableName;
	this.nickName = nickName;
    }

    public void setTableName(String tableName) {
	this.tableName = tableName;
    }

    public String getTableName() {
	return tableName;
    }

    public void setNickName(String nickName) {
	this.nickName = nickName;
    }

    public String getNickName() {
	return nickName;
    }
}
