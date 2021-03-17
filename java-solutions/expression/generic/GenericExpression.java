package expression.generic;

import expression.types.Num;
import expression.operations.ToMiniString;

public interface GenericExpression<T extends Number> extends ToMiniString {
    T evaluate(Num<T> cl, T x, T y, T z);
}