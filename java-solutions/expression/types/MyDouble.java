package expression.types;

public class MyDouble implements Num<Double> {

    @Override
    public Double add(Double lhs, Double rhs) {
        return lhs + rhs;
    }

    @Override
    public Double divide(Double lhs, Double rhs) {
        return lhs / rhs;
    }

    @Override
    public Double multiply(Double lhs, Double rhs) {
        return lhs * rhs;
    }

    @Override
    public Double negate(Double element) {
        return -element;
    }

    @Override
    public Double subtract(Double lhs, Double rhs) {
        return lhs - rhs;
    }

    @Override
    public Double abs(Double element) {
        return abs(element);
    }

    @Override
    public Double sqrt(Double element) {
        return Math.sqrt(element);
    }

    @Override
    public Double convert(int element) {
        return (double) element;
    }

}
