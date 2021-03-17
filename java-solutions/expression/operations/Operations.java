package expression.operations;

public enum Operations {
    ADD('+'),
    SUB('-'),
    MUL('*'),
    DIV('/'),
    XOR('^'),
    AND('&'),
    OR('|'),
    NOP('e'),
    MINUS('-');

    private final char value;

    Operations(char s) {
        value = s;
    }

    public static Operations getOperation(char c) {
        for (Operations op : Operations.values()) {
            if (op.value == c) return op;
        }
        return null;
    }

    public static char getStrRepr(Operations operation) {
        return operation.value;
    }

    public static boolean contains(char c) {
        for (Operations op : Operations.values()) {
            if (op.value == c) return true;
        }
        return false;
    }

    public static Operations getLower(Operations operation) {
        return switch (operation) {
            case ADD, SUB -> MUL;
            case MUL, DIV -> NOP;
            default -> null;
        };
    }
}
