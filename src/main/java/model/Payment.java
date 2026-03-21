package model;

public class Payment {

    private int paymentId;
    private double amount;
    private String paymentDate;
    private String status;
    private int bookingId;
    private String paymentMethod; 

    public Payment() {}
    
    public Payment(int paymentId, double amount, String paymentDate, String status, int bookingId, String paymentMethod) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
    }

    public Payment(double amount, String paymentDate, String status, int bookingId, String paymentMethod) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
    }

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}