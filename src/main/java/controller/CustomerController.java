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
import util.ColorText;

public class CustomerController {

    private Scanner sc = new Scanner(System.in);

    private TourPackageService tourService = new TourPackageService();
    private BookingService bookingService  = new BookingService();
    private UserService userService        = new UserService();
    private MessageService messageService  = new MessageService();

    private int customerId;

    public CustomerController(int customerId) { this.customerId = customerId; }
    public CustomerController() {}

    public void customerMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("         CUSTOMER DASHBOARD           ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1.  View Tour Packages              " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2.  Search Package                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3.  View Package Itinerary          " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4.  Create Booking                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5.  View Booking                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  6.  Cancel Booking                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  7.  View Payment History            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  8.  View Profile                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  9.  Message Admin                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  10. Exit                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));

            int choice = sc.nextInt();

            switch(choice) {
                case 1:  tourService.displayPackages(); break;
                case 2:  searchPackage();               break;
                case 3:  viewItinerary();               break;
                case 4:  createBooking();               break;
                case 5:  viewBooking();                 break;
                case 6:  cancelBooking();               break;
                case 7:  viewPaymentHistory();          break;
                case 8:  viewProfile();                 break;
                case 9:  messageMenu();                 break;
                case 10:
                    System.out.println(ColorText.yellow("\n  Logging out..."));
                    return;
                default:
                    System.out.println(ColorText.error("\n  Invalid choice. Please enter 1-10."));
            }
        }
    }

    private void viewItinerary() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("        VIEW PACKAGE ITINERARY       ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print(ColorText.bold("  Enter Package ID: "));
        int packageId = sc.nextInt();

        ItineraryService itineraryService = new ItineraryService();
        Itinerary itinerary = itineraryService.viewItinerary(packageId);

        if(itinerary != null) {
            List<ItineraryItem> items = itinerary.getItems();
            System.out.println("\n  Package ID  : " + itinerary.getPackageId());
            System.out.println("  Total Days  : " + items.size());
            System.out.println();
            System.out.println(ColorText.bold("  ┌──────┬─────────────────┬───────────────────────────────────┐"));
            System.out.printf(ColorText.bold("  │ %-4s │ %-15s │ %-33s │%n"), "Day", "Location", "Activity");
            System.out.println(ColorText.bold("  ├──────┼─────────────────┼───────────────────────────────────┤"));
            for(ItineraryItem item : items) {
                System.out.printf("  │ %-4d │ %-15s │ %-33s │%n",
                    item.getDayNumber(), item.getLocation(), item.getActivity());
            }
            System.out.println(ColorText.bold("  └──────┴─────────────────┴───────────────────────────────────┘"));
        } else {
            System.out.println(ColorText.error("\n  No itinerary found for Package ID: " + packageId));
        }
    }

    private void messageMenu() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           MESSAGE ADMIN             ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
        System.out.println("  1.  Send Message to Admin");
        System.out.println("  2.  View Replies from Admin");
        System.out.print(ColorText.bold("  Enter choice: "));

        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {
            case 1:
                System.out.print("  Your message: ");
                String msg = sc.nextLine();
                messageService.sendToAdmin(customerId, msg);
                System.out.println(ColorText.success("\n  Message sent to Admin!"));
                break;

            case 2:
                List<String> replies = messageService.viewReplies(customerId);
                if(replies.isEmpty()) {
                    System.out.println(ColorText.yellow("\n  No replies from admin yet."));
                } else {
                    System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
                    System.out.println(ColorText.warning("│") + ColorText.bold("         REPLIES FROM ADMIN          ") + ColorText.warning("│"));
                    System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
                    for(String r : replies) {
                        System.out.println("  " + ColorText.green("Admin") + " : " + r);
                        System.out.println(ColorText.warning("  ─────────────────────────────────────"));
                    }
                }
                break;

            default:
                System.out.println(ColorText.error("\n  Invalid choice."));
        }
    }

    public void viewProfile() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           CUSTOMER PROFILE          ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        User user = userService.getUserById(customerId);

        if(user != null) {
            System.out.println("  Name  : " + ColorText.bold(user.getName()));
            System.out.println("  Email : " + user.getEmail());
            System.out.println("  Phone : " + user.getPhone());
        } else {
            System.out.println(ColorText.error("  Profile not found."));
        }
    }

    private void createBooking() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           CREATE BOOKING            ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print("  Package ID        : ");
        int packageId = sc.nextInt();

        System.out.print("  No. of Travelers  : ");
        int travelers = sc.nextInt();
        sc.nextLine();

        System.out.print("  Booking Date      : ");
        String bookingDate = sc.nextLine();

        Booking booking = new Booking();
        booking.setCustomerId(customerId);
        booking.setPackageId(packageId);
        booking.setTravelers(travelers);
        booking.setBookingDate(bookingDate);

        bookingService.createBooking(booking);

        int bookingId = booking.getBookingId();
        double amount = booking.getTotalAmount();
        String date   = java.time.LocalDate.now().toString();

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           SELECT PAYMENT            ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
        System.out.println("  1.  UPI");
        System.out.println("  2.  Credit Card");
        System.out.println("  3.  Debit Card");
        System.out.print(ColorText.bold("  Enter choice: "));

        int choice = sc.nextInt();
        sc.nextLine();

        Payment payment = null;

        switch(choice) {
            case 1:
                System.out.print("  UPI ID      : ");
                payment = new UPIPayment(amount, date, "SUCCESS", bookingId, sc.nextLine());
                break;
            case 2:
                System.out.print("  Card Number : ");
                String cc = sc.nextLine();
                System.out.print("  Card Holder : ");
                payment = new CreditCardPayment(amount, date, "SUCCESS", bookingId, cc, sc.nextLine());
                break;
            case 3:
                System.out.print("  Card Number : ");
                String cn = sc.nextLine();
                System.out.print("  Bank Name   : ");
                payment = new DebitCardPayment(amount, date, "SUCCESS", bookingId, cn, sc.nextLine());
                break;
            default:
                System.out.println(ColorText.error("\n  Invalid payment choice."));
                return;
        }

        new PaymentService().processPayment(payment);
    }

    private void viewBooking() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("            VIEW BOOKING             ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print(ColorText.bold("  Booking ID: "));
        int bookingId = sc.nextInt();

        Booking booking = bookingService.viewBooking(bookingId);

        if(booking != null) {
            System.out.println(ColorText.warning("\n  ─────────────────────────────────────"));
            System.out.println("  Booking ID   : " + booking.getBookingId());
            System.out.println("  Package ID   : " + booking.getPackageId());
            System.out.println("  Travelers    : " + booking.getTravelers());
            System.out.println("  Booking Date : " + booking.getBookingDate());
            System.out.println("  Total Amount : " + ColorText.green("Rs. " + booking.getTotalAmount()));
            System.out.println("  Status       : " + ColorText.success(booking.getStatus()));
            System.out.println(ColorText.warning("  ─────────────────────────────────────"));
        } else {
            System.out.println(ColorText.error("\n  Booking not found!"));
        }
    }

    private void viewPaymentHistory() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("          PAYMENT HISTORY            ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print(ColorText.bold("  Booking ID: "));
        int bookingId = sc.nextInt();
        new PaymentService().viewPaymentHistory(bookingId);
    }

    private void cancelBooking() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           CANCEL BOOKING            ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print(ColorText.warning("  Booking ID to cancel: "));
        int bookingId = sc.nextInt();
        bookingService.cancelBooking(bookingId);
    }

    public void searchPackage() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           SEARCH PACKAGE            ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
        System.out.println("  1.  Search by Destination");
        System.out.println("  2.  Filter Packages");
        System.out.print(ColorText.bold("  Enter choice: "));

        int option = sc.nextInt();
        sc.nextLine();

        List<TourPackage> list = new ArrayList<>();

        if(option == 1) {
            System.out.print("  Destination: ");
            list = tourService.searchByDestination(sc.nextLine());
            if(list.isEmpty()) {
                System.out.println(ColorText.error("\n  No packages found!"));
                return;
            }
        } else if(option == 2) {
            System.out.println("\n  1.  Sort by Price");
            System.out.println("  2.  Sort by Duration");
            System.out.print(ColorText.bold("  Enter choice: "));
            int f = sc.nextInt(); sc.nextLine();
            list = tourService.getAllPackages();
            if(f == 1) Collections.sort(list, new PriceComparator());
            else if(f == 2) Collections.sort(list, new DurationComparator());
        } else {
            System.out.println(ColorText.error("\n  Invalid choice."));
            return;
        }

        System.out.println(ColorText.bold("\n  ┌────────┬──────────────────────┬──────────┬──────────────┐"));
        System.out.printf(ColorText.bold("  │ %-6s │ %-20s │ %-8s │ %-12s │%n"), "ID", "Destination", "Price", "Duration");
        System.out.println(ColorText.bold("  ├────────┼──────────────────────┼──────────┼──────────────┤"));
        for(TourPackage tp : list) {
            System.out.printf("  │ %-6d │ %-20s │ Rs.%-5d │ %-5d days   │%n",
                tp.getPackageId(), tp.getDestination(), tp.getPrice(), tp.getDuration());
        }
        System.out.println(ColorText.bold("  └────────┴──────────────────────┴──────────┴──────────────┘"));
    }
}