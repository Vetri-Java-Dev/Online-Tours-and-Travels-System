package exception;

public class UserNotFoundException extends TourAppException {
    public UserNotFoundException(String message) {
        super(message);
    }
}