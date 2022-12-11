package exceptions;

public class QueryException extends RuntimeException {
    public QueryException(final String message) {
        super(message);
    }

    public QueryException(final String message, final Throwable innerException) {
        super(message, innerException);
    }
}
