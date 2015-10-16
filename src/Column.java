public class Column {
    private boolean canHaveNullValue;
    
    public Column() {
	
    }
    
    public boolean canHaveNullValue() {
	return canHaveNullValue;
    }
    
    public void guaranteeNotNull() {
	canHaveNullValue = false;
    }
}
