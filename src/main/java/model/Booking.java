/*
 * Author         : Harini R G
 * Description    : Booking is a model class that represents booking details 
 *                  such as booking ID, date, travelers, total amount, status, 
 *                  package details, and customer information. It is used to 
 *                  transfer data between different layers of the application.
 * Module         : Booking Module (Model Layer)
 * Java version   : 25
 */
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
	public Booking(int bookingId, LocalDate bookingDate, int travelers, double totalAmount,
			String status, String packageName, int customerId, int packageId) {
		this.bookingId = bookingId;
		this.bookingDate = bookingDate;
		this.travelers = travelers;
		this.totalAmount = totalAmount;
		this.status = status;
		this.packageName = packageName;
		this.customerId = customerId;
		this.packageId = packageId;
	}
	
	public int getBookingId() {
		return bookingId;
	}
	
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public LocalDate getBookingDate() {
		return bookingDate;
	}
	
	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public int getTravelers() {
		return travelers;
	}
	
	public void setTravelers(int travelers) {
		this.travelers = travelers;
	}


	public double getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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