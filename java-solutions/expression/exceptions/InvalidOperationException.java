package expression.exceptions;

public class InvalidOperationException extends ParseException {
    public InvalidOperationException(int pos, String message) {
        super(pos, message);
    }
}
