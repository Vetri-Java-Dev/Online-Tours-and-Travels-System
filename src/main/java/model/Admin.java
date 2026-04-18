/*
 * Author         : Vetrivel B
 * Description    : Entity class representing a Admin user with preferences, contact details and booking history.
 * Module         : Admin Module
 * Java Version   : 24
 */
package model;

public class Admin extends User {
	public Admin(int userId, String name, String email, String password, String phone, String role) {
		super(userId, name, email, password, phone, role);
	}

	public Admin() {
		super();
	}

}
