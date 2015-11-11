package predicate;

import relation.Tuple;

public interface Predicate extends BooleanTest {
    public boolean compute(Tuple tuple);
}
