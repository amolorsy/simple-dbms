package predicate;

import java.util.ArrayList;
import java.util.List;

import relation.Tuple;

public class BooleanTerm {
    private List<BooleanFactor> booleanFactors;
    
    public BooleanTerm() {
	booleanFactors = new ArrayList<BooleanFactor>();
    }
    
    public void addBooleanFactor(BooleanFactor booleanFactor) {
	booleanFactors.add(booleanFactor);
    }
    
    public boolean compute(Tuple tuple) {
	for (BooleanFactor booleanFactor : booleanFactors) {
	    boolean result = booleanFactor.compute(tuple);
	    
	    if (!result)
		return false;
	}
	
	return true;
    }
}
