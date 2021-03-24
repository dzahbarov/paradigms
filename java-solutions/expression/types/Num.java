package expression.types;

public interface Num<T extends Number> {
    T add(T lhs, T rhs);
    T divide(T lhs, T rhs);
    T multiply(T lhs, T rhs);
    T negate(T element);
    T subtract(T lhs, T rhs);
    T abs(T element);
    T sqrt(T element);
    T convert(int element);
    T parseConst(String s);
}
