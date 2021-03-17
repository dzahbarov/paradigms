package expression.operations;

import expression.types.Num;
import expression.exceptions.EvaluateException;
import expression.exceptions.InvalidSqrtArgument;

public class Sqrt<T extends Number> extends UnaryOperation<T> {

    public Sqrt(MathExpression<T> element) {
        super(element);
    }

    @Override
    public int evaluate(int valueOfVariable) {
        int r = element.evaluate(valueOfVariable);
        return check(r);
    }

    public int check(int r) {
        if (r >= 0) {
            return getSqrt(r);
        }
        throw new InvalidSqrtArgument("invalid sqrt argument");
    }

    private int getSqrt(int r) {
        int i = 0;
        while (i * i <= r) {
            i++;
        }

        return i - 1;
    }

    @Override
    public String toString() {
        return "sqrt(" + element.toString() + ")";
    }

    @Override
    public T evaluate(Num<T> cl, T x, T y, T z) throws EvaluateException {
        return cl.sqrt(element.evaluate(cl, x, y, z));
    }
}
