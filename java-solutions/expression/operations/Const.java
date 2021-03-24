package expression.operations;

import expression.types.Num;
import expression.exceptions.EvaluateException;

import java.util.Objects;

public class Const<T extends Number> implements MathExpression<T> {
    private final String value;

    public Const(String value) {
        this.value = value;
    }

    @Override
    public int evaluate(int valueOfVariable) {
        return Integer.parseInt(value);
    }

    @Override
    public T evaluate(Num<T> cl, T x, T y, T z) throws EvaluateException {
        return cl.parseConst(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Const)) return false;
        Const<?> aConst = (Const<?>) o;
        return Objects.equals(value, aConst.value);
    }


    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
