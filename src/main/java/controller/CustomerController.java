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

    public CustomerController() {}

    // ================= MENU =================
    public void customerMenu() {

        while (true) {

        	System.out.println(ColorText.warning("╔══════════════════════════════════════╗"));
        	System.out.println(ColorText.warning("║") + ColorText.bold("         CUSTOMER DASHBOARD           ") + ColorText.warning("║"));
        	System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
        	System.out.println(ColorText.warning("║") + "  1.  View Tour Packages              " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  2.  Search Package                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  3.  View Package Itinerary          " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  4.  Create Booking                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  5.  View Booking                    " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  6.  Modify Booking                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  7.  Cancel Booking                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  8.  View Payment History            " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  9.  View Profile                    " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 10.  Update Profile                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 11.  Booking History                 " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 12.  Message Admin                   " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 13.  Delete Account                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + " 14.  Exit                            " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int choice = sc.nextInt();

            switch (choice) {
                case 1: tourService.displayPackages(); break;
                case 2: searchPackage(); break;
                case 3: viewItinerary(); break;
                case 4: createBooking(); break;
                case 5: viewBooking(); break;
                case 6: modifyBooking(); break;
                case 7: cancelBooking(); break;
                case 8: viewPaymentHistory(); break;
                case 9: viewProfile(); break;
                case 10: updateProfile(); break;
                case 11: viewBookingHistory(); break;
                case 12: sc.nextLine(); messageMenu(); break;
                case 13: deleteAccount(); return;
                case 14: return;
                default:
                    System.out.println(ColorText.error("\nInvalid choice."));
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

        String bookingDate;

        while (true) {
            System.out.print("Booking Date (YYYY-MM-DD): ");
            bookingDate = sc.nextLine();

            try {
                LocalDate date = LocalDate.parse(bookingDate);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Past date not allowed.");
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
        booking.setBookingDate(bookingDate);

        bookingService.createBooking(booking);

        int bookingId = booking.getBookingId();
        double amount = booking.getTotalAmount();

        System.out.println("\n1. UPI\n2. Credit Card\n3. Debit Card");
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
        System.out.print("Booking ID: ");
        bookingService.viewBooking(sc.nextInt());
    }

    private void cancelBooking() {
        System.out.print("Booking ID: ");
        bookingService.cancelBooking(sc.nextInt());
    }

    private void modifyBooking() {

        System.out.print("Booking ID: ");
        int id = sc.nextInt();

        Booking b = bookingService.viewBooking(id);

        if (b == null || b.getStatus().equalsIgnoreCase("Cancelled")) {
            System.out.println("Cannot modify.");
            return;
        }

        sc.nextLine();

        System.out.print("New Date: ");
        String date = sc.nextLine();

        System.out.print("New Travelers: ");
        int t = sc.nextInt();

        b.setBookingDate(date);
        b.setTravelers(t);

        bookingService.modifyBooking(b);

        System.out.println("Updated!");
    }

    private void viewPaymentHistory() {
        System.out.print("Booking ID: ");
        new PaymentService().viewPaymentHistory(sc.nextInt());
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