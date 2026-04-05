package model;

public class Feedback {

	private int feedbackId;
	private int bookingId;
	private int customerId;
	private int packageId;

	private int rating;
	private String title;
	private String description;

	private String status;
	private String feedbackDate;
	private String createdAt;
	private String updatedAt;

	private String customerName;
	private String destination;

	public Feedback() {
	}

	public Feedback(int bookingId, int customerId, int packageId, int rating, String title, String description) {
		this.bookingId = bookingId;
		this.customerId = customerId;
		this.packageId = packageId;
		this.rating = rating;
		this.title = title;
		this.description = description;
		this.status = "pending";
	}

	/** Returns e.g. "★★★★☆ (4/5)" */
	public String getStarDisplay() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 5; i++) {
			sb.append(i <= rating ? "★" : "☆");
		}
		sb.append("  (").append(rating).append("/5)");
		return sb.toString();
	}


	public int getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
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

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFeedbackDate() {
		return feedbackDate;
	}

	public void setFeedbackDate(String feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
}
