package exception;

public class InvalidBookingException extends TourAppException {
    public InvalidBookingException(String message) {
        super(message);
    }
}