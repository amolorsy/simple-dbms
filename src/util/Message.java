package util;

import java.util.ArrayList;

public class Message {
    private static Message instance;

    public static final int PRINT_SYNTAX_ERROR = -1;

    public static final int CREATE_TABLE_SUCCESS = 0;
    public static final int DROP_SUCCESS = 1;
    public static final int DROP_SUCCESS_ALL_TABLES = 2;
    public static final int INSERT_RESULT = 3;
    public static final int DELETE_RESULT = 4;

    public static final int DUPLICATE_COLUMN_DEF_ERROR = 5;
    public static final int DUPLICATE_PRIMARY_KEY_DEF_ERROR = 6;
    public static final int REFERENCE_TYPE_ERROR = 7;
    public static final int REFERENCE_NON_PRIMARY_KEY_ERROR = 8;
    public static final int REFERENCE_COLUMN_EXISTENCE_ERROR = 9;
    public static final int REFERENCE_TABLE_EXISTENCE_ERROR = 10;
    public static final int NON_EXISTING_COLUMN_DEF_ERROR = 11;
    public static final int TABLE_EXISTENCE_ERROR = 12;
    public static final int DROP_REFERENCED_TABLE_ERROR = 13;
    public static final int NO_SUCH_TABLE = 14;
    public static final int CHAR_LENGTH_ERROR = 15;
    public static final int INSERT_TYPE_MISMATCH_ERROR = 16;
    public static final int INSERT_COLUMN_NON_NULLABLE_ERROR = 17;
    public static final int INSERT_COLUMN_EXISTENCE_ERROR = 18;
    public static final int INSERT_DUPLICATE_PRIMARY_KEY_ERROR = 19;
    public static final int INSERT_REFERENTIAL_INTEGRITY_ERROR = 20;
    public static final int INSERT_COLUMN_VALUE_CORRESPONDENCE_ERROR = 21;
    public static final int DELETE_REFERENTIAL_INTEGRITY_PASSED = 22;
    public static final int WHERE_INCOMPARABLE_ERROR = 23;
    public static final int WHERE_TABLE_NOT_SPECIFIED = 24;
    public static final int WHERE_COLUMN_NOT_EXIST = 25;
    public static final int WHERE_AMBIGUOUS_REFERENCE = 26;
    public static final int SELECT_TABLE_EXISTENCE_ERROR = 27;
    public static final int SELECT_COLUMN_RESOLVE_ERROR = 28;
    public static final int REFERENCE_COLUMNS_PARTIAL_PRIMARY_KEY_COLUMNS_ERROR = 29;

    public static final int PRINT_CREATE_TABLE = 30;
    public static final int PRINT_DROP_TABLE = 31;
    public static final int PRINT_DESC = 32;
    public static final int PRINT_SHOW_TABLES = 33;
    public static final int PRINT_INSERT = 34;
    public static final int PRINT_DELETE = 35;
    public static final int PRINT_SELECT = 36;

    /*
     * For create table, desc command, schemaErrors is used to store errors of execution
     */
    private ArrayList<Unit> schemaErrors;

    /*
     * For create table command, commandResults is used to store success execution results.
     * For drop table command, commandResults is used to store dropping execution results of tables.
     */
    private ArrayList<Unit> commandResults;

    private Message() {
        schemaErrors = new ArrayList<Unit>();
    }

    public static Message getInstance() {
        if (instance == null)
            instance = new Message();

        return instance;
    }

    public void addSchemaError(Unit error) {
        schemaErrors.add(error);
    }

    public boolean isSchemaErrorExist() {
        return schemaErrors.size() == 0 ? false : true;
    }

    public ArrayList<Unit> getSchemaErrors() {
        return schemaErrors;
    }

    public void resetSchemaErrors() {
        schemaErrors = new ArrayList<Unit>();
    }

    public void addCommandResult(Unit result) {
        commandResults.add(result);
    }

    public ArrayList<Unit> getCommandResults() {
        return commandResults;
    }

    public void resetCommandResults() {
        commandResults = new ArrayList<Unit>();
    }

    public static void print(int type, String word) {
        System.out.print("DB_STUDENT-NUMBER> ");

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
        case INSERT_RESULT:
            System.out.println("The row is inserted");
            break;
        case DELETE_RESULT:
            System.out.println(word + " row(s) are deleted");
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
        case INSERT_TYPE_MISMATCH_ERROR:
            System.out.println("Insertion has failed: Types are not matched");
            break;
        case INSERT_COLUMN_NON_NULLABLE_ERROR:
            System.out.println("Insertion has failed: '" + word + "' is not nullable");
            break;
        case INSERT_COLUMN_EXISTENCE_ERROR:
            System.out.println("Insertion has failed: '" + word + "' does not exist");
            break;
        case INSERT_DUPLICATE_PRIMARY_KEY_ERROR:
            System.out.println("Insertion has failed: Primary key duplication");
            break;
        case INSERT_REFERENTIAL_INTEGRITY_ERROR:
            System.out.println("Insertion has failed: Referential integrity violation");
            break;
        case INSERT_COLUMN_VALUE_CORRESPONDENCE_ERROR:
            System.out.println("Insertion has failed: the value can't be corresponded to table column");
            break;
        case DELETE_REFERENTIAL_INTEGRITY_PASSED:
            System.out.println(word + " row(s) are not deleted due to referential integrity");
            break;
        case WHERE_INCOMPARABLE_ERROR:
            System.out.println("Where clause try to compare incomparable values");
            break;
        case WHERE_TABLE_NOT_SPECIFIED:
            System.out.println("Where clause try to reference tables which are not specified");
            break;
        case WHERE_COLUMN_NOT_EXIST:
            System.out.println("Where clause try to reference non existing column");
            break;
        case WHERE_AMBIGUOUS_REFERENCE:
            System.out.println("Where clause contains ambiguous reference");
            break;
        case SELECT_TABLE_EXISTENCE_ERROR:
            System.out.println("Selection has failed: '" + word + "' does not exist");
            break;
        case SELECT_COLUMN_RESOLVE_ERROR:
            System.out.println("Selection has failed: fail to resolve '" + word + "'");
            break;
        case REFERENCE_COLUMNS_PARTIAL_PRIMARY_KEY_COLUMNS_ERROR:
            System.out.println("Create table has failed: foreign key references primary key columns partially");
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
            break;
        case PRINT_DELETE:
            break;
        case PRINT_SELECT:
            break;
        }
    }

    // Include message type, word.
    // Word is used in printing message.
    public static class Unit {
        private int messageType;
        private String word;

        public Unit(int messageType, String word) {
            this.messageType = messageType;
            this.word = word;
        }

        public int getMessageType() {
            return messageType;
        }

        public String getWord() {
            return word;
        }
    }
}
