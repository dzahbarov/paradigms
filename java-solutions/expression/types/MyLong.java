package expression.types;

public class MyLong implements Num<Long> {
    @Override
    public Long add(Long lhs, Long rhs) {
        return lhs + rhs;
    }

    @Override
    public Long divide(Long lhs, Long rhs) {
        return lhs / rhs;
    }

    @Override
    public Long multiply(Long lhs, Long rhs) {
        return lhs * rhs;
    }

    @Override
    public Long negate(Long element) {
        return -element;
    }

    @Override
    public Long subtract(Long lhs, Long rhs) {
        return lhs - rhs;
    }

    @Override
    public Long abs(Long element) {
        if (element >= 0) return element;
        return negate(element);
    }

    @Override
    public Long sqrt(Long element) {
        return getSqrt(element);
    }

    @Override
    public Long convert(int element) {
        return (long) element;
    }

    @Override
    public Long parseConst(String s) {
        return Long.parseLong(s);
    }

    private Long getSqrt(Long r) {
        Long i = 0L;
        while (i * i <= r) {
            i++;
        }
        return i - 1;
    }
}
