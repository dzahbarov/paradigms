package expression.operations;

import expression.types.Num;

import java.util.Objects;

public class Variable<T extends Number> implements MathExpression<T> {
    private final String varName;

    public Variable(String varName) {
        this.varName = varName;
    }

    @Override
    public int evaluate(int valueOfVariable) {
        return valueOfVariable;
    }

    @Override
    public T evaluate(Num<T> cl, T x, T y, T z) {
        switch (varName) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
        throw new AssertionError("no name");
    }

    @Override
    public String toString() {
        return varName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable<?> variable = (Variable<?>) o;
        return Objects.equals(varName, variable.varName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(varName) * 31;
    }

}

