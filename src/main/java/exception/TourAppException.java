package exception;

public class TourAppException extends RuntimeException {

    public TourAppException(String message) {
        super(message);
    }

    public TourAppException(String message, Throwable cause) {
        super(message, cause);
    }
}