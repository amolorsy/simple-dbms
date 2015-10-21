import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;

public class Record {
    private Environment environment;
    private Database database;

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
    }

    public void quit() {
	if (database != null)
	    database.close();
	if (environment != null)
	    environment.close();
    }

    public boolean isTableExist(String tableName) {
	return tableDictionary.containsKey(tableName);
    }

    public Map<String, Table> getTableDictionary() {
	return tableDictionary;
    }

    public byte[] objectToByteArray(Object object) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	try {
	    ObjectOutputStream oos = new ObjectOutputStream(baos);
	    oos.writeObject(object);
	    
	    return baos.toByteArray();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	return null;
    }

    public Object byteArrayToObject(byte[] byteArray) {
	ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
	
	try {
	    ObjectInputStream ois = new ObjectInputStream(bais);
	    
	    return ois.readObject();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	
	return null;
    }

    public void save(Table table) {
	DatabaseEntry key;
	DatabaseEntry value;

	try {
	    key = new DatabaseEntry(table.getTableName().getBytes("UTF-8"));
	    value = new DatabaseEntry(objectToByteArray(table));
	    database.put(null, key, value);

	    tableDictionary.put(table.getTableName(), table);
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    public Table load(String tableName) {
	Table table = null;
	
	try {
	    DatabaseEntry key = new DatabaseEntry(tableName.getBytes("UTF-8"));
	    DatabaseEntry value = new DatabaseEntry();
	    database.get(null, key, value, LockMode.DEFAULT);
	    
	    if (value.getData() != null) {
		table = (Table) byteArrayToObject(value.getData());
	    }
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}

	return table == null ? null : table;
    }

    public void dropAllTables() {
	for (String tableName : tableDictionary.keySet()) {
	    dropTable(tableName);
	}
    }

    public void dropTable(String tableName) {
	try {
	    DatabaseEntry key = new DatabaseEntry(tableName.getBytes("UTF-8"));
	    database.delete(null, key);

	    tableDictionary.remove(tableName);
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    public void printDesc(ArrayList<String> tableNameList) {
	ArrayList<Table> tables = new ArrayList<Table>();

	for (String tableName : tableNameList) {
	    Table table = load(tableName);

	    if (table == null) {
		Message.getInstance().addSchemaError(new Message.Unit(Message.NO_SUCH_TABLE, null));
		return;
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
