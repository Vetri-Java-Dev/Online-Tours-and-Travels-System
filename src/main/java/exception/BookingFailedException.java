package exception;

public class BookingFailedException extends TourAppException {
    public BookingFailedException(String message) {
        super(message);
    }
}