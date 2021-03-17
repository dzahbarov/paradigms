package expression.operations;

import java.util.Objects;

public abstract class UnaryOperation<T extends Number> implements MathExpression<T> {
    protected final MathExpression<T> element;


    public UnaryOperation(MathExpression<T> element) {
        this.element = element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnaryOperation)) return false;
        UnaryOperation<?> that = (UnaryOperation<?>) o;
        return Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(element) * 31 + Objects.hashCode(getClass()) * 29;
    }

}
