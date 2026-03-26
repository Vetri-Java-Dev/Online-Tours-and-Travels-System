package model;

import java.util.List;

public class Customer extends User{
	
	private String preference;
	private String contactDetail;
	private List<Integer> bookingHistory;
	
	public Customer(int userId, String name, String email, String password, String phone, String role,
			String preference, String contactDetail, List<Integer> bookingHistory) {
		super(userId, name, email, password, phone, role);
		this.preference = preference;
		this.contactDetail = contactDetail;
		this.bookingHistory = bookingHistory;
	}
	
	
	

}
