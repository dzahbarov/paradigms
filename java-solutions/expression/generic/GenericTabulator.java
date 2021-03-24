package expression.generic;

import expression.types.*;

import expression.parser.ExpressionParser;
import expression.exceptions.ParseException;

public class GenericTabulator implements Tabulator {

    public static void main(String[] args) {

        if (args.length < 2) {
            throw new RuntimeException("2 arguments required: mode, expression");
        }
        String mode = args[0];
        String exp = args[1];

        Object[][][] res = new Object[5][5][5];
        try {
            res = new GenericTabulator().tabulate(mode.substring(1), exp, -2, 2, -2, 2, -2, 2);
        } catch (Exception e) {
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

        return switch (mode) {
            case "i" -> getObjects(x1, x2, y1, y2, z1, z2, new MyInteger(), expression);
            case "d" -> getObjects(x1, x2, y1, y2, z1, z2, new MyDouble(), expression);
            case "bi" -> getObjects(x1, x2, y1, y2, z1, z2, new MyBigInteger(), expression);
            case "u" -> getObjects(x1, x2, y1, y2, z1, z2, new MyIntWithoutCheck(), expression);
            case "l" -> getObjects(x1, x2, y1, y2, z1, z2, new MyLong(), expression);
            case "s" -> getObjects(x1, x2, y1, y2, z1, z2, new MyShort(), expression);
            default -> null;
        };


    }

    private <T extends Number> Object[][][] getObjects(int x1, int x2, int y1, int y2, int z1, int z2, Num<T> e, String expression) throws ParseException {
        GenericExpression<T> exp = new ExpressionParser<T>().parse(expression);

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
