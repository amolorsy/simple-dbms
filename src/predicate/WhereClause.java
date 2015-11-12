package predicate;

import relation.Tuple;

public class WhereClause implements BooleanTest {
    private BooleanValueExpression booleanValueExpression;
    
    public void setBooleanValueExpression(BooleanValueExpression booleanValueExpression) {
	this.booleanValueExpression = booleanValueExpression;
    }
    
    @Override
    public boolean compute(Tuple tuple) {
	return booleanValueExpression.compute(tuple);
    }
}
