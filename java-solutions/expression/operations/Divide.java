package expression.operations;

import expression.types.Num;
import expression.exceptions.EvaluateException;

public class Divide<T extends Number> extends BinaryOperation<T> implements MathExpression<T> {

    public Divide(MathExpression<T> lhs, MathExpression<T> rhs) {
        super(lhs, rhs);
    }


    @Override
    public int evaluate(int valueOfVariable) {
        return getLhs().evaluate(valueOfVariable) / getRhs().evaluate(valueOfVariable);
    }

    @Override
    public T evaluate(Num<T> cl, T x, T y, T z) throws EvaluateException {
        return cl.divide(getLhs().evaluate(cl, x, y, z), getRhs().evaluate(cl, x, y, z));
    }

    @Override
    public String toString() {
        return "(" + getLhs().toString() + " / " + getRhs().toString() + ")";
    }


}
