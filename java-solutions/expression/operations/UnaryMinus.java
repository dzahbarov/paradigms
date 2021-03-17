package expression.operations;

import expression.types.Num;
import expression.exceptions.EvaluateException;


public class UnaryMinus<T extends Number> extends UnaryOperation<T> {

    public UnaryMinus(MathExpression<T> element) {
        super(element);
    }

    @Override
    public int evaluate(int valueOfVariable) {
        return -element.evaluate(valueOfVariable);
    }


    @Override
    public T evaluate(Num<T> cl, T x, T y, T z) throws EvaluateException {
        return cl.negate(element.evaluate(cl, x, y, z));
    }

    @Override
    public String toString() {
        return "-" + element.toString();
    }
}
