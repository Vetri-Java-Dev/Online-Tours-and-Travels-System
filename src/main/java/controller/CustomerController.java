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

    private TourPackageService tourService    = new TourPackageService();
    private BookingService     bookingService = new BookingService();
    private UserService        userService    = new UserService();
    private MessageService     messageService = new MessageService();
    private FeedbackService    feedbackService = new FeedbackService();

    private int customerId;

    public CustomerController(int customerId) {
        this.customerId = customerId;
    }

    // ── Main menu ─────────────────────────────────────────────────────────────
    public void customerMenu() {

        while (true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
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
            System.out.println(ColorText.warning("║") + " 13.  Feedback & Ratings              " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + " 14.  Delete Account                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + " 15.  Exit                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));

        	System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));

        	System.out.println(ColorText.warning("║") + "  1.  View Tour Packages              " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  2.  Search Package                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  3.  View Package Itinerary          " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  4.  Manage Booking                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  5.  View Payment History            " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  6.  Manage Profile                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  7.  Message Admin                   " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  8.  Delete Account                  " + ColorText.warning("║"));
        	System.out.println(ColorText.warning("║") + "  9.  Exit                            " + ColorText.warning("║"));

        	System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        	System.out.print(ColorText.bold("Enter choice: "));
        	int choice = sc.nextInt();

            switch (choice) {
                case 1:  tourService.displayPackages(); break;
                case 2:  searchPackage(); break;
                case 3:  viewItinerary(); break;
                case 4:  createBooking(); break;
                case 5:  viewBooking(); break;
                case 6:  modifyBooking(); break;
                case 7:  cancelBooking(); break;
                case 8:  viewPaymentHistory(); break;
                case 9:  viewProfile(); break;
                case 10: updateProfile(); break;
                case 11: viewBookingHistory(); break;
                case 12: sc.nextLine(); messageMenu(); break;
                case 13: sc.nextLine(); feedbackMenu(); break;
                case 14: deleteAccount(); return;
                case 15:
                    System.out.println(ColorText.success("  Logging out..."));
                    return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    // ── Feedback sub-menu ─────────────────────────────────────────────────────
    private void feedbackMenu() {

        while (true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("         FEEDBACK & RATINGS           ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1.  Submit Feedback                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2.  View My Feedback                " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3.  View Package Reviews            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4.  Back                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));

            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1: submitFeedback(); break;
                case 2:
                    System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
                    System.out.println(ColorText.warning("║") + ColorText.bold("           MY FEEDBACK HISTORY        ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
                    feedbackService.viewMyFeedback(customerId);
                    break;
                case 3: viewPackageReviews(); break;
                case 4: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    // ── Submit feedback ───────────────────────────────────────────────────────
    private void submitFeedback() {

        List<Booking> all = bookingService.getBookingsByCustomerId(customerId);
        List<Booking> eligible = new ArrayList<>();

        for (Booking b : all) {
            if ("CONFIRMED".equalsIgnoreCase(b.getStatus()) &&
                !feedbackService.hasFeedback(b.getBookingId())) {
                eligible.add(b);
            }
        }

        if (eligible.isEmpty()) {
            System.out.println(ColorText.warning("\n  No eligible bookings found."));
            System.out.println(ColorText.yellow("  You can submit feedback only for CONFIRMED bookings"));
            System.out.println(ColorText.yellow("  that have not been reviewed yet."));
            return;
        }

        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("     BOOKINGS ELIGIBLE FOR FEEDBACK     ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╠════════════╦═══════════════╦════════════╣"));
        System.out.println(ColorText.warning("  ║") + ColorText.cyan("  Booking ID") + ColorText.warning("║") + ColorText.cyan("  Date         ") + ColorText.warning("║") + ColorText.cyan("  Pkg ID    ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╠════════════╬═══════════════╬════════════╣"));
        for (Booking b : eligible) {
            System.out.printf(ColorText.warning("  ║") + "  %-10d" + ColorText.warning("║") + "  %-13s" + ColorText.warning("║") + "  %-10d" + ColorText.warning("║") + "%n",
                b.getBookingId(), b.getBookingDate(), b.getPackageId());
        }
        System.out.println(ColorText.warning("  ╚════════════╩═══════════════╩════════════╝"));

        System.out.print(ColorText.bold("\n  Enter Booking ID: "));
        int bookingId;
        try {
            bookingId = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(ColorText.error("  Invalid input."));
            return;
        }

        Booking selected = null;
        for (Booking b : eligible) {
            if (b.getBookingId() == bookingId) { selected = b; break; }
        }
        if (selected == null) {
            System.out.println(ColorText.error("  Invalid Booking ID or not eligible."));
            return;
        }

        // ── Rating ──
        int rating = 0;
        while (rating < 1 || rating > 5) {
            System.out.println(ColorText.warning("\n  Rate your experience:"));
            System.out.println(ColorText.yellow("  1 ★ Poor  |  2 ★★ Fair  |  3 ★★★ Good  |  4 ★★★★ Very Good  |  5 ★★★★★ Excellent"));
            System.out.print(ColorText.bold("  Your rating (1-5): "));
            try {
                rating = Integer.parseInt(sc.nextLine());
                if (rating < 1 || rating > 5)
                    System.out.println(ColorText.error("  Please enter a number between 1 and 5."));
            } catch (NumberFormatException e) {
                System.out.println(ColorText.error("  Invalid input."));
            }
        }

        // ── Title ──
        String title = "";
        while (title.isEmpty()) {
            System.out.print(ColorText.bold("\n  Review title (max 150 chars): "));
            title = sc.nextLine().trim();
            if (title.isEmpty())
                System.out.println(ColorText.error("  Title cannot be empty."));
            else if (title.length() > 150) {
                System.out.println(ColorText.error("  Title too long. Max 150 characters."));
                title = "";
            }
        }

        // ── Description ──
        String description = "";
        while (description.length() < 10) {
            System.out.print(ColorText.bold("\n  Write your review (min 10 chars): "));
            description = sc.nextLine().trim();
            if (description.length() < 10)
                System.out.println(ColorText.error("  Review must be at least 10 characters."));
        }

        feedbackService.submitFeedback(
            bookingId, customerId, selected.getPackageId(),
            rating, title, description
        );
    }

    // ── View package reviews ──────────────────────────────────────────────────
    private void viewPackageReviews() {
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("       VIEW PACKAGE REVIEWS           ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  Enter Package ID : "));
        try {
            int packageId = Integer.parseInt(sc.nextLine());
            feedbackService.viewPackageReviews(packageId);
        } catch (NumberFormatException e) {
            System.out.println(ColorText.error("  Invalid Package ID."));
        }
    }

    // ── Booking ───────────────────────────────────────────────────────────────
    private void createBooking() {

        System.out.print(ColorText.bold("  Package ID : "));
        int packageId = sc.nextInt();

        System.out.print(ColorText.bold("  Travelers  : "));
        int travelers = sc.nextInt();
        sc.nextLine();

        String bookingDate;
        while (true) {
            System.out.print(ColorText.bold("  Booking Date (YYYY-MM-DD): "));
            bookingDate = sc.nextLine();
            try {
                LocalDate date = LocalDate.parse(bookingDate);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println(ColorText.error("  Date is in the past. Enter again."));
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println(ColorText.error("  Invalid format."));
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

        int    bookingId = booking.getBookingId();
        double amount    = booking.getTotalAmount();
        String today     = LocalDate.now().toString();

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           SELECT PAYMENT             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
        System.out.println(ColorText.warning("║") + "  1.  UPI                             " + ColorText.warning("║"));
        System.out.println(ColorText.warning("║") + "  2.  Credit Card                     " + ColorText.warning("║"));
        System.out.println(ColorText.warning("║") + "  3.  Debit Card                      " + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  Enter choice: "));
        int choice = sc.nextInt();
        sc.nextLine();

        Payment payment = null;
        switch (choice) {
            case 1:
                System.out.print(ColorText.bold("  UPI ID         : "));
                payment = new UPIPayment(amount, today, "SUCCESS", bookingId, sc.nextLine());
                break;
            case 2:
                System.out.print(ColorText.bold("  Credit Card No : "));
                String cc = sc.nextLine();
                System.out.print(ColorText.bold("  Holder         : "));
                payment = new CreditCardPayment(amount, today, "SUCCESS", bookingId, cc, sc.nextLine());
                break;
            case 3:
                System.out.print(ColorText.bold("  Debit Card No  : "));
                String dc = sc.nextLine();
                System.out.print(ColorText.bold("  Bank           : "));
                payment = new DebitCardPayment(amount, today, "SUCCESS", bookingId, dc, sc.nextLine());
                break;
        }
        new PaymentService().processPayment(payment);
    }

    private void viewBooking() {
        System.out.print(ColorText.bold("  Booking ID: "));
        bookingService.viewBooking(sc.nextInt());
    }
    private void modifyBooking() {

    private void cancelBooking() {
        System.out.print(ColorText.bold("  Booking ID: "));
        bookingService.cancelBooking(sc.nextInt());
    }

    private void modifyBooking() {
        System.out.print(ColorText.bold("  Booking ID: "));
        int id = sc.nextInt();
        Booking b = bookingService.viewBooking(id);
        if (b == null || b.getStatus().equalsIgnoreCase("Cancelled")) {
            System.out.println(ColorText.error("  Cannot modify this booking."));
            return;
        }
        sc.nextLine();
        System.out.print(ColorText.bold("  New Date      : "));
        String date = sc.nextLine();
        System.out.print(ColorText.bold("  New Travelers : "));
        int t = sc.nextInt();
        b.setBookingDate(date);
        b.setTravelers(t);
        bookingService.modifyBooking(b);
        System.out.println(ColorText.success("  Booking updated successfully!"));
    }

    private void viewPaymentHistory() {
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("         VIEW PAYMENT HISTORY         ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  Enter Booking ID : "));
        new PaymentService().viewPaymentHistory(sc.nextInt());
    }

    // ── Profile ───────────────────────────────────────────────────────────────
    public void viewProfile() {
        User u = userService.getUserById(customerId);
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("            MY PROFILE                ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
        System.out.printf(ColorText.warning("║") + "  " + ColorText.cyan("Name  :") + " %-30s" + ColorText.warning("║") + "%n", u.getName());
        System.out.printf(ColorText.warning("║") + "  " + ColorText.cyan("Email :") + " %-30s" + ColorText.warning("║") + "%n", u.getEmail());
        System.out.printf(ColorText.warning("║") + "  " + ColorText.cyan("Phone :") + " %-30s" + ColorText.warning("║") + "%n", u.getPhone());
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
    }

    public void updateProfile() {
        sc.nextLine();
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           UPDATE PROFILE             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  New Name  : "));
        String name = sc.nextLine();
        System.out.print(ColorText.bold("  New Phone : "));
        String phone = sc.nextLine();
        userService.updateUser(customerId, name, phone);
        System.out.println(ColorText.success("  Profile updated successfully!"));
    }

    public void deleteAccount() {
        sc.nextLine();
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           DELETE ACCOUNT             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  Confirm delete (yes/no): "));
        if (sc.nextLine().equalsIgnoreCase("yes")) {
            userService.deleteUser(customerId);
            System.out.println(ColorText.success("  Account deleted successfully."));
        } else {
            System.out.println(ColorText.yellow("  Account deletion cancelled."));
        }
    }

    // ── History ───────────────────────────────────────────────────────────────
    public void viewBookingHistory() {
        List<Booking> list = bookingService.getBookingsByCustomerId(customerId);
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           BOOKING HISTORY            ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════╦═══════════════════════╣"));
        System.out.println(ColorText.warning("║") + ColorText.cyan("  Booking ID  ") + ColorText.warning("║") + ColorText.cyan("  Status               ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════╬═══════════════════════╣"));
        if (list.isEmpty()) {
            System.out.println(ColorText.warning("║") + "  No booking history found.           " + ColorText.warning("║"));
        } else {
            for (Booking b : list)
                System.out.printf(ColorText.warning("║") + "  %-12d" + ColorText.warning("║") + "  %-21s" + ColorText.warning("║") + "%n",
                    b.getBookingId(), b.getStatus());
        }
        System.out.println(ColorText.warning("╚══════════════╩═══════════════════════╝"));
    }

    // ── Search ────────────────────────────────────────────────────────────────
    public void searchPackage() {
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           SEARCH PACKAGE             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
        System.out.println(ColorText.warning("║") + "  1.  Search by Destination           " + ColorText.warning("║"));
        System.out.println(ColorText.warning("║") + "  2.  Sort Packages                   " + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  Enter choice: "));
        int opt = sc.nextInt();
        sc.nextLine();
        List<TourPackage> list = tourService.getAllPackages();
        if (opt == 1) {
            System.out.print(ColorText.bold("  Destination: "));
            list = tourService.searchByDestination(sc.nextLine());
        } else {
            System.out.println(ColorText.warning("\n  1. Sort by Price   2. Sort by Duration"));
            System.out.print(ColorText.bold("  Enter choice: "));
            if (sc.nextInt() == 1) Collections.sort(list, new PriceComparator());
            else                   Collections.sort(list, new DurationComparator());
        }
        for (TourPackage t : list)
            System.out.println("  " + ColorText.cyan(t.getDestination()) + " - Rs." + t.getPrice());
    }

    private void viewItinerary() {
        System.out.print(ColorText.bold("  Package ID: "));
        new ItineraryService().viewItinerary(sc.nextInt());
    }

    // ── Message ───────────────────────────────────────────────────────────────
    private void messageMenu() {
        while (true) {
            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("           MESSAGE ADMIN              ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1.  Send Message                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2.  View Replies                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3.  Back                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));
            int ch = Integer.parseInt(sc.nextLine());
            switch (ch) {
                case 1:
                    System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
                    System.out.println(ColorText.warning("║") + ColorText.bold("       SEND MESSAGE TO ADMIN          ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
                    System.out.print(ColorText.bold("  Your Message : "));
                    messageService.sendToAdmin(customerId, sc.nextLine());
                    System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
                    System.out.println(ColorText.warning("║") + ColorText.success("  ✔  Message sent to Admin!           ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
                    break;
                case 2:
                    List<String> replies = messageService.viewReplies(customerId);
                    System.out.println(ColorText.warning("\n╔══════════════════════════════════════════════════╗"));
                    System.out.println(ColorText.warning("║") + ColorText.bold("             REPLIES FROM ADMIN                   ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("╠══════════════════════════════════════════════════╣"));
                    if (replies.isEmpty()) {
                        System.out.println(ColorText.warning("║") + ColorText.yellow("  No replies from admin yet.                      ") + ColorText.warning("║"));
                    } else {
                        int idx = 1;
                        for (String r : replies) {
                            System.out.printf(ColorText.warning("║") + "  " + ColorText.cyan(String.format("[%2d]", idx)) + "  %-44s" + ColorText.warning("║") + "%n",
                                r.length() > 44 ? r.substring(0, 44) : r);
                            String rest = r.length() > 44 ? r.substring(44) : "";
                            while (!rest.isEmpty()) {
                                System.out.printf(ColorText.warning("║") + "        %-44s" + ColorText.warning("║") + "%n",
                                    rest.length() > 44 ? rest.substring(0, 44) : rest);
                                rest = rest.length() > 44 ? rest.substring(44) : "";
                            }
                            if (idx < replies.size())
                                System.out.println(ColorText.warning("╠══════════════════════════════════════════════════╣"));
                            idx++;
                        }
                    }
                    System.out.println(ColorText.warning("╚══════════════════════════════════════════════════╝"));
                    break;
                case 3: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }
}