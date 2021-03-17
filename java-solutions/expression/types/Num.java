package expression.types;

public interface Num<T extends Number> {
    public abstract T add(T lhs, T rhs);
    public abstract T divide(T lhs, T rhs);
    public abstract T multiply(T lhs, T rhs);
    public abstract T negate(T element);
    public abstract T subtract(T lhs, T rhs);
    public abstract T abs(T element);
    public abstract T sqrt(T element);
    public abstract T convert(int element);

}
