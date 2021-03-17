package expression.operations;

import expression.types.Num;
import expression.exceptions.EvaluateException;
import expression.exceptions.OverflowException;

public class Abs<T extends Number> extends UnaryOperation<T> {

    public Abs(MathExpression<T> element) {
        super(element);
    }

    @Override
    public int evaluate(int valueOfVariable) {
        int r = element.evaluate(valueOfVariable);
        return check(r);
    }

    public int check(int r) {
        if (r >= 0) {
            return r;
        } else {
            if (r == Integer.MIN_VALUE) {
                throw new OverflowException("overflow");
            } else {
                return -r;
            }
        }
    }

    @Override
    public String toString() {
        return "|" + element.toString() + "|";
    }

    @Override
    public T evaluate(Num<T> cl, T x, T y, T z) throws EvaluateException {
        return cl.abs(element.evaluate(cl, x, y, z));
    }
}
