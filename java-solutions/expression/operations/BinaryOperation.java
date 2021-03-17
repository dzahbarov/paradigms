package expression.operations;

import java.util.Objects;

abstract class BinaryOperation <T extends Number> {
    private final MathExpression<T> lhs;
    private final MathExpression<T> rhs;


    public BinaryOperation(MathExpression<T> lhs, MathExpression<T> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public MathExpression<T> getLhs() {
        return lhs;
    }

    public MathExpression<T> getRhs() {
        return rhs;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryOperation)) return false;
        BinaryOperation<?> that = (BinaryOperation<?>) o;
        return Objects.equals(lhs, that.lhs) && Objects.equals(rhs, that.rhs);
    }

    @Override
    public int hashCode() {
        int res = Objects.hashCode(getLhs()) * 31;
        res = (res + Objects.hashCode(getRhs()))*31;
        res = res + Objects.hashCode(getClass()) * 29;
        return res;
    }
}
