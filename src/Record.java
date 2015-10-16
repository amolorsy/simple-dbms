import java.io.File;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class Record {
    private Environment environment;
    private Database database;

    public Record() {
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
	return true;
    }

    public boolean isColumnExist(String columnName) {
	return true;
    }

    public void save(Table table) {

    }

    public void dropAllTables() {

    }

    public void dropTable(String table) {

    }
}
