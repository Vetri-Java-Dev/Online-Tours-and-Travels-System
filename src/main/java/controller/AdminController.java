package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Booking;
import model.Itinerary;
import model.ItineraryItem;
import model.User;
import service.*;
import util.ColorText;

public class AdminController {

    Scanner sc = new Scanner(System.in);

    TourPackageService service        = new TourPackageService();
    MessageService messageService     = new MessageService();
    BookingService bookingService     = new BookingService();
    PaymentService paymentService     = new PaymentService();
    ItineraryService itineraryService = new ItineraryService();
    UserService userService           = new UserService();
    ReportService reportService       = new ReportService();

    public void adminMenu() {


        while (true) {

        	System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));

        	System.out.println(ColorText.warning("║") 
        	        + ColorText.bold("           ADMIN DASHBOARD            ") 
        	        + ColorText.warning("║"));

        	System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));

        	System.out.println(ColorText.warning("║") + "  1. Manage Tour Packages             " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  2. Message Customer                 " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  3. Manage Booking                   " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  4. View Payment History             " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  5. Manage Itinerary                 " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  6. Track All Bookings               " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  7. Reports                          " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  8. Exit                             " + ColorText.warning("║"));

        	System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        	System.out.print(ColorText.bold("Enter choice: "));

            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                case 1: packageMenu(); break;
                case 2: messageMenu(); break;
                case 3: bookingMenu(); break;
                case 4:
                    System.out.print(ColorText.bold("  Enter Booking ID: "));
                    paymentService.viewPaymentHistory(Integer.parseInt(sc.nextLine()));
                    break;
                case 5: itineraryMenu(); break;
                case 6: trackAllBookings(); break;
                case 7: reportsMenu();   break; 
                case 8:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println(ColorText.error("\n  Invalid choice."));
            }
        }
    }

    // ================= PACKAGE MENU =================
    private void packageMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
            System.out.println(ColorText.warning("│") + ColorText.bold("         MANAGE TOUR PACKAGES        ") + ColorText.warning("│"));
            System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
            System.out.println("  1. Add Package");
            System.out.println("  2. View Packages");
            System.out.println("  3. Update Package");
            System.out.println("  4. Delete Package");
            System.out.println("  5. Back");
            System.out.print(ColorText.bold("  Enter choice: "));

            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {

                case 1:
                    System.out.print("  Package ID   : ");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.print("  Destination  : ");
                    String dest = sc.nextLine();

                    System.out.print("  Price (INR)  : ");
                    int price = Integer.parseInt(sc.nextLine());

                    System.out.print("  Duration     : ");
                    int duration = Integer.parseInt(sc.nextLine());

                    System.out.print("  Seats        : ");
                    int seats = Integer.parseInt(sc.nextLine());

                    service.createPackage(id, dest, price, duration, seats);
                    System.out.println(ColorText.success("\n  Package added successfully!"));
                    break;

                case 2:
                    service.displayPackages();
                    break;

                case 3:
                    System.out.print("  Package ID      : ");
                    int uid = Integer.parseInt(sc.nextLine());

                    System.out.print("  New Destination : ");
                    String nd = sc.nextLine();

                    System.out.print("  New Price       : ");
                    double np = Double.parseDouble(sc.nextLine());

                    System.out.print("  New Duration    : ");
                    int ndur = Integer.parseInt(sc.nextLine());

                    service.updatePackage(uid, nd, np, ndur);
                    System.out.println(ColorText.success("\n  Updated successfully!"));
                    break;

                case 4:
                    System.out.print("  Package ID : ");
                    int deleteId = Integer.parseInt(sc.nextLine());

                    System.out.print("  Confirm delete? (yes/no): ");
                    if(sc.nextLine().equalsIgnoreCase("yes")) {
                        service.deletePackage(deleteId);
                        System.out.println(ColorText.success("\n  Deleted successfully!"));
                    }
                    break;

                case 5: return;

                default:
                    System.out.println(ColorText.error("\n  Invalid choice."));
            }
        }
    }

    // ================= MESSAGE MENU =================
    private void messageMenu() {

        while(true) {

        	System.out.println(ColorText.warning("\n╔═════════════════════════════════════╗"));

        	System.out.println(ColorText.warning("║") 
        	        + ColorText.bold("          MESSAGE CENTER             ") 
        	        + ColorText.warning("║"));

        	System.out.println(ColorText.warning("╠═════════════════════════════════════╣"));

        	System.out.println(ColorText.warning("║") + "  1. View Messages                   " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  2. Reply to Customer               " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  3. Back                            " + ColorText.warning("║"));

        	System.out.println(ColorText.warning("╚═════════════════════════════════════╝"));

        	System.out.print(ColorText.bold("Enter choice: "));
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {

                case 1:
                    List<String> messages = messageService.viewMessages();
                    if(messages.isEmpty()) {
                        System.out.println("  No messages.");
                    } else {
                        messages.forEach(System.out::println);
                    }
                    break;

                case 2:
                    List<User> users = userService.getAllUsers();
                    users.forEach(u -> System.out.println(u.getUserId()+" - "+u.getName()));

                    System.out.print("  Enter Customer ID: ");
                    int cid = Integer.parseInt(sc.nextLine());

                    System.out.print("  Message: ");
                    messageService.replyToCustomer(cid, sc.nextLine());
                    System.out.println(ColorText.success("  Reply sent!"));
                    break;

                case 3: return;
                default: System.out.println("Invalid choice");
            }
        }
    }

    // ================= BOOKING MENU =================
    private void bookingMenu() {
    	    while(true) {

    	        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));

    	        System.out.println(ColorText.warning("║") 
    	                + ColorText.bold("           MANAGE BOOKING             ") 
    	                + ColorText.warning("║"));

    	        System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));

    	        System.out.println(ColorText.warning("║") + "  1. View Booking                     " + ColorText.warning("║"));
    	        System.out.println(ColorText.warning("║") + "  2. Cancel Booking                   " + ColorText.warning("║"));
    	        System.out.println(ColorText.warning("║") + "  3. Back                             " + ColorText.warning("║"));

    	        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

    	        System.out.print(ColorText.bold("Enter choice: "));
    	        int c = Integer.parseInt(sc.nextLine());

    	        if(c == 1) {
    	            System.out.print(ColorText.bold("Enter Booking ID: "));
    	            bookingService.viewBooking(Integer.parseInt(sc.nextLine()));
    	        }
    	        else if(c == 2) {
    	            System.out.print(ColorText.bold("Enter Booking ID: "));
    	            bookingService.cancelBooking(Integer.parseInt(sc.nextLine()));
    	        }
    	        else if(c == 3) {
    	            return;
    	        }
    	        else {
    	            System.out.println(ColorText.error("Invalid choice!"));
    	        }
    	    }
    	}
    private void trackAllBookings() {

        System.out.println("\n===== TRACK PACKAGE BOOKINGS =====");

        List<Booking> list = bookingService.getAllBookings();

        if (list.isEmpty()) {
            System.out.println("No bookings found");
            return;
        }

        System.out.println("ID | Customer | Package | Travel Date | Status");
        System.out.println("-----------------------------------------------------");

        for (Booking b : list) {
            System.out.println(
                    b.getBookingId() + " | " +
                    b.getCustomerName() + " | " +
                    b.getPackageName() + " | " +
                    b.getBookingDate() + " | " +
                    b.getStatus()
            );
        }

        System.out.print("\nEnter Booking ID to view details: ");
        int id = Integer.parseInt(sc.nextLine());

        bookingService.viewBooking(id);
    }
      // ================= ITINERARY MENU =================
    private void itineraryMenu() {
        while(true) {
            System.out.println("\n1.Add 2.View 3.Modify 4.Delete 5.Back");
            int c = Integer.parseInt(sc.nextLine());

            switch(c) {
                case 1: addItinerary(); break;
                case 2: viewItinerary(); break;
                case 3: addItinerary(); break;
                case 4: itineraryService.deleteItinerary(Integer.parseInt(sc.nextLine())); break;
                case 5: return;
            }
        }
    }

    private void addItinerary() {
    	
        int pid = Integer.parseInt(sc.nextLine());
        int days = Integer.parseInt(sc.nextLine());

        List<ItineraryItem> items = new ArrayList<>();

        for(int i=1;i<=days;i++) {
            items.add(new ItineraryItem(0,i,sc.nextLine(),sc.nextLine()));
        }

        itineraryService.createItinerary(new Itinerary(0,pid,1,items));
    }

    private void viewItinerary() {
        Itinerary it = itineraryService.viewItinerary(Integer.parseInt(sc.nextLine()));
        if(it!=null) it.getItems().forEach(i->System.out.println(i.getDayNumber()+" "+i.getLocation()));
    }

    // ================= REPORT MENU =================
    private void reportsMenu() {

        while(true) {
            System.out.println("\n1.All\n2.Confirmed\n3.Cancelled\n4.Payment\n5.Availability\n6.Back");
            int c = Integer.parseInt(sc.nextLine());

            switch(c) {
                case 1: reportService.showAllBookingsReport(); break;
                case 2: reportService.showBookingReportByStatus("CONFIRMED"); break;
                case 3: reportService.showBookingReportByStatus("Cancelled"); break;
                case 4: reportService.showPaymentReport(); break;
                case 5: reportService.showPackageAvailabilityReport(); break;
                case 6: return;
            }
        }
    }
}