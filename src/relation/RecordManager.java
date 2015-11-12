package relation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;

import predicate.WhereClause;
import relation.column.Column;
import relation.column.SelectColumn;
import relation.column.type.CharType;
import relation.column.type.DateType;
import relation.column.type.IntType;
import relation.column.value.CharValue;
import relation.column.value.ColumnValue;
import relation.column.value.DateValue;
import relation.column.value.IntValue;
import util.Message;

public class RecordManager {
    private static RecordManager instance;

    private Environment environment;
    private Database database;

    private Map<String, Table> tableDictionary;

    private RecordManager() {
	tableDictionary = new HashMap<String, Table>();
    }

    public static RecordManager getInstance() {
	if (instance == null)
	    instance = new RecordManager();

	return instance;
    }

    /* Berkeley DB setup */
    public void setup() {
	EnvironmentConfig envConfig = new EnvironmentConfig();
	envConfig.setAllowCreate(true);
	environment = new Environment(new File("db/"), envConfig);

	DatabaseConfig dbConfig = new DatabaseConfig();
	dbConfig.setAllowCreate(true);
	dbConfig.setSortedDuplicates(true);
	database = environment.openDatabase(null, "db", dbConfig);
    }

    /* Berkeley DB quit */
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

    // Make object to byte array
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

    // Make byte array to object
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

    // Save table schema on Berkeley DB
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

    // Load table schema from Berkeley DB
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

    /* drop table schema from Berkeley DB */
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

	if (tableNameList.size() == 1 && tableNameList.get(0).equals("*")) {
	    /* desc *; */
	    Set<String> tableNameSet = tableDictionary.keySet();
	    Iterator<String> iterator = tableNameSet.iterator();

	    while (iterator.hasNext()) {
		String tableName = iterator.next();
		Table table = load(tableName);

		if (table != null)
		    tables.add(table);
	    }
	} else {
	    /* desc a, b, c; */
	    for (String tableName : tableNameList) {
		Table table = load(tableName);

		if (table == null) {
		    Message.getInstance().addSchemaError(new Message.Unit(Message.NO_SUCH_TABLE, null));
		    return;
		}

		tables.add(table);
	    }
	}

	for (Table table : tables) {
	    List<Column> tableColumns = table.getTableColumns();

	    System.out.println("-------------------------------------------------");
	    System.out.println("table_name [" + table.getTableName() + "]");
	    System.out.println("column_name\t" + "type\t" + "null\t" + "key");

	    for (Column column : tableColumns) {
		System.out.println(column.getColumnName() + "\t" + column.getColumnType().toString() + "\t"
			+ (column.canHaveNullValue() ? "Y\t" : "N\t") + column.getKeyType());
	    }
	}

	if (tables.size() != 0)
	    System.out.println("-------------------------------------------------");
    }

    public void printTuples(ArrayList<ReferedTable> referenceTables, ArrayList<SelectColumn> selectColumns,
	    WhereClause whereClause) {
	for (ReferedTable referenceTable : referenceTables) {
	    if (!isTableExist(referenceTable.getTableName())) {
		Message.getInstance().addSchemaError(
			new Message.Unit(Message.SELECT_TABLE_EXISTENCE_ERROR, referenceTable.getTableName()));
		return;
	    }
	}

	if (whereClause == null) {
	    if (referenceTables.size() == 1) {
		Table table = load(referenceTables.get(0).getTableName());
		List<Column> tableColumns = table.getTableColumns();
		List<Tuple> tuples = table.getTuples();

		System.out.println("-----------------------------------------");
		if (selectColumns.size() == 1 && selectColumns.get(0).getColumnName().equals("*")) {
		    for (Column column : tableColumns)
			System.out.print("| " + column.getColumnName().toUpperCase() + " ");
		} else {
		    for (SelectColumn selectColumn : selectColumns) {
			for (Column column : tableColumns) {
			    if (column.getColumnName().equals(selectColumn.getColumnName())) {
				if (selectColumn.getTableName() != null) {
				    if (selectColumn.getTableName().equals(column.getTableName())) {
					if (selectColumn.getNickName() != null)
					    System.out.print("| " + selectColumn.getNickName().toUpperCase() + " ");
					else
					    System.out.print("| " + column.getColumnName().toUpperCase() + " ");
				    }
				} else {
				    if (selectColumn.getNickName() != null)
					System.out.print("| " + selectColumn.getNickName().toUpperCase() + " ");
				    else
					System.out.print("| " + column.getColumnName().toUpperCase() + " ");
				}
			    }
			}
		    }
		}
		System.out.println("|");
		System.out.println("-----------------------------------------");

		if (selectColumns.size() == 1 && selectColumns.get(0).getColumnName().equals("*")) {
		    for (Tuple tuple : tuples) {
			List<ColumnValue> columnValues = tuple.getColumnValues();

			for (ColumnValue columnValue : columnValues) {
			    if (columnValue == null)
				System.out.print("| " + columnValue + " ");
			    else {
				if (columnValue.getColumnType() instanceof CharType)
				    System.out.print("| " + ((CharValue) columnValue).getValue() + " ");
				else if (columnValue.getColumnType() instanceof IntType)
				    System.out.print("| " + ((IntValue) columnValue).getValue() + " ");
				else if (columnValue.getColumnType() instanceof DateType)
				    System.out.print("| " + ((DateValue) columnValue).getValue() + " ");
			    }
			}
			System.out.println("|");
		    }
		} else {
		    for (Tuple tuple : tuples) {
			List<ColumnValue> columnValues = tuple.getColumnValues();

			for (SelectColumn selectColumn : selectColumns) {
			    for (ColumnValue columnValue : columnValues) {
				if (columnValue == null) {
				    System.out.print("| " + columnValue + " ");
				    continue;
				}

				if (columnValue.getColumnName().equals(selectColumn.getColumnName())) {
				    if (selectColumn.getTableName() != null) {
					if (selectColumn.getTableName().equals(columnValue.getTableName())) {
					    if (columnValue.getColumnType() instanceof CharType)
						System.out.print("| " + ((CharValue) columnValue).getValue() + " ");
					    else if (columnValue.getColumnType() instanceof IntType)
						System.out.print("| " + ((IntValue) columnValue).getValue() + " ");
					    else if (columnValue.getColumnType() instanceof DateType)
						System.out.print("| " + ((DateValue) columnValue).getValue() + " ");
					}
				    } else {
					if (columnValue.getColumnType() instanceof CharType)
					    System.out.print("| " + ((CharValue) columnValue).getValue() + " ");
					else if (columnValue.getColumnType() instanceof IntType)
					    System.out.print("| " + ((IntValue) columnValue).getValue() + " ");
					else if (columnValue.getColumnType() instanceof DateType)
					    System.out.print("| " + ((DateValue) columnValue).getValue() + " ");
				    }
				}
			    }
			}
			System.out.println("|");
		    }
		}
		System.out.println("-----------------------------------------");
	    } else {
		Table table = load(referenceTables.get(0).getTableName());
		for (int i = 1; i < referenceTables.size(); i++) {
		    Table rightTable = load(referenceTables.get(i).getTableName());
		    table = table.cartesianProduct(rightTable);
		}

		List<Column> tableColumns = table.getTableColumns();
		List<Tuple> tuples = table.getTuples();

		System.out.println("-----------------------------------------");
		if (selectColumns.size() == 1 && selectColumns.get(0).getColumnName().equals("*")) {
		    for (Column column : tableColumns)
			System.out.print("| " + column.getColumnName().toUpperCase() + " ");
		} else {
		    for (SelectColumn selectColumn : selectColumns) {
			for (Column column : tableColumns) {
			    if (column.getColumnName().equals(selectColumn.getColumnName())) {
				if (selectColumn.getTableName() != null) {
				    if (selectColumn.getTableName().equals(column.getTableName())) {
					if (selectColumn.getNickName() != null)
					    System.out.print("| " + selectColumn.getNickName().toUpperCase() + " ");
					else
					    System.out.print("| " + column.getColumnName().toUpperCase() + " ");
				    }
				} else {
				    if (selectColumn.getNickName() != null)
					System.out.print("| " + selectColumn.getNickName().toUpperCase() + " ");
				    else
					System.out.print("| " + column.getColumnName().toUpperCase() + " ");
				}
			    }
			}
		    }
		}
		System.out.println("|");
		System.out.println("-----------------------------------------");

		if (selectColumns.size() == 1 && selectColumns.get(0).getColumnName().equals("*")) {
		    for (Tuple tuple : tuples) {
			List<ColumnValue> columnValues = tuple.getColumnValues();

			for (ColumnValue columnValue : columnValues) {
			    if (columnValue == null) {
				System.out.print("| " + columnValue + " ");
				continue;
			    }

			    if (columnValue.getColumnType() instanceof CharType)
				System.out.print("| " + ((CharValue) columnValue).getValue() + " ");
			    else if (columnValue.getColumnType() instanceof IntType)
				System.out.print("| " + ((IntValue) columnValue).getValue() + " ");
			    else if (columnValue.getColumnType() instanceof DateType)
				System.out.print("| " + ((DateValue) columnValue).getValue() + " ");
			}
			System.out.println("|");
		    }
		} else {
		    for (Tuple tuple : tuples) {
			List<ColumnValue> columnValues = tuple.getColumnValues();

			for (SelectColumn selectColumn : selectColumns) {
			    for (ColumnValue columnValue : columnValues) {
				if (columnValue == null) {
				    System.out.print("| " + columnValue + " ");
				    continue;
				}

				if (columnValue.getColumnName().equals(selectColumn.getColumnName())) {
				    if (selectColumn.getTableName() != null) {
					if (selectColumn.getTableName().equals(columnValue.getTableName())) {
					    if (columnValue.getColumnType() instanceof CharType)
						System.out.print("| " + ((CharValue) columnValue).getValue() + " ");
					    else if (columnValue.getColumnType() instanceof IntType)
						System.out.print("| " + ((IntValue) columnValue).getValue() + " ");
					    else if (columnValue.getColumnType() instanceof DateType)
						System.out.print("| " + ((DateValue) columnValue).getValue() + " ");
					}
				    } else {
					if (columnValue.getColumnType() instanceof CharType)
					    System.out.print("| " + ((CharValue) columnValue).getValue() + " ");
					else if (columnValue.getColumnType() instanceof IntType)
					    System.out.print("| " + ((IntValue) columnValue).getValue() + " ");
					else if (columnValue.getColumnType() instanceof DateType)
					    System.out.print("| " + ((DateValue) columnValue).getValue() + " ");
				    }
				}
			    }
			}
			System.out.println("|");
		    }
		}
		System.out.println("-----------------------------------------");
	    }
	} else {
	    if (referenceTables.size() == 1) {
		Table table = load(referenceTables.get(0).getTableName());
		List<Column> tableColumns = table.getTableColumns();
		List<Tuple> tuples = table.getTuples();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("-----------------------------------------").append("\n");
		if (selectColumns.size() == 1 && selectColumns.get(0).getColumnName().equals("*")) {
		    for (Column column : tableColumns)
			stringBuilder.append("| " + column.getColumnName().toUpperCase() + " ");
		} else {
		    for (SelectColumn selectColumn : selectColumns) {
			for (Column column : tableColumns) {
			    if (column.getColumnName().equals(selectColumn.getColumnName())) {
				if (selectColumn.getTableName() != null) {
				    if (selectColumn.getTableName().equals(column.getTableName())) {
					if (selectColumn.getNickName() != null)
					    stringBuilder.append("| " + selectColumn.getNickName().toUpperCase() + " ");
					else
					    stringBuilder.append("| " + column.getColumnName().toUpperCase() + " ");
				    }
				} else {
				    if (selectColumn.getNickName() != null)
					stringBuilder.append("| " + selectColumn.getNickName().toUpperCase() + " ");
				    else
					stringBuilder.append("| " + column.getColumnName().toUpperCase() + " ");
				}
			    }
			}
		    }
		}
		stringBuilder.append("|").append("\n");
		stringBuilder.append("-----------------------------------------").append("\n");

		if (selectColumns.size() == 1 && selectColumns.get(0).getColumnName().equals("*")) {
		    for (Tuple tuple : tuples) {
			if (!whereClause.compute(tuple))
			    continue;

			List<ColumnValue> columnValues = tuple.getColumnValues();

			for (ColumnValue columnValue : columnValues) {
			    if (columnValue == null) {
				stringBuilder.append("| " + columnValue + " ");
				continue;
			    }

			    if (columnValue.getColumnType() instanceof CharType)
				stringBuilder.append("| " + ((CharValue) columnValue).getValue() + " ");
			    else if (columnValue.getColumnType() instanceof IntType)
				stringBuilder.append("| " + ((IntValue) columnValue).getValue() + " ");
			    else if (columnValue.getColumnType() instanceof DateType)
				stringBuilder.append("| " + ((DateValue) columnValue).getValue() + " ");
			}
			stringBuilder.append("|").append("\n");
		    }
		} else {
		    for (Tuple tuple : tuples) {
			if (!whereClause.compute(tuple))
			    continue;

			List<ColumnValue> columnValues = tuple.getColumnValues();

			for (SelectColumn selectColumn : selectColumns) {
			    for (ColumnValue columnValue : columnValues) {
				if (columnValue == null) {
				    stringBuilder.append("| " + columnValue + " ");
				    continue;
				}

				if (columnValue.getColumnName().equals(selectColumn.getColumnName())) {
				    if (selectColumn.getTableName() != null) {
					if (selectColumn.getTableName().equals(columnValue.getTableName())) {
					    if (columnValue.getColumnType() instanceof CharType)
						stringBuilder.append("| " + ((CharValue) columnValue).getValue() + " ");
					    else if (columnValue.getColumnType() instanceof IntType)
						stringBuilder.append("| " + ((IntValue) columnValue).getValue() + " ");
					    else if (columnValue.getColumnType() instanceof DateType)
						stringBuilder.append("| " + ((DateValue) columnValue).getValue() + " ");
					}
				    } else {
					if (columnValue.getColumnType() instanceof CharType)
					    stringBuilder.append("| " + ((CharValue) columnValue).getValue() + " ");
					else if (columnValue.getColumnType() instanceof IntType)
					    stringBuilder.append("| " + ((IntValue) columnValue).getValue() + " ");
					else if (columnValue.getColumnType() instanceof DateType)
					    stringBuilder.append("| " + ((DateValue) columnValue).getValue() + " ");
				    }
				}
			    }
			}
			stringBuilder.append("|").append("\n");
		    }
		}
		stringBuilder.append("-----------------------------------------");

		if (!Message.getInstance().isSchemaErrorExist())
		    System.out.println(stringBuilder.toString());
	    } else {
		Table table = load(referenceTables.get(0).getTableName());
		for (int i = 1; i < referenceTables.size(); i++) {
		    Table rightTable = load(referenceTables.get(i).getTableName());
		    table = table.cartesianProduct(rightTable);
		}

		List<Column> tableColumns = table.getTableColumns();
		List<Tuple> tuples = table.getTuples();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("-----------------------------------------").append("\n");
		if (selectColumns.size() == 1 && selectColumns.get(0).getColumnName().equals("*")) {
		    for (Column column : tableColumns)
			stringBuilder.append("| " + column.getColumnName().toUpperCase() + " ");
		} else {
		    for (SelectColumn selectColumn : selectColumns) {
			for (Column column : tableColumns) {
			    if (column.getColumnName().equals(selectColumn.getColumnName())) {
				if (selectColumn.getTableName() != null) {
				    if (selectColumn.getTableName().equals(column.getTableName())) {
					if (selectColumn.getNickName() != null)
					    stringBuilder.append("| " + selectColumn.getNickName().toUpperCase() + " ");
					else
					    stringBuilder.append("| " + column.getColumnName().toUpperCase() + " ");
				    }
				} else {
				    if (selectColumn.getNickName() != null)
					stringBuilder.append("| " + selectColumn.getNickName().toUpperCase() + " ");
				    else
					stringBuilder.append("| " + column.getColumnName().toUpperCase() + " ");
				}
			    }
			}
		    }
		}
		stringBuilder.append("|").append("\n");
		stringBuilder.append("-----------------------------------------").append("\n");

		if (selectColumns.size() == 1 && selectColumns.get(0).getColumnName().equals("*")) {
		    for (Tuple tuple : tuples) {
			if (!whereClause.compute(tuple)) {
			    continue;
			}

			List<ColumnValue> columnValues = tuple.getColumnValues();
			for (ColumnValue columnValue : columnValues) {
			    if (columnValue == null) {
				stringBuilder.append("| " + columnValue + " ");
				continue;
			    }

			    if (columnValue.getColumnType() instanceof CharType)
				stringBuilder.append("| " + ((CharValue) columnValue).getValue() + " ");
			    else if (columnValue.getColumnType() instanceof IntType)
				stringBuilder.append("| " + ((IntValue) columnValue).getValue() + " ");
			    else if (columnValue.getColumnType() instanceof DateType)
				stringBuilder.append("| " + ((DateValue) columnValue).getValue() + " ");
			}
			stringBuilder.append("|").append("\n");
		    }
		} else {
		    for (Tuple tuple : tuples) {
			if (!whereClause.compute(tuple))
			    continue;

			List<ColumnValue> columnValues = tuple.getColumnValues();
			for (SelectColumn selectColumn : selectColumns) {
			    for (ColumnValue columnValue : columnValues) {
				if (columnValue == null) {
				    stringBuilder.append("| " + columnValue + " ");
				    continue;
				}
				if (columnValue.getColumnName().equals(selectColumn.getColumnName())) {
				    if (selectColumn.getTableName() != null) {
					if (selectColumn.getTableName().equals(columnValue.getTableName())) {
					    if (columnValue.getColumnType() instanceof CharType)
						stringBuilder.append("| " + ((CharValue) columnValue).getValue() + " ");
					    else if (columnValue.getColumnType() instanceof IntType)
						stringBuilder.append("| " + ((IntValue) columnValue).getValue() + " ");
					    else if (columnValue.getColumnType() instanceof DateType)
						stringBuilder.append("| " + ((DateValue) columnValue).getValue() + " ");
					}
				    } else {
					if (columnValue.getColumnType() instanceof CharType)
					    stringBuilder.append("| " + ((CharValue) columnValue).getValue() + " ");
					else if (columnValue.getColumnType() instanceof IntType)
					    stringBuilder.append("| " + ((IntValue) columnValue).getValue() + " ");
					else if (columnValue.getColumnType() instanceof DateType)
					    stringBuilder.append("| " + ((DateValue) columnValue).getValue() + " ");
				    }
				}
			    }
			}
			stringBuilder.append("|").append("\n");
		    }
		}
		stringBuilder.append("-----------------------------------------");

		if (!Message.getInstance().isSchemaErrorExist())
		    System.out.println(stringBuilder.toString());
	    }
	}
    }
}
