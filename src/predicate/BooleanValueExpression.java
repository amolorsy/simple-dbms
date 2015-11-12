package predicate;

import java.util.ArrayList;
import java.util.List;

import relation.Tuple;

public class BooleanValueExpression implements BooleanTest {
    private List<BooleanTerm> booleanTerms;

    public BooleanValueExpression() {
        booleanTerms = new ArrayList<BooleanTerm>();
    }

    public void addBooleanTerm(BooleanTerm booleanTerm) {
        booleanTerms.add(booleanTerm);
    }

    public boolean compute(Tuple tuple) {
        for (BooleanTerm booleanTerm : booleanTerms) {
            boolean result = booleanTerm.compute(tuple);

            if (result)
                return result;
        }

        return false;
    }
}
