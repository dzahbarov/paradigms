package expression.parser;

import expression.exceptions.ParseException;
import expression.generic.GenericExpression;

public interface MyParser<T extends Number> {
    GenericExpression<T> parse(String expression) throws ParseException;
}
