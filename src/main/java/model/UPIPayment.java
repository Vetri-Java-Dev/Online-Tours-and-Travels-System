package model;


public class UPIPayment extends Payment {

    private String upiId;

    // Constructor used when retrieving from database
    public UPIPayment(int paymentId, double amount, String paymentDate, String status, int bookingId,
                      String upiId) {

        super(paymentId, amount, paymentDate, status, bookingId, "UPI");
        this.upiId = upiId;
    }

    // Constructor used when creating new payment
    public UPIPayment(double amount, String paymentDate, String status, int bookingId,
                      String upiId) {

        super(amount, paymentDate, status, bookingId, "UPI");
        this.upiId = upiId;
    }

    public String getUpiId() {
        return upiId;
    }
}
