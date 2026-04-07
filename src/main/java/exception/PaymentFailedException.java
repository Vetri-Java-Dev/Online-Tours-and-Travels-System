package exception;

public class PaymentFailedException extends TourAppException {
    public PaymentFailedException(String message) {
        super(message);
    }
}