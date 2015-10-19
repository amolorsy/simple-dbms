public class Message {
    public static final int PRINT_SYNTAX_ERROR = -1;
    
    public static final int CREATE_TABLE_SUCCESS = 0;
    public static final int DROP_SUCCESS = 1;
    public static final int DROP_SUCCESS_ALL_TABLES = 2;
    
    public static final int DUPLICATE_COLUMN_DEF_ERROR = 3;
    public static final int DUPLICATE_PRIMARY_KEY_DEF_ERROR = 4;
    public static final int REFERENCE_TYPE_ERROR = 5;
    public static final int REFERENCE_NON_PRIMARY_KEY_ERROR = 6;
    public static final int REFERENCE_COLUMN_EXISTENCE_ERROR = 7;
    public static final int REFERENCE_TABLE_EXISTENCE_ERROR = 8;
    public static final int NON_EXISTING_COLUMN_DEF_ERROR = 9;
    public static final int TABLE_EXISTENCE_ERROR = 10;
    public static final int DROP_REFERENCED_TABLE_ERROR = 11;
    public static final int NO_SUCH_TABLE = 12;
    public static final int CHAR_LENGTH_ERROR = 13;
    public static final int PRINT_CREATE_TABLE = 14;
    public static final int PRINT_DROP_TABLE = 15;
    public static final int PRINT_DESC = 16;
    public static final int PRINT_SHOW_TABLES = 17;
    public static final int PRINT_INSERT = 18;
    public static final int PRINT_DELETE = 19;
    public static final int PRINT_SELECT = 20;
    
    public static void print(int type, String word) {
	System.out.print("DB_2013-11379> ");
	
	switch (type) {
	case PRINT_SYNTAX_ERROR:
	    System.out.println("Syntax error");
	    break;
	case CREATE_TABLE_SUCCESS:
	    System.out.println("'" + word + "' table is created");
	    break;
	case DROP_SUCCESS:
	    System.out.println("'" + word + "' table is dropped");
	    break;
	case DROP_SUCCESS_ALL_TABLES:
	    System.out.println("Every table is dropped");
	    break;
	case DUPLICATE_COLUMN_DEF_ERROR:
	    System.out.println("Create table has failed: column definition is duplicated");
	    break;
	case DUPLICATE_PRIMARY_KEY_DEF_ERROR:
	    System.out.println("Create table has failed: primary key definition is duplicated");
	    break;
	case REFERENCE_TYPE_ERROR:
	    System.out.println("Create table has failed: foreign key references wrong type");
	    break;
	case REFERENCE_NON_PRIMARY_KEY_ERROR:
	    System.out.println("Create table has failed: foreign key references non primary key column");
	    break;
	case REFERENCE_COLUMN_EXISTENCE_ERROR:
	    System.out.println("Create table has failed: foreign key references non existing column");
	    break;
	case REFERENCE_TABLE_EXISTENCE_ERROR:
	    System.out.println("Create table has failed: foreign key references non existing table");
	    break;
	case NON_EXISTING_COLUMN_DEF_ERROR:
	    System.out.println("Create table has failed: '" + word + "' does not exists in column definition");
	    break;
	case TABLE_EXISTENCE_ERROR:
	    System.out.println("Create table has failed: table with the same name already exists");
	    break;
	case DROP_REFERENCED_TABLE_ERROR:
	    System.out.println("Drop table has failed: '" + word + "' is referenced by other table");
	    break;
	case NO_SUCH_TABLE:
	    System.out.println("No such table");
	    break;
	case CHAR_LENGTH_ERROR:
	    System.out.println("Char length should be > 0");
	    break;
	case PRINT_CREATE_TABLE:
	    break;
	case PRINT_DROP_TABLE:
	    break;
	case PRINT_DESC:
	    break;
	case PRINT_SHOW_TABLES:
	    System.out.println("\'SHOW TABLES\' requested");
	    break;
	case PRINT_INSERT:
	    System.out.println("\'INSERT\' requested");
	    break;
	case PRINT_DELETE:
	    System.out.println("\'DELETE\' requested");
	    break;
	case PRINT_SELECT:
	    System.out.println("\'SELECT\' requested");
	    break;
	}
    }
}
