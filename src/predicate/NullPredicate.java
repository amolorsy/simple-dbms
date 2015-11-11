package predicate;

import relation.Tuple;

public class NullPredicate implements Predicate {
    public static final int CHECK_NULL = 0;
    public static final int CHECK_NOT_NULL = 1;
    
    private int checkType;
    private CompOperand compOperand;
    
    public void setCheckType(int checkType) {
	this.checkType = checkType;
    }
    
    public int getCheckType() {
	return checkType;
    }
    
    public void setCompOperand(CompOperand compOperand) {
	this.compOperand = compOperand;
    }
    
    // Implement function compute of interface Predicate
    public boolean compute(Tuple tuple) {
	if (checkType == CHECK_NULL)
	    return compOperand.getColumnValue(tuple) == null;
	else
	    return compOperand.getColumnValue(tuple) != null;
    }
}
