package model;

public class Booking {

    private int bookingId;
    private String bookingDate;
    private int travelers;
    private double totalAmount;
    private String status;
    private int customerId;
    private int packageId;

    public Booking(int bookingId, String bookingDate, int travelers, double totalAmount,
                   String status, int customerId, int packageId) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.travelers = travelers;
        this.totalAmount = totalAmount;
        this.status = status;
        this.customerId = customerId;
        this.packageId = packageId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public int getTravelers() {
        return travelers;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}