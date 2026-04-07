package exception;

public class NotAvailableSeatsException extends TourAppException {
    public NotAvailableSeatsException(String message) {
        super(message);
    }
}