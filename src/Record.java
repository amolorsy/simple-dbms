import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;

public class Record {
    private Environment environment;
    private Database database;
    private Database classDatabase;
    private EntryBinding<Table> entryBinding;

    private Map<String, Table> tableDictionary;

    public Record() {
	tableDictionary = new HashMap<String, Table>();
    }

    public void setup() {
	EnvironmentConfig envConfig = new EnvironmentConfig();
	envConfig.setAllowCreate(true);
	environment = new Environment(new File("db/"), envConfig);

	DatabaseConfig dbConfig = new DatabaseConfig();
	dbConfig.setAllowCreate(true);
	dbConfig.setSortedDuplicates(true);
	database = environment.openDatabase(null, "db", dbConfig);

	dbConfig.setSortedDuplicates(false);
	classDatabase = environment.openDatabase(null, "class_db", dbConfig);

	StoredClassCatalog storedClassCatalog = new StoredClassCatalog(classDatabase);
	entryBinding = new SerialBinding<Table>(storedClassCatalog, Table.class);
    }

    public void quit() {
	if (environment != null)
	    environment.close();
	if (database != null)
	    database.close();
	if (classDatabase != null)
	    classDatabase.close();
    }

    public boolean isTableExist(String tableName) {
	return tableDictionary.containsKey(tableName);
    }

    public Map<String, Table> getTableDictionary() {
	return tableDictionary;
    }

    public void save(Table table) {
	DatabaseEntry key;
	DatabaseEntry value;

	try {
	    key = new DatabaseEntry(table.getTableName().getBytes("UTF-8"));
	    value = new DatabaseEntry();
	    entryBinding.objectToEntry(table, value);
	    database.put(null, key, value);
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    public Table load(String tableName) {
	try {
	    DatabaseEntry key = new DatabaseEntry(tableName.getBytes("UTF-8"));
	    DatabaseEntry value = new DatabaseEntry();
	    database.get(null, key, value, LockMode.DEFAULT);

	    return ((Table) entryBinding.entryToObject(value));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}

	return null;
    }

    public void dropAllTables() {

    }

    public void dropTable(String table) {

    }

    /*
     * Map<String, Column> tableColumnDictionary =
     * table.getTableColumnDictionary(); PrimaryKey primaryKey =
     * table.getPrimaryKey(); List<ForeignKey> foreignKeys =
     * table.getForeignKeys();
     * 
     * // Table name key = new DatabaseEntry("table_name".getBytes("UTF-8"));
     * value = new DatabaseEntry(table.getTableName().getBytes("UTF-8"));
     * cursor.put(key, value);
     * 
     * // Table columns for (String columnName : tableColumnDictionary.keySet())
     * { key = new DatabaseEntry(columnName.getBytes("UTF-8")); value = new
     * DatabaseEntry(
     * tableColumnDictionary.get(columnName).getColumnType().toString().getBytes
     * ("UTF-8")); cursor.put(key, value); }
     * 
     * // Primary key StringBuilder stringBuilder = new StringBuilder();
     * List<Column> primaryKeyColumns = primaryKey.getPrimaryKeyColumns(); for
     * (int i = 0; i < primaryKeyColumns.size(); i++) { if (i == 0)
     * stringBuilder.append(primaryKeyColumns.get(i).getColumnName()); else
     * stringBuilder.append(", "
     * ).append(primaryKeyColumns.get(i).getColumnName()); } key = new
     * DatabaseEntry("primary_key".getBytes("UTF-8")); value = new
     * DatabaseEntry(stringBuilder.toString().getBytes("UTF-8"));
     * cursor.put(key, value);
     * 
     * // Foreign keys int count = 1; Iterator<ForeignKey> iterator =
     * foreignKeys.iterator(); while (iterator.hasNext()) { stringBuilder = new
     * StringBuilder();
     * 
     * ForeignKey foreignKey = iterator.next(); List<Column> foreignKeyColumns =
     * foreignKey.getForeignKeyColumns(); for (int i = 0; i <
     * foreignKeyColumns.size(); i++) { if (i == 0)
     * stringBuilder.append(foreignKeyColumns.get(i).getColumnName()); else
     * stringBuilder.append(", "
     * ).append(foreignKeyColumns.get(i).getColumnName()); }
     * 
     * key = new DatabaseEntry(("foreign_key" + count++).getBytes("UTF-8"));
     * value = new DatabaseEntry(stringBuilder.toString().getBytes("UTF-8"));
     * cursor.put(key, value);
     * 
     * tableDictionary.put(table.getTableName(), table); }
     */
}
