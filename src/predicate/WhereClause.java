package predicate;

import java.util.ArrayList;
import java.util.List;

import relation.Tuple;
import util.SelectColumn;

public class WhereClause implements BooleanTest {
    private BooleanValueExpression booleanValueExpression;
    private List<String> referenceTables;
    private List<SelectColumn> selectColumns;
    
    public void setBooleanValueExpression(BooleanValueExpression booleanValueExpression) {
	this.booleanValueExpression = booleanValueExpression;
    }
    
    public void setReferenceTables(ArrayList<String> referenceTables) {
	this.referenceTables = referenceTables;
    }
    
    public List<String> getReferenceTables() {
	return referenceTables;
    }
    
    public void setSelectColumns(ArrayList<SelectColumn> selectColumns) {
	this.selectColumns = selectColumns;
    }
    
    public List<SelectColumn> getSelectColumns() {
	return selectColumns;
    }
    
    @Override
    public boolean compute(Tuple tuple) {
	return booleanValueExpression.compute(tuple);
    }
}
