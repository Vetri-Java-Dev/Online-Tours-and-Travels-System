package model;

import java.time.LocalDate;

public class Booking {

	private int bookingId;
	private LocalDate bookingDate;
	private int travelers;
	private double totalAmount;
	private String status;
	private int customerId;
	private int packageId;

	public Booking() {
	}

	public Booking(int bookingId, LocalDate bookingDate, int travelers, double totalAmount, String status,
			int customerId, int packageId, String customerName, String packageName) {
		super();
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

	public LocalDate getBookingDate() {
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

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public void setTravelers(int travelers) {
		this.travelers = travelers;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

}