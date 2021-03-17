package expression.types;

public class MyIntWithoutCheck implements Num<Integer> {
    @Override
    public Integer add(Integer lhs, Integer rhs) {
        return lhs + rhs;
    }

    @Override
    public Integer divide(Integer lhs, Integer rhs) {
        return lhs / rhs;
    }

    @Override
    public Integer multiply(Integer lhs, Integer rhs) {
        return lhs * rhs;
    }

    @Override
    public Integer negate(Integer element) {
        return -element;
    }

    @Override
    public Integer subtract(Integer lhs, Integer rhs) {
        return lhs - rhs;
    }

    @Override
    public Integer abs(Integer element) {
        if (element >= 0) return element;
        return -element;
    }

    @Override
    public Integer sqrt(Integer element) {
        return getSqrt(element);
    }

    @Override
    public Integer convert(int element) {
        return element;
    }

    private int getSqrt(int r) {
        int i = 0;
        while (i * i <= r) {
            i++;
        }
        return i - 1;
    }

}
