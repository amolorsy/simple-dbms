package predicate;

import relation.Tuple;
import relation.column.type.CharType;
import relation.column.type.DateType;
import relation.column.type.IntType;
import relation.column.value.CharValue;
import relation.column.value.ColumnValue;
import relation.column.value.DateValue;
import relation.column.value.IntValue;
import util.Message;

public class ComparisonPredicate implements Predicate {
    private CompOperand leftOperand;
    private CompOperand rightOperand;
    private CompOperator operator;

    public void setLeftCompOperand(CompOperand leftOperand) {
	this.leftOperand = leftOperand;
    }

    public void setRightCompOperand(CompOperand rightOperand) {
	this.rightOperand = rightOperand;
    }

    public void setCompOperator(CompOperator operator) {
	this.operator = operator;
    }

    // Implement function compute of interface Predicate
    public boolean compute(Tuple tuple) {
	ColumnValue leftValue = leftOperand.getColumnValue(tuple);
	ColumnValue rightValue = rightOperand.getColumnValue(tuple);
	String compOperator = operator.getCompOperator();

	if (leftValue == null || rightValue == null)
	    return false;
	else if (leftValue.getColumnType() instanceof IntType && rightValue.getColumnType() instanceof IntType) {
	    IntValue leftIntValue = (IntValue) leftValue;
	    IntValue rightIntValue = (IntValue) rightValue;

	    switch (compOperator) {
	    case "<":
		return leftIntValue.getValue().compareTo(rightIntValue.getValue()) < 0;
	    case ">":
		return leftIntValue.getValue().compareTo(rightIntValue.getValue()) > 0;
	    case "=":
		return leftIntValue.getValue().compareTo(rightIntValue.getValue()) == 0;
	    case "!=":
		return leftIntValue.getValue().compareTo(rightIntValue.getValue()) != 0;
	    case "<=":
		return leftIntValue.getValue().compareTo(rightIntValue.getValue()) <= 0;
	    case ">=":
		return leftIntValue.getValue().compareTo(rightIntValue.getValue()) >= 0;
	    }
	} else if (leftValue.getColumnType() instanceof CharType && rightValue.getColumnType() instanceof CharType) {
	    CharValue leftCharValue = (CharValue) leftValue;
	    CharValue rightCharValue = (CharValue) rightValue;

	    switch (compOperator) {
	    case "<":
		return leftCharValue.getValue().compareTo(rightCharValue.getValue()) < 0;
	    case ">":
		return leftCharValue.getValue().compareTo(rightCharValue.getValue()) > 0;
	    case "=":
		return leftCharValue.getValue().compareTo(rightCharValue.getValue()) == 0;
	    case "!=":
		return leftCharValue.getValue().compareTo(rightCharValue.getValue()) != 0;
	    case "<=":
		return leftCharValue.getValue().compareTo(rightCharValue.getValue()) <= 0;
	    case ">=":
		return leftCharValue.getValue().compareTo(rightCharValue.getValue()) >= 0;
	    }
	} else if (leftValue.getColumnType() instanceof DateType && rightValue.getColumnType() instanceof DateType) {
	    DateValue leftDateValue = (DateValue) leftValue;
	    DateValue rightDateValue = (DateValue) rightValue;

	    switch (compOperator) {
	    case "<":
		return leftDateValue.getValue().compareTo(rightDateValue.getValue()) < 0;
	    case ">":
		return leftDateValue.getValue().compareTo(rightDateValue.getValue()) > 0;
	    case "=":
		return leftDateValue.getValue().compareTo(rightDateValue.getValue()) == 0;
	    case "!=":
		return leftDateValue.getValue().compareTo(rightDateValue.getValue()) != 0;
	    case "<=":
		return leftDateValue.getValue().compareTo(rightDateValue.getValue()) <= 0;
	    case ">=":
		return leftDateValue.getValue().compareTo(rightDateValue.getValue()) >= 0;
	    }
	} else {
	    Message.getInstance().addSchemaError(new Message.Unit(Message.WHERE_INCOMPARABLE_ERROR, null));
	}

	return false;
    }
}
