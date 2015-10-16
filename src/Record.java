import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class Record {
    private Environment environment;
    private Database database;
    
    private Map<String, Table> tableDictionary;
    private Map<String, Column> columnDictionary;

    public Record() {
	tableDictionary = new HashMap<String, Table>();
	columnDictionary = new HashMap<String, Column>();
    }

    public void setup() {
	EnvironmentConfig envConfig = new EnvironmentConfig();
	envConfig.setAllowCreate(true);
	environment = new Environment(new File("db/"), envConfig);

	DatabaseConfig dbConfig = new DatabaseConfig();
	dbConfig.setAllowCreate(true);
	dbConfig.setSortedDuplicates(true);
	database = environment.openDatabase(null, "db", dbConfig);
    }

    public void quit() {
	if (environment != null)
	    environment.close();
	if (database != null)
	    database.close();
    }

    public boolean isTableExist(String tableName) {
	return tableDictionary.get(tableName) != null ? true : false;
    }
    
    public boolean isColumnExist(String columnName) {
	return columnDictionary.get(columnName) != null ? true : false;
    }
    
    public Map<String, Table> getTableDictionary() {
	return tableDictionary;
    }
    
    public Map<String, Column> getColumnDictionary() {
	return columnDictionary;
    }

    public void save(Table table) {

    }

    public void dropAllTables() {

    }

    public void dropTable(String table) {

    }
}
