package expression.parser;

import expression.exceptions.*;
import expression.generic.GenericExpression;
import expression.operations.*;


import java.util.Objects;

public class ExpressionParser<T extends Number> extends BaseParser implements Parser<T> {

    @Override
    public GenericExpression<T> parse(String expression) throws ParseException {
        this.source = new StringSource(expression);
        nextChar();
        return parseExpression();
    }

    private GenericExpression<T> parseExpression() throws ParseException {

        GenericExpression<T> result = parseAddSub();
        if (ch == ')') {
            throw new InvalidBracketsException(getPos(), "No opening parenthesis");
        } else if (Character.isDigit(ch)) {
            throw new InvalidConstException(getPos(), "Spaces in numbers");
        } else if (notEndOfInput()) {
            throw new ParseException(getPos(), "End-of-input expected");
        }
        return result;
    }

    private GenericExpression<T> parseAddSub() throws ParseException {
        return parseBinary(Operations.ADD, Operations.SUB);
    }

    private GenericExpression<T> parseMulDiv() throws ParseException {
        return parseBinary(Operations.MUL, Operations.DIV);
    }

    private GenericExpression<T> parseUnsignedConst() throws InvalidConstException {
        return parseConst(new StringBuilder());
    }

    private GenericExpression<T> parseNegativeConst() throws InvalidConstException {
        return parseConst(new StringBuilder().append('-'));
    }

    private GenericExpression<T> parseSqrt() throws ParseException {
        return new Sqrt<T>((MathExpression<T>) parseLastLevel());
    }

    private GenericExpression<T> parseAbs() throws ParseException {
        return new Abs<T>((MathExpression<T>) parseLastLevel());
    }

    private GenericExpression<T> parseUnaryElement() throws ParseException {
        return new UnaryMinus<T>((MathExpression<T>) parseLastLevel());
    }

    private GenericExpression<T> parseLowerPriority(Operations operation) throws ParseException {
        return parse(Operations.getLower(operation));
    }

    private int getPos() {
        return source.getPos();
    }

    private GenericExpression<T> parseLastLevel() throws ParseException {
        skipWhiteSpace();

        if (isDigit()) {
            return parseUnsignedConst();
        }

        if (test(Operations.getStrRepr(Operations.MINUS))) {
            if (Character.isDigit(ch)) {
                return parseNegativeConst();
            }
            return parseUnaryElement();
        }

        if (test('(')) {
            GenericExpression<T> lowestLevel = parseAddSub();
            skipWhiteSpace();
            if (!test(')')) {
                throw new InvalidBracketsException(getPos(), "No closing parenthesis");
            }
            return lowestLevel;
        }

        if (Character.isLetter(ch)) {
            if (test('s')) {
                if (test('q') && test('r') && test('t')) {
                    return parseSqrt();
                }
                throw new InvalidCharacterException(getPos(), "invalid character");
            } else if (test('a')) {
                if (test('b') && test('s') && !Character.isLetter(ch)) {
                    return parseAbs();
                }
                throw new InvalidCharacterException(getPos(), "invalid character");
            }
            return parseVariable();
        }

        if (Operations.contains(ch)) {
            throw new InvalidOperandException(getPos(), "No argument for operation");
        }
        if (ch != ')') {
            throw new InvalidCharacterException(getPos(), "Unknown symbol");
        }
        throw new InvalidBracketsException(getPos(), "Empty braces");
    }


    private Variable<T> parseVariable() throws ParseException {
        final StringBuilder sb = new StringBuilder();
        while (Character.isLetter(ch)) {
            sb.append(getNextChar());
        }
        String var = sb.toString();
        if (var.equals("x") || var.equals("y") || var.equals("z")) {
            return new Variable<T>(var);
        }
        throw new InvalidVariableException(getPos(), "Bare var");
    }


    private GenericExpression<T> parseBinary(Operations firstOperation, Operations secondOperation) throws ParseException {

        GenericExpression<T> lhs = parseLowerPriority(firstOperation);

        while (notEndOfInput()) {
            Operations op = Operations.getOperation(ch);
            if (op != firstOperation && op != secondOperation) {
                break;
            }
            nextChar();
            GenericExpression<T> rhs = parseLowerPriority(firstOperation);
            lhs = createOperation(lhs, op, (MathExpression<T>) rhs);
        }
        return lhs;
    }

    private boolean notEndOfInput() {
        boolean result = !test(EOF);
        skipWhiteSpace();
        return result;
    }

    private GenericExpression<T> createOperation(GenericExpression<T> lhs, Operations op, MathExpression<T> rhs) {
        GenericExpression<T> result = null;
        switch (Objects.requireNonNull(op)) {
            case ADD -> result = new Add<T>((MathExpression<T>) lhs, rhs);
            case SUB -> result = new Subtract<T>((MathExpression<T>) lhs, rhs);
            case MUL -> result = new Multiply<T>((MathExpression<T>) lhs, rhs);
            case DIV -> result = new Divide<T>((MathExpression<T>) lhs, rhs);
        }
        return result;
    }

    private GenericExpression<T> parse(Operations operation) throws ParseException {
        return switch (operation) {
            case ADD, SUB -> parseAddSub();
            case MUL, DIV -> parseMulDiv();
            case NOP -> parseLastLevel();
            default -> null;
        };
    }


    private GenericExpression<T> parseConst(StringBuilder value) throws InvalidConstException {

        while (isDigit()) {
            value.append(getNextChar());
        }
        return new Const<T>(Integer.parseInt(value.toString()));

    }

    private void skipWhiteSpace() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }

    private boolean isDigit() {
        return ch >= '0' && ch <= '9';
    }
}