package controller;

import service.MessageService;
import java.util.*;

public class MessageController {

	static Scanner sc = new Scanner(System.in);
	static MessageService service = new MessageService();

	public static void main(String[] args) {

		while (true) {
			System.out.println("\n===== MAIN MENU =====");
			System.out.println("1. Customer");
			System.out.println("2. Admin");
			System.out.println("3. Exit");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				customerMenu();
				break;
			case 2:
				adminMenu();
				break;
			case 3:
				System.exit(0);
			}
		}
	}

	// CUSTOMER MENU
	static void customerMenu() {
		System.out.print("Enter Customer ID: ");
		int id = sc.nextInt();
		sc.nextLine();

		System.out.println("1. Send Message");
		System.out.println("2. View Replies");

		int ch = sc.nextInt();
		sc.nextLine();

		if (ch == 1) {
			System.out.print("Enter Message: ");
			String msg = sc.nextLine();
			service.sendToAdmin(id, msg);

		}
		else {
			List<String> replies = service.viewReplies(id);

			if (replies.isEmpty()) {
				System.out.println("No replies yet.");
			} else {
				for (String r : replies) {
					System.out.println("Admin: " + r);
				}
			}
		}
	}

	// ADMIN MENU
	static void adminMenu() {
		System.out.println("1. View Messages");
		System.out.println("2. Reply");

		int ch = sc.nextInt();
		sc.nextLine();

		if (ch == 1) {
			List<String> msgs = service.viewMessages();

			if (msgs.isEmpty()) {
				System.out.println("No new messages.");
			}
			else {
				for (String m : msgs) {
					System.out.println(m);
				}
			}

		} else {
			System.out.print("Enter Customer ID: ");
			int cid = sc.nextInt();
			sc.nextLine();

			System.out.print("Enter Reply: ");
			String reply = sc.nextLine();

			service.replyToCustomer(cid, reply);
		}
	}
}
