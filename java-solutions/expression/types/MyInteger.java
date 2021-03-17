package expression.types;

import expression.exceptions.DBZException;
import expression.exceptions.InvalidSqrtArgument;
import expression.exceptions.OverflowException;


public class MyInteger implements Num<Integer> {

    @Override
    public Integer add(Integer lhs, Integer rhs) {
        return checkAdd(lhs, rhs);
    }

    @Override
    public Integer divide(Integer lhs, Integer rhs) {
        return checkDiv(lhs, rhs);
    }

    @Override
    public Integer multiply(Integer lhs, Integer rhs) {
        return checkMul(lhs, rhs);
    }

    @Override
    public Integer negate(Integer element) {
        return checkNegate(element);
    }

    @Override
    public Integer subtract(Integer lhs, Integer rhs) {
        return checkSub(lhs, rhs);
    }

    @Override
    public Integer abs(Integer element) {
        return checkAbs(element);
    }

    @Override
    public Integer sqrt(Integer element) {
        return checkSqrt(element);
    }

    @Override
    public Integer convert(int element) {
        return element;
    }

    private int checkAdd(int lhs, int rhs) {
        int res = lhs + rhs;
        if (lhs >= 0 && rhs >= 0 && res < 0) {
            throw new OverflowException("overflow");
        } else if (lhs < 0 && rhs < 0 && res >= 0) {
            throw new OverflowException("overflow");
        }
        return res;
    }

    private int checkDiv(int lhs, int rhs) {

        if (rhs == 0) {
            throw new DBZException("division by zero");
        }

        int res = lhs / rhs;
        if (lhs < 0 && rhs < 0 && res < 0) {
            throw new OverflowException("overflow");
        }
        return lhs / rhs;
    }

    private int checkMul(int lhs, int rhs) {

        int result = lhs * rhs;
        if (rhs != 0 && result / rhs != lhs || (lhs < 0 && rhs < 0 && result < 0)) {
            throw new OverflowException("overflow");
        }
        return result;
    }

    private int checkNegate(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return -value;
    }

    private int checkSub(int lhs, int rhs) {
        int res = lhs - rhs;
        if (lhs >= 0 && rhs <= 0 && res < 0) {
            throw new OverflowException("overflow");
        } else if (lhs < 0 && rhs > 0 && res >= 0) {
            throw new OverflowException("overflow");
        }
        return res;
    }

    public int checkAbs(int r) {
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

    public int checkSqrt(int r) {
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
}
