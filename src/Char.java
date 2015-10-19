public class Char extends ColumnType {
    private int size;
    
    public void setSize(int size) {
	this.size = size;
    }
    
    public String toString() {
	return "char(" + size + ")";
    }
}
