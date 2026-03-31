package controller;
import java.util.*;

import model.Booking;
import model.CreditCardPayment;
import model.DebitCardPayment;
import model.Payment;
import model.UPIPayment;
import model.User;
import model.TourPackage;
import model.Itinerary;
import model.ItineraryItem;
import comparator.PriceComparator;
import comparator.DurationComparator;
import service.BookingService;
import service.MessageService;
import service.PaymentService;
import service.TourPackageService;
import service.UserService;
import service.ItineraryService;

import java.time.LocalDate; 
public class CustomerController {

    private Scanner sc = new Scanner(System.in);

    private TourPackageService tourService   = new TourPackageService();
    private BookingService bookingService    = new BookingService();
    private UserService userService          = new UserService();
    private MessageService messageService    = new MessageService();

    private int customerId;

    public CustomerController(int customerId) {
        this.customerId = customerId;
    }

    public CustomerController() {}

    public void customerMenu() {

        while(true) {

        	 	 System.out.println("\n╔══════════════════════════════════════╗");
             System.out.println("║         CUSTOMER DASHBOARD           ║");
             System.out.println("╠══════════════════════════════════════╣");
             System.out.println("║  1.  View Tour Packages              ║");
             System.out.println("║  2.  Search Package                  ║");
             System.out.println("║  3.  View Package Itinerary          ║");
             System.out.println("║  4.  Create Booking                  ║");
             System.out.println("║  5.  View Booking                    ║");
             System.out.println("║  6.  Modify Booking                  ║");
             System.out.println("║  7.  Cancel Booking                  ║");
             System.out.println("║  8.  View Payment History            ║");
             System.out.println("║  9.  View Profile                    ║");
             System.out.println("║  10. Message Admin                   ║");
             System.out.println("║  11. Exit                            ║");
             System.out.println("╚══════════════════════════════════════╝");
             System.out.print("  Enter choice: ");

            int choice = sc.nextInt();

            switch(choice) {

                case 1:
                    tourService.displayPackages();
                    break;

                case 2:
                    searchPackage();
                    break;

                case 3:
                    viewItinerary();
                    break;

                case 4:
                    createBooking();
                    break;

                case 5:
                    viewBooking();
                    break;
                case 6:
                	 modifyBooking();   
                	 break;

                case 7:
                    cancelBooking();
                    break;

                case 8:
                    viewPaymentHistory();
                    break;

                case 9:
                    viewProfile();
                    break;

                case 10:
                    messageMenu();
                    break;

                case 11:
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
   

    private void viewItinerary() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│        VIEW PACKAGE ITINERARY       │");
        System.out.println("└─────────────────────────────────────┘");
        
        System.out.print("  Enter Package ID: ");
        int packageId = sc.nextInt();

        ItineraryService itineraryService = new ItineraryService();
        Itinerary itinerary = itineraryService.viewItinerary(packageId);

        if (itinerary != null) {

            List<ItineraryItem> items = itinerary.getItems();

            System.out.println("\n  Package ID  : " + itinerary.getPackageId());
            System.out.println("  Total Days  : " + items.size());
            System.out.println();
            System.out.println("  ┌──────┬─────────────────┬───────────────────────────────────┐");
            System.out.printf("  │ %-4s │ %-15s │ %-33s │%n", "Day", "Location", "Activity");
            System.out.println("  ├──────┼─────────────────┼───────────────────────────────────┤");

            for (ItineraryItem item : items) {
                System.out.printf("  │ %-4d │ %-15s │ %-33s │%n",
                    item.getDayNumber(),
                    item.getLocation(),
                    item.getActivity());
            }

            System.out.println("  └──────┴─────────────────┴───────────────────────────────────┘");

        }
        else {
            System.out.println("\n  No itinerary found for Package ID: " + packageId);
        }
    }

    private void messageMenu() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           MESSAGE ADMIN             │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  1.  Send Message to Admin");
        System.out.println("  2.  View Replies from Admin");
        System.out.print("  Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {

            case 1:
                System.out.print("  Your message: ");
                String msg = sc.nextLine();
                messageService.sendToAdmin(customerId, msg);
                System.out.println("\n  Message sent to Admin!");
                break;

            case 2:
                List<String> replies = messageService.viewReplies(customerId);
                
                if (replies.isEmpty()) {
                    System.out.println("\n  No replies from admin yet.");
                }
                else {
                	
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│         REPLIES FROM ADMIN          │");
                    System.out.println("└─────────────────────────────────────┘");
                    
                    for (String r : replies) {
                        System.out.println("  Admin : " + r);
                        System.out.println("  ─────────────────────────────────────");
                    }
                }
                break;

            default:
                System.out.println("\n  Invalid choice.");
        }
    }

    public void viewProfile() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           CUSTOMER PROFILE          │");
        System.out.println("└─────────────────────────────────────┘");

        User user = userService.getUserById(customerId);

        if(user != null) {
            System.out.println("  Name  : " + user.getName());
            System.out.println("  Email : " + user.getEmail());
            System.out.println("  Phone : " + user.getPhone());
        }
        else {
            System.out.println("  Profile not found.");
        }
    }

    private void createBooking() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           CREATE BOOKING            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("  Package ID        : ");
        int packageId = sc.nextInt();

        System.out.print("  No. of Travelers  : ");
        int travelers = sc.nextInt();
        sc.nextLine();


        String bookingDate;

        while (true) {

            System.out.print("  Booking Date (YYYY-MM-DD): ");
            bookingDate = sc.nextLine();

            try {
                LocalDate date = LocalDate.parse(bookingDate);
                LocalDate today = LocalDate.now();

                if (date.isBefore(today)) {
                    System.out.println("Date is in the past. Enter again.");
                    continue;
                }

                break;
            } catch (Exception e) {
                System.out.println(" Invalid format. Enter again.");
            }
        }
        Booking booking = new Booking();
        booking.setCustomerId(customerId);
        booking.setPackageId(packageId);
        booking.setTravelers(travelers);
        booking.setBookingDate(bookingDate);

        bookingService.createBooking(booking);

        int bookingId = booking.getBookingId();
        double amount = booking.getTotalAmount();
        String date   = java.time.LocalDate.now().toString();

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           SELECT PAYMENT            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  1.  UPI");
        System.out.println("  2.  Credit Card");
        System.out.println("  3.  Debit Card");
        System.out.print("  Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        Payment payment = null;

        switch(choice) {
            case 1:
                System.out.print("  UPI ID          : ");
                String upiId = sc.nextLine();
                payment = new UPIPayment(amount, date, "SUCCESS", bookingId, upiId);
                break;

            case 2:
                System.out.print("  Card Number     : ");
                String cc = sc.nextLine();
                System.out.print("  Card Holder     : ");
                String name = sc.nextLine();
                payment = new CreditCardPayment(amount, date, "SUCCESS", bookingId, cc, name);
                break;

            case 3:
                System.out.print("  Card Number     : ");
                String cardNumber = sc.nextLine();
                System.out.print("  Bank Name       : ");
                String bank = sc.nextLine();
                payment = new DebitCardPayment(amount, date, "SUCCESS", bookingId, cardNumber, bank);
                break;

            default:
                System.out.println("\n  Invalid payment choice.");
                return;
        }

        PaymentService paymentService = new PaymentService();
        paymentService.processPayment(payment);
    }

    private void viewBooking() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│            VIEW BOOKING             │");
        System.out.println("└─────────────────────────────────────┘");
        
        System.out.print("  Booking ID: ");
        int bookingId = sc.nextInt();

        Booking booking = bookingService.viewBooking(bookingId);

        if (booking != null) {
            System.out.println("\n  ─────────────────────────────────────");
            System.out.println("  Booking ID   : " + booking.getBookingId());
            System.out.println("  Package ID   : " + booking.getPackageId());
            System.out.println("  Travelers    : " + booking.getTravelers());
            System.out.println("  Booking Date : " + booking.getBookingDate());
            System.out.println("  Total Amount : Rs." + booking.getTotalAmount());
            System.out.println("  Status       : " + booking.getStatus());
            System.out.println("  ─────────────────────────────────────");
        }
        
        else {
            System.out.println("\n  Booking not found!");
        }
    }
    private void modifyBooking() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           MODIFY BOOKING            │");
        System.out.println("└─────────────────────────────────────┘");

        System.out.print("  Enter Booking ID: ");
        int bookingId = sc.nextInt();

        Booking booking = bookingService.viewBooking(bookingId);

        if (booking == null) {
            System.out.println("\n  Booking not found!");
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

        booking.setBookingDate(newDate);
        booking.setTravelers(newTravelers);

        bookingService.modifyBooking(booking);

        System.out.println("\n  Booking modified successfully!");
    }

    private void viewPaymentHistory() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│          PAYMENT HISTORY            │");
        System.out.println("└─────────────────────────────────────┘");
        
        System.out.print("  Booking ID: ");
        int bookingId = sc.nextInt();

        PaymentService paymentService = new PaymentService();
        paymentService.viewPaymentHistory(bookingId);
    }

    private void cancelBooking() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           CANCEL BOOKING            │");
        System.out.println("└─────────────────────────────────────┘");
        
        System.out.print("  Booking ID to cancel: ");
        int bookingId = sc.nextInt();

        bookingService.cancelBooking(bookingId);
    }

    public void searchPackage() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           SEARCH PACKAGE            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  1.  Search by Destination");
        System.out.println("  2.  Filter Packages");
        System.out.print("  Enter choice: ");

        int option = sc.nextInt();
        sc.nextLine();

        if (option == 1) {

            System.out.print("  Destination: ");
            String dest = sc.nextLine();

            List<TourPackage> list = tourService.searchByDestination(dest);

            if (list.isEmpty()) {
                System.out.println("\n  No packages found for '" + dest + "'");
            }
            else {
            	
                System.out.println("\n  ┌────────┬──────────────────────┬──────────┬──────────┐");
                System.out.printf("  │ %-6s │ %-20s │ %-8s │ %-8s │%n", "ID", "Destination", "Price", "Duration");
                System.out.println("  ├────────┼──────────────────────┼──────────┼──────────┤");
                
                for (TourPackage tp : list) {
                    System.out.printf("  │ %-6d │ %-20s │ Rs.%-5d │ %-5d days│%n",
                        tp.getPackageId(),
                        tp.getDestination(),
                        tp.getPrice(),
                        tp.getDuration());
                }
                System.out.println("  └────────┴──────────────────────┴──────────┴──────────┘");
            }

        } else if (option == 2) {

            System.out.println("\n  1.  Sort by Price");
            System.out.println("  2.  Sort by Duration");
            System.out.print("  Enter choice: ");

            int filterChoice = sc.nextInt();
            sc.nextLine();

            List<TourPackage> list = tourService.getAllPackages();

            if (filterChoice == 1) {
                Collections.sort(list, new PriceComparator());
            }
            else if (filterChoice == 2) {
                Collections.sort(list, new DurationComparator());
            }

            System.out.println("\n  ┌────────┬──────────────────────┬──────────┬──────────┐");
            System.out.printf("  │ %-6s │ %-20s │ %-8s │ %-8s │%n", "ID", "Destination", "Price", "Duration");
            System.out.println("  ├────────┼──────────────────────┼──────────┼──────────┤");
            
            for (TourPackage tp : list) {
                System.out.printf("  │ %-6d │ %-20s │ Rs.%-5d │ %-5d days│%n",
                    tp.getPackageId(),
                    tp.getDestination(),
                    tp.getPrice(),
                    tp.getDuration());
            }
            System.out.println("  └────────┴──────────────────────┴──────────┴──────────┘");

        }
        else {
            System.out.println("\n  Invalid choice.");
        }
    }
}