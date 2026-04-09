package exception;

public class InvalidCredentialsException extends TourAppException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}