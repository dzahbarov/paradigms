package expression.generic;

import expression.types.*;

import expression.parser.ExpressionParser;
import expression.exceptions.ParseException;

import java.math.BigInteger;


public class GenericTabulator implements Tabulator {

    public static void main(String[] args) throws ParseException {

        if (args.length < 2) {
            throw new RuntimeException("2 arguments required: mode, expression");
        }
        String mode = args[0];
        String exp = args[1];

        Object[][][] res = new Object[5][5][5];
        try {
            res = new GenericTabulator().eval(mode.substring(1), exp, -2, 2, -2, 2, -2, 2);
        } catch (ParseException e) {
            System.out.println("Error in parsing expression: " + e.getMessage());
        }

        System.out.println(exp);
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    System.out.printf("x=%s, y=%s, z=%d res=%s%n", i, j, k, res[i + 2][j + 2][k + 2]);
                }
            }
        }
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return eval(mode, expression, x1, x2, y1, y2, z1, z2);

    }


    private Object[][][] eval(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParseException {
        switch (mode) {
            case "i" -> {
                GenericExpression<Integer> exp = new ExpressionParser<Integer>().parse(expression);
                return getObjects(x1, x2, y1, y2, z1, z2, exp, new MyInteger());
            }
            case "d" -> {
                GenericExpression<Double> exp = new ExpressionParser<Double>().parse(expression);
                return getObjects(x1, x2, y1, y2, z1, z2, exp, new MyDouble());
            }
            case "bi" -> {
                GenericExpression<BigInteger> exp = new ExpressionParser<BigInteger>().parse(expression);
                return getObjects(x1, x2, y1, y2, z1, z2, exp, new MyBigInteger());
            }
            case "u" -> {
                GenericExpression<Integer> exp = new ExpressionParser<Integer>().parse(expression);
                return getObjects(x1, x2, y1, y2, z1, z2, exp, new MyIntWithoutCheck());
            }
            case "l" -> {
                GenericExpression<Long> exp = new ExpressionParser<Long>().parse(expression);
                return getObjects(x1, x2, y1, y2, z1, z2, exp, new MyLong());
            }
            case "s" -> {
                GenericExpression<Short> exp = new ExpressionParser<Short>().parse(expression);
                return getObjects(x1, x2, y1, y2, z1, z2, exp, new MyShort());
            }
        }
        return null;

    }

    private <T extends Number> Object[][][] getObjects(int x1, int x2, int y1, int y2, int z1, int z2, GenericExpression<T> exp, Num<T> e) {

        Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        res[i - x1][j - y1][k - z1] = exp.evaluate(e, e.convert(i), e.convert(j), e.convert(k));
                    } catch (Exception er) {
                        res[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return res;
    }
}
