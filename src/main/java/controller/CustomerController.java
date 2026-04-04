package controller;

import java.util.*;

import model.*;
import comparator.PriceComparator;
import comparator.DurationComparator;
import service.*;
import util.ColorText;

import java.time.LocalDate;

public class CustomerController {

    private Scanner sc = new Scanner(System.in);

    private TourPackageService tourService = new TourPackageService();
    private BookingService bookingService = new BookingService();
    private UserService userService = new UserService();
    private MessageService messageService = new MessageService();

    private int customerId;

    public CustomerController(int customerId) {
        this.customerId = customerId;
    }


    public void customerMenu() {

        while(true) {
        	System.out.println(ColorText.warning("╔══════════════════════════════════════╗"));

        	System.out.println(ColorText.warning("║") 
        	        + ColorText.bold("         CUSTOMER DASHBOARD           ") 
        	        + ColorText.warning("║"));

        	System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));

        	System.out.println(ColorText.warning("║") + "  1.  View Tour Packages              " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  2.  Search Package                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  3.  View Package Itinerary          " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  4.  Manage Booking                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  5.  View Payment History            " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  6.  View Profile                    " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 7.  Update Profile                   " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 8.  Message Admin                    " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 9.  Delete Account                   " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 10.  Exit                            " + ColorText.warning("║"));

        	System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        	System.out.print(ColorText.bold("Enter choice: "));
        	int choice = sc.nextInt();

            switch(choice) {

            case 1: tourService.displayPackages(); break;
            case 2: searchPackage(); break;
            case 3: viewItinerary(); break;
            case 4: manageBookingMenu(); break;
            case 5: viewPaymentHistory(); break;
            case 6: viewProfile(); break;
            case 7: updateProfile(); break;
            case 8: sc.nextLine(); messageMenu(); break;
            case 9: deleteAccount(); return;
            case 10: 
                System.out.println("Exiting...");
                return;

            default:
                System.out.println("Invalid choice!");
        } 
       }
    }
    
    private void manageBookingMenu() {

        while (true) {
        	System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        	System.out.println(ColorText.warning("│") 
        	        + ColorText.bold("          MANAGE BOOKING             ") 
        	        + ColorText.warning("│"));
        	System.out.println(ColorText.warning("├─────────────────────────────────────┤"));

        	System.out.println(ColorText.warning("│") + "  1. Create Booking                  " + ColorText.warning("│"));
        	System.out.println(ColorText.warning("│") + "  2. View Booking                    " + ColorText.warning("│"));
        	System.out.println(ColorText.warning("│") + "  3. Modify Booking                  " + ColorText.warning("│"));
        	System.out.println(ColorText.warning("│") + "  4. Cancel Booking                  " + ColorText.warning("│"));
        	System.out.println(ColorText.warning("│") + "  5. Booking History                 " + ColorText.warning("│"));
        	System.out.println(ColorText.warning("│") + "  6. Back                            " + ColorText.warning("│"));

        	System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        	System.out.print(ColorText.bold("Enter choice: "));
        	int choice = sc.nextInt();

            switch (choice) {

                case 1: createBooking(); break;
                case 2: viewBooking(); break;
                case 3: modifyBooking(); break;
                case 4: cancelBooking(); break;
                case 5: viewBookingHistory(); break;

                case 6:
                    return;

                default:
                    System.out.println(ColorText.error("Invalid choice!"));
            }
        }
    }
    

    // ================= BOOKING =================
    private void createBooking() {

        System.out.print("Package ID: ");
        int packageId = sc.nextInt();

        System.out.print("Travelers: ");
        int travelers = sc.nextInt();
        sc.nextLine();
        LocalDate date=null;
        String bookingDate;

        while (true) {
            System.out.print("Booking Date (YYYY-MM-DD): ");
            bookingDate = sc.nextLine();

            try {
                date = LocalDate.parse(bookingDate);
                LocalDate today = LocalDate.now();

                if (date.isBefore(today)) {
                    System.out.println("Date is in the past. Enter again.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid format.");
            }
        }

        Booking booking = new Booking();
        booking.setCustomerId(customerId);
        booking.setPackageId(packageId);
        booking.setTravelers(travelers);
        booking.setBookingDate(date);
        bookingService.createBooking(booking);
        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") 
                + ColorText.bold("        BOOKING CONFIRMATION         ") 
                + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.println("  Booking ID   : " + booking.getBookingId());
        System.out.println("  Package ID   : " + booking.getPackageId());
        System.out.println("  Travelers    : " + booking.getTravelers());
        System.out.printf ("  Total Amount : Rs. %.2f%n", booking.getTotalAmount());
        System.out.println("  Status       : " + booking.getStatus());
        System.out.println("  ─────────────────────────────────────");

        int bookingId = booking.getBookingId();
        double amount = booking.getTotalAmount();
      
        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));

        System.out.println(ColorText.warning("│") 
                + ColorText.bold("           SELECT PAYMENT            ") 
                + ColorText.warning("│"));

        System.out.println(ColorText.warning("├─────────────────────────────────────┤"));

        System.out.println(ColorText.warning("│") + "  1. UPI                              " + ColorText.warning("│"));
        System.out.println(ColorText.warning("│") + "  2. Credit Card                      " + ColorText.warning("│"));
        System.out.println(ColorText.warning("│") + "  3. Debit Card                       " + ColorText.warning("│"));

        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print(ColorText.bold("Enter choice: "));
        int choice = sc.nextInt();
        sc.nextLine();

        Payment payment = null;
        String today = LocalDate.now().toString();

        switch (choice) {
            case 1:
                System.out.print("UPI ID: ");
                payment = new UPIPayment(amount, today, "SUCCESS", bookingId, sc.nextLine());
                break;
            case 2:
                System.out.print("Card No: ");
                String cc = sc.nextLine();
                System.out.print("Holder: ");
                payment = new CreditCardPayment(amount, today, "SUCCESS", bookingId, cc, sc.nextLine());
                break;
            case 3:
                System.out.print("Card No: ");
                String dc = sc.nextLine();
                System.out.print("Bank: ");
                payment = new DebitCardPayment(amount, today, "SUCCESS", bookingId, dc, sc.nextLine());
                break;
        }

        new PaymentService().processPayment(payment);
    }

    private void viewBooking() {
    	System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
    	System.out.println(ColorText.warning("│") 
    	        + ColorText.bold("            VIEW BOOKING             ") 
    	        + ColorText.warning("│"));
    	System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

    	System.out.print("Enter Booking ID: ");
    	int bookingId = sc.nextInt();
    	sc.nextLine(); 

    	Booking booking = bookingService.viewBooking(bookingId);

    	if (booking != null) {
    	    System.out.println("  ─────────────────────────────────────");
    	    System.out.println("  Booking ID   : " + booking.getBookingId());
    	    System.out.println("  Package ID   : " + booking.getPackageId());
    	    System.out.println("  Travelers    : " + booking.getTravelers());
    	    System.out.println("  Booking Date : " + booking.getBookingDate());
    	    System.out.printf ("  Total Amount : Rs. %.2f%n", booking.getTotalAmount());
    	    System.out.println("  Status       : " + booking.getStatus());
    	    System.out.println("  ─────────────────────────────────────");
    	} else {
    	    System.out.println(ColorText.error("Booking not found!"));
    	}
    }
    private void modifyBooking() {

    	System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));

    	System.out.println(ColorText.warning("│") 
    	        + ColorText.bold("           MODIFY BOOKING             ") 
    	        + ColorText.warning("│"));

    	System.out.println(ColorText.warning("├─────────────────────────────────────┤"));

    	System.out.println(ColorText.warning("│") + "  Enter Booking ID:                  " + ColorText.warning("│"));

    	System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

    	System.out.print(ColorText.bold("Enter Booking ID: "));
    	int bookingId = sc.nextInt();

        Booking booking = bookingService.viewBooking(bookingId);

        if (booking == null) {
        	System.out.println(ColorText.error("Booking not found!"));
        	return;
        }

        if (booking.getStatus().equalsIgnoreCase("Cancelled")) {
            System.out.println("\n  Cannot modify a cancelled booking!");
            return;
        }

        System.out.println("\n  Current Details:");
        System.out.println("  Date       : " + booking.getBookingDate());
        System.out.println("  Travelers  : " + booking.getTravelers());

        sc.nextLine(); 

        String newDate;

        while (true) {

            System.out.print("\n  Enter New Booking Date (YYYY-MM-DD): ");
            newDate = sc.nextLine();

            try {
                LocalDate date = LocalDate.parse(newDate);
                LocalDate today = LocalDate.now();

                if (date.isBefore(today)) {
                    System.out.println(" Date is in the past. Enter again.");
                    continue;
                }

                break;
            } catch (Exception e) {
                System.out.println("Invalid format. Enter again.");
            }
        }
        System.out.print("  Enter New Travelers: ");
        int newTravelers = sc.nextInt();
        LocalDate date = LocalDate.parse(newDate);
        booking.setBookingDate(date);
        booking.setTravelers(newTravelers);

        bookingService.modifyBooking(booking);

        System.out.println(ColorText.success("Booking modified successfully!"));
    }

    private void cancelBooking() {

    	System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
    	System.out.println(ColorText.warning("│") 
    	        + ColorText.bold("           CANCEL BOOKING            ") 
    	        + ColorText.warning("│"));
    	System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

    	System.out.print(ColorText.bold("Enter Booking ID: "));
    	int bookingId = sc.nextInt();

    	boolean result = bookingService.cancelBooking(bookingId);

    	if (result) {
    	    System.out.println(ColorText.success("Booking cancelled successfully!"));
    	} else {
    	    System.out.println(ColorText.error("Invalid Booking ID!"));
    	}
    } private void viewPaymentHistory() {
    	    System.out.print("Booking ID: ");
    	    int bookingId = sc.nextInt();
    	    new PaymentService().viewPaymentHistory(bookingId);
    	}

        // ================= PROFILE =================
    public void viewProfile() {
        User u = userService.getUserById(customerId);
        System.out.println("Name: " + u.getName());
        System.out.println("Email: " + u.getEmail());
        System.out.println("Phone: " + u.getPhone());
    }

    public void updateProfile() {

        sc.nextLine();
        System.out.print("New Name: ");
        String name = sc.nextLine();

        System.out.print("New Phone: ");
        String phone = sc.nextLine();

        userService.updateUser(customerId, name, phone);
        System.out.println("Updated!");
    }

    public void deleteAccount() {
        sc.nextLine();
        System.out.print("Confirm delete (yes/no): ");
        if (sc.nextLine().equalsIgnoreCase("yes")) {
            userService.deleteUser(customerId);
            System.out.println("Deleted.");
        }
    }

    // ================= HISTORY =================
    public void viewBookingHistory() {

        List<Booking> list = bookingService.getBookingsByCustomerId(customerId);

        for (Booking b : list) {
            System.out.println(b.getBookingId() + " | " + b.getStatus());
        }
    }

    // ================= SEARCH =================
    public void searchPackage() {

        System.out.println("1. Destination\n2. Sort");
        int opt = sc.nextInt();
        sc.nextLine();

        List<TourPackage> list = tourService.getAllPackages();

        if (opt == 1) {
            System.out.print("Destination: ");
            list = tourService.searchByDestination(sc.nextLine());
        } else {
            System.out.println("1.Price 2.Duration");
            if (sc.nextInt() == 1)
                Collections.sort(list, new PriceComparator());
            else
                Collections.sort(list, new DurationComparator());
        }

        for (TourPackage t : list) {
            System.out.println(t.getDestination() + " - " + t.getPrice());
        }
    }

    // ================= OTHER =================
    private void viewItinerary() {
        System.out.print("Package ID: ");
        new ItineraryService().viewItinerary(sc.nextInt());
    }

    // ================= MESSAGE =================
    private void messageMenu() {

        while (true) {

            System.out.println("\n1.Send\n2.View Replies\n3.Back");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1:
                    System.out.print("Message: ");
                    messageService.sendToAdmin(customerId, sc.nextLine());
                    break;
                case 2:
                    for (String r : messageService.viewReplies(customerId))
                        System.out.println(r);
                    break;
                case 3:
                    return;
            }
        }
    }
}