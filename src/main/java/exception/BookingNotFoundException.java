package exception;

public class BookingNotFoundException extends TourAppException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}