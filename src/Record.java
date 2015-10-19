import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	if (database != null)
	    database.close();
	if (classDatabase != null)
	    classDatabase.close();
	if (environment != null)
	    environment.close();
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

	    tableDictionary.put(table.getTableName(), table);
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
	for (String tableName : tableDictionary.keySet()) {
	    dropTable(tableName);
	}
    }

    public void dropTable(String tableName) {
	try {
	    DatabaseEntry key = new DatabaseEntry(tableName.getBytes("UTF-8"));
	    DatabaseEntry value = new DatabaseEntry();
	    database.get(null, key, value, LockMode.DEFAULT);

	    Table table = (Table) entryBinding.entryToObject(value);
	    table = null;
	    database.delete(null, value);

	    tableDictionary.remove(tableName);
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    public void printDesc(ArrayList<String> tableNameList) throws ParseException {
	ArrayList<Table> tables = new ArrayList<Table>();

	for (String tableName : tableNameList) {
	    Table table = load(tableName);

	    if (table == null) {
		Message.print(Message.NO_SUCH_TABLE, null);
		throw new ParseException();
	    }

	    tables.add(table);
	}

	for (Table table : tables) {
	    List<Column> tableColumns = table.getTableColumns();

	    System.out.println("-------------------------------------------------");
	    System.out.println("table_name [" + table.getTableName() + "]");
	    System.out.println("column_name\t\t" + "type\t\t" + "null\t\t" + "key");

	    for (Column column : tableColumns) {
		System.out.println(column.getColumnName() + "\t\t" + column.getColumnType().toString() + "\t\t"
			+ (column.canHaveNullValue() ? "Y\t\t" : "N\t\t") + column.getKeyType());
	    }
	}

	System.out.println("-------------------------------------------------");
    }
}
