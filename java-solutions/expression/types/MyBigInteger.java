package expression.types;

import java.math.BigInteger;

public class MyBigInteger implements Num<BigInteger> {

    @Override
    public BigInteger add(BigInteger lhs, BigInteger rhs) {
        return lhs.add(rhs);
    }

    @Override
    public BigInteger divide(BigInteger lhs, BigInteger rhs) {
        return lhs.divide(rhs);
    }

    @Override
    public BigInteger multiply(BigInteger lhs, BigInteger rhs) {
        return lhs.multiply(rhs);
    }

    @Override
    public BigInteger negate(BigInteger element) {
        return element.negate();
    }

    @Override
    public BigInteger subtract(BigInteger lhs, BigInteger rhs) {
        return lhs.subtract(rhs);
    }

    @Override
    public BigInteger abs(BigInteger element) {
        return element.abs();
    }

    @Override
    public BigInteger sqrt(BigInteger element) {
        return element.sqrt();
    }

    @Override
    public BigInteger convert(int element) {
        return BigInteger.valueOf(element);
    }

    @Override
    public BigInteger parseConst(String s) {
        return new BigInteger(s);
    }
}
