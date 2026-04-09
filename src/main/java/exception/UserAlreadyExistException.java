package exception;

public class UserAlreadyExistException extends TourAppException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}