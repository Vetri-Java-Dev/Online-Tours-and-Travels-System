package model;

import java.time.LocalDate;

public class Booking {

	private int bookingId;
	private LocalDate bookingDate;
	private int travelers;
	private double totalAmount;
	private String status;
	private String packageName;
	private int customerId;
	private int packageId;

	public Booking() {
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
	public String getPackageName() {
	    return packageName;
	}

	public void setPackageName(String packageName) {
	    this.packageName = packageName;
	}
	public int getCustomerId() {
	    return customerId;
	}

	public void setCustomerId(int customerId) {
	    this.customerId = customerId;
	}

	public int getPackageId() {
	    return packageId;
	}

	public void setPackageId(int packageId) {
	    this.packageId = packageId;
	}



}