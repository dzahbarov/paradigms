package expression.types;

public class MyShort implements Num<Short>{

    @Override
    public Short add(Short lhs, Short rhs) {

        return (short)(lhs + rhs);
    }

    @Override
    public Short divide(Short lhs, Short rhs) {
        return (short) (lhs / rhs);
    }

    @Override
    public Short multiply(Short lhs, Short rhs) {
        return (short) (lhs * rhs);
    }

    @Override
    public Short negate(Short element) {
        return (short) (-element);
    }

    @Override
    public Short subtract(Short lhs, Short rhs) {
        return (short) (lhs - rhs);
    }

    @Override
    public Short abs(Short element) {
        if (element >= 0) return element;
        return negate(element);
    }

    @Override
    public Short sqrt(Short element) {
        return getSqrt(element);
    }

    @Override
    public Short parseConst(String s) {
        return Short.parseShort(s);
    }

    @Override
    public Short convert(int element) {
        return (short) element;
    }

    private Short getSqrt(short r) {
        int i = 0;
        while (i * i <= r) {
            i++;
        }
        return (short) (i - 1);
    }
}
