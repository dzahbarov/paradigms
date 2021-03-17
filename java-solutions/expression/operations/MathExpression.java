package expression.operations;

import expression.generic.GenericExpression;

public interface MathExpression<T extends Number> extends Expression, GenericExpression<T> {
    String toString();

    boolean equals(Object element);
}
