package controller;

import java.util.*;

import model.*;
import comparator.PriceComparator;
import comparator.DurationComparator;
import service.*;

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

        while (true) {

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         CUSTOMER DASHBOARD           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1.  View Tour Packages              ║");
            System.out.println("║  2.  Search Package                  ║");
            System.out.println("║  3.  View Package Itinerary          ║");
            System.out.println("║  4.  Create Booking                  ║");
            System.out.println("║  5.  View Booking                    ║");
            System.out.println("║  6.  Cancel Booking                  ║");
            System.out.println("║  7.  View Payment History            ║");
            System.out.println("║  8.  View Profile                    ║");
            System.out.println("║  9.  Message Admin                   ║");
            System.out.println("║ 10.  Update Profile                  ║");
            System.out.println("║ 11.  Booking History                 ║");
            System.out.println("║ 12.  Delete Account                  ║");
            System.out.println("║ 13.  Exit                            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1: tourService.displayPackages(); break;
                case 2: searchPackage(); break;
                case 3: viewItinerary(); break;
                case 4: createBooking(); break;
                case 5: viewBooking(); break;
                case 6: cancelBooking(); break;
                case 7: viewPaymentHistory(); break;
                case 8: viewProfile(); break;
                case 9: sc.nextLine();
                        messageMenu(); break;
                case 10: updateProfile(); break;
                case 11: viewBookingHistory(); break;
                case 12: deleteAccount(); return;
                case 13: return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ---------------- BOOKING ----------------

    private void createBooking() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           CREATE BOOKING            │");
        System.out.println("└─────────────────────────────────────┘");

        System.out.print("  Package ID        : ");
        int packageId = sc.nextInt();

        System.out.print("  Travelers         : ");
        int travelers = sc.nextInt();
        sc.nextLine();

        System.out.print("  Booking Date      : ");
        String date = sc.nextLine();

        Booking booking = new Booking();
        booking.setCustomerId(customerId);
        booking.setPackageId(packageId);
        booking.setTravelers(travelers);
        booking.setBookingDate(date);

        bookingService.createBooking(booking);

        int bookingId = booking.getBookingId();
        double amount = booking.getTotalAmount();

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           SELECT PAYMENT            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  1. UPI");
        System.out.println("  2. Credit Card");
        System.out.println("  3. Debit Card");
        System.out.print("  Choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        Payment payment = null;
        String today = java.time.LocalDate.now().toString();

        switch (choice) {
            case 1:
                System.out.print("  UPI ID: ");
                payment = new UPIPayment(amount, today, "SUCCESS", bookingId, sc.nextLine());
                break;

            case 2:
                System.out.print("  Card No: ");
                String cc = sc.nextLine();
                System.out.print("  Holder: ");
                payment = new CreditCardPayment(amount, today, "SUCCESS", bookingId, cc, sc.nextLine());
                break;

            case 3:
                System.out.print("  Card No: ");
                String dc = sc.nextLine();
                System.out.print("  Bank: ");
                payment = new DebitCardPayment(amount, today, "SUCCESS", bookingId, dc, sc.nextLine());
                break;
        }

        new PaymentService().processPayment(payment);
    }

    private void viewBooking() {
        System.out.print("\n  Booking ID: ");
        Booking b = bookingService.viewBooking(sc.nextInt());
    }

    private void cancelBooking() {
        System.out.print("\n  Booking ID: ");
        bookingService.cancelBooking(sc.nextInt());
    }

    private void viewPaymentHistory() {
        System.out.print("\n  Booking ID: ");
        new PaymentService().viewPaymentHistory(sc.nextInt());
    }

    // ---------------- PROFILE ----------------

    public void viewProfile() {
        User u = userService.getUserById(customerId);
        System.out.println("\nName: " + u.getName());
        System.out.println("Email: " + u.getEmail());
        System.out.println("Phone: " + u.getPhone());
    }

    public void updateProfile() {

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║           UPDATE PROFILE             ║");
        System.out.println("╠══════════════════════════════════════╣");

        sc.nextLine();
        System.out.print("  New Name  : ");
        String name = sc.nextLine();

        System.out.print("  New Phone : ");
        String phone = sc.nextLine();

        boolean updated = userService.updateUser(customerId, name, phone);

        System.out.printf("║ %-36s ║\n",
                updated ? "Profile updated successfully" : "Update failed");

        System.out.println("╚══════════════════════════════════════╝");
    }

    public void deleteAccount() {

        System.out.print("\nConfirm delete (yes/no): ");
        sc.nextLine();
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            userService.deleteUser(customerId);
            System.out.println("Account deleted.");
        }
    }

    // ---------------- HISTORY ----------------

    public void viewBookingHistory() {

        List<Booking> list = bookingService.getBookingsByCustomerId(customerId);

        System.out.println("\n┌────────┬────────┬────────────┬──────────┐");
        System.out.printf("│ %-6s │ %-6s │ %-10s │ %-8s │%n", "ID", "Pkg", "Date", "Status");
        System.out.println("├────────┼────────┼────────────┼──────────┤");

        for (Booking b : list) {
            System.out.printf("│ %-6d │ %-6d │ %-10s │ %-8s │%n",
                    b.getBookingId(), b.getPackageId(),
                    b.getBookingDate(), b.getStatus());
        }

        System.out.println("└────────┴────────┴────────────┴──────────┘");
    }

    // ---------------- SEARCH ----------------

    public void searchPackage() {

        System.out.println("\n1. Destination\n2. Sort");

        int opt = sc.nextInt();
        sc.nextLine();

        List<TourPackage> list = tourService.getAllPackages();

        if (opt == 1) {
            System.out.print("Destination: ");
            list = tourService.searchByDestination(sc.nextLine());
        } else if (opt == 2) {
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

    // ---------------- OTHER ----------------

    private void viewItinerary() {
        System.out.print("Package ID: ");
        new ItineraryService().viewItinerary(sc.nextInt());
    }

 // ---------------- MESSAGE ----------------

    private void messageMenu() {

        while (true) {

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           MESSAGE CENTER             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Send Message to Admin            ║");
            System.out.println("║  2. View Admin Replies               ║");
            System.out.println("║  3. Back                             ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {

                case 1:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│         SEND MESSAGE TO ADMIN       │");
                    System.out.println("└─────────────────────────────────────┘");
                    System.out.print("  Your message: ");
                    String msg = sc.nextLine();
                    if (msg == null || msg.trim().isEmpty()) {
                        System.out.println("  Message cannot be empty.");
                    } else {
                        messageService.sendToAdmin(customerId, msg.trim());
                        System.out.println("  Message sent to admin successfully!");
                    }
                    break;

                case 2:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│           ADMIN REPLIES             │");
                    System.out.println("└─────────────────────────────────────┘");
                    List<String> replies = messageService.viewReplies(customerId);
                    if (replies.isEmpty()) {
                        System.out.println("  No replies from admin yet.");
                    } else {
                        int i = 1;
                        for (String r : replies) {
                            System.out.println("  [" + i++ + "] Admin: " + r);
                        }
                    }
                    break;

                case 3:
                    return;

                default:
                    System.out.println("  Invalid choice!");
            }
        }
    }
}