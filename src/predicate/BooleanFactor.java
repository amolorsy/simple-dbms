package predicate;

import relation.Tuple;

public class BooleanFactor {
    private BooleanTest booleanTest;
    private boolean precedingNot;

    public void setBooleanTest(BooleanTest booleanTest) {
        this.booleanTest = booleanTest;
    }

    public void setPrecedingNot(boolean flag) {
        this.precedingNot = flag;
    }

    public boolean hasPrecedingNot() {
        return precedingNot;
    }

    public boolean compute(Tuple tuple) {
        if (precedingNot)
            return !booleanTest.compute(tuple);
        else
            return booleanTest.compute(tuple);
    }
}
