package exception;

public class PackageNotFoundException extends TourAppException {
    public PackageNotFoundException(String message) {
        super(message);
    }
}