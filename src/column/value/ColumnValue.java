package column.value;

import java.io.Serializable;

import column.type.ColumnType;

public abstract class ColumnValue implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String columnName;
    protected ColumnType columnType;
    
    public abstract void setColumnName(String columnName);
    
    public abstract String getColumnName();
    
    public abstract void setColumnType(ColumnType columnType);
    
    public abstract ColumnType getColumnType();
}
