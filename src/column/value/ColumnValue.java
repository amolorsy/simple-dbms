package column.value;

import java.io.Serializable;

import column.type.ColumnType;

public abstract class ColumnValue implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected ColumnType columnType;
    
    public abstract void setColumnType(ColumnType columnType);
    
    public abstract ColumnType getColumnType();
}
