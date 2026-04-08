
package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import exception.*;
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
    FeedbackService feedbackService   = new FeedbackService();

    // ================= MAIN ADMIN MENU =================
    public void adminMenu() {

        while (true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("           ADMIN DASHBOARD            ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. Manage Tour Packages             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. Message Customer                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Manage Booking                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4. View Payment History             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5. Track All Bookings               " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  6. Manage Itinerary                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  7. Reports                          " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  8. Manage Feedback & Ratings        " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  9. Exit                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                case 1: packageMenu(); break;
                case 2: messageMenu(); break;
                case 3: bookingMenu(); break;
                case 4:
                    paymentService.viewAllPaymentHistory();
                    break;

                case 5: trackAllBookings(); break;
                case 6: itineraryMenu(); break;
                case 7: reportsMenu(); break;
                case 8: feedbackMenu(); break;

                case 9:
                    System.out.println(ColorText.success("  Logging out..."));
                    return;
                default:
                    System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    // ================= FEEDBACK MENU =================
    private void feedbackMenu() {
        while (true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("      MANAGE FEEDBACK & RATINGS       ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. View Pending Feedback            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. Approve Feedback                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Reject Feedback                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4. View All Feedback                " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5. View Feedback by Package         " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  6. Delete Feedback                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  7. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1: feedbackService.viewPendingFeedback(); break;
                case 2:
                    System.out.print(ColorText.bold("  Feedback ID : "));
                    try {
                        feedbackService.approveFeedback(Integer.parseInt(sc.nextLine()));
                    } catch (BookingNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;
                case 3:
                    System.out.print(ColorText.bold("  Feedback ID : "));
                    try {
                        feedbackService.rejectFeedback(Integer.parseInt(sc.nextLine()));
                    } catch (BookingNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;
                case 4: feedbackService.viewAllFeedback(); break;
                case 5:
                    service.displayPackages();
                    System.out.print(ColorText.bold("\n  Enter Package ID to view feedback: "));
                    try {
                        feedbackService.viewPackageReviews(Integer.parseInt(sc.nextLine()));
                    } catch (PackageNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;
                case 6:
                    feedbackService.viewAllFeedback();
                    System.out.print(ColorText.bold("\n  Enter Feedback ID to delete: "));
                    try {
                        feedbackService.deleteFeedback(Integer.parseInt(sc.nextLine()));
                    } catch (BookingNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;
                case 7: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    // ================= PACKAGE MENU =================
    private void packageMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("         MANAGE TOUR PACKAGES         ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. Add Package                      " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. View Packages                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Update Package                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4. Delete Package                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {

                case 1:
                    System.out.print(ColorText.bold("  Package ID   : "));
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.print(ColorText.bold("  Destination  : "));
                    String dest = sc.nextLine();
                    System.out.print(ColorText.bold("  Price (INR)  : "));
                    int price = Integer.parseInt(sc.nextLine());
                    System.out.print(ColorText.bold("  Duration     : "));
                    int duration = Integer.parseInt(sc.nextLine());
                    System.out.print(ColorText.bold("  Seats        : "));
                    int seats = Integer.parseInt(sc.nextLine());
                    try {
                        service.createPackage(id, dest, price, duration, seats);
                        System.out.println(ColorText.success("  Package added successfully!"));
                    } catch (PackageAlreadyExistsException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 2: service.displayPackages(); break;

                case 3:

                    System.out.print(ColorText.bold("  Package ID      : "));

                    int uid;

                    try{
                        uid=Integer.parseInt(sc.nextLine());
                    }
                    catch(Exception e){
                        while(true){
                            if(sc.hasNextInt()){
                                uid=sc.nextInt();
                                break;
                            }
                            else{
                                System.out.print("Enter Valid Package Id : ");
                            }
                        }
                    }

                    System.out.print(ColorText.bold("  New Destination : "));
                    String nd = sc.nextLine();

                    System.out.print(ColorText.bold("  New Price       : "));
                    double np = Double.parseDouble(sc.nextLine());

                    System.out.print(ColorText.bold("  New Duration    : "));
                    int ndur = Integer.parseInt(sc.nextLine());

                    try {
                        service.updatePackage(uid, nd, np, ndur);
                        System.out.println(ColorText.success("  Package updated successfully!"));
                    }
                    catch (PackageNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 4:
                    System.out.print(ColorText.bold("  Package ID : "));
                    int deleteId = Integer.parseInt(sc.nextLine());
                    System.out.print(ColorText.bold("  Confirm delete? (yes/no): "));
                    if(sc.nextLine().equalsIgnoreCase("yes")) {
                        try {
                            service.deletePackage(deleteId);
                            System.out.println(ColorText.success("  Package deleted successfully!"));
                        } catch (PackageNotFoundException e) {
                            System.out.println(ColorText.error("  " + e.getMessage()));
                        }
                    } else {
                        System.out.println(ColorText.yellow("  Deletion cancelled."));
                    }
                    break;

                case 5: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    // ================= MESSAGE MENU =================
    private void messageMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("          MESSAGE CUSTOMER            ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. View Messages                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. Reply to Customer                " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {

                case 1:
                    List<String> messages = messageService.viewMessages();
                    System.out.println(ColorText.warning("\n  ╔═════════════════════════════════════════════════════╗"));
                    System.out.println(ColorText.warning("  ║") + ColorText.bold("              INBOX — CUSTOMER MESSAGES              ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("  ╠═════════════════════════════════════════════════════╣"));
                    if (messages == null || messages.isEmpty()) {
                        System.out.println(ColorText.warning("  ║") + ColorText.yellow("  No unread messages.                                ") + ColorText.warning("║"));
                    } else {
                        int idx = 1;
                        for (String msg : messages) {
                            String[] parts = msg.split(" : ", 2);
                            String sender  = parts.length > 0 ? parts[0].trim() : "Unknown";
                            String content = parts.length > 1 ? parts[1].trim() : msg;
                            System.out.printf(ColorText.warning("  ║") + " " + ColorText.cyan(String.format("[%2d]", idx)) + " " + ColorText.bold("From :") + " %-39s" + ColorText.warning("║") + "%n", truncate(sender, 39));
                            while (content.length() > 51) {
                                System.out.printf(ColorText.warning("  ║") + "        %-51s" + ColorText.warning("║") + "%n", content.substring(0, 51));
                                content = content.substring(51);
                            }
                            System.out.printf(ColorText.warning("  ║") + "        %-51s" + ColorText.warning("║") + "%n", content);
                            if (idx < messages.size())
                                System.out.println(ColorText.warning("  ╠═════════════════════════════════════════════════════╣"));
                            idx++;
                        }
                    }
                    System.out.println(ColorText.warning("  ╚═════════════════════════════════════════════════════╝"));
                    break;

                case 2:
                    try {
                        List<User> users = userService.getAllUsers();
                        System.out.println(ColorText.warning("\n  ╔═════════════════════════════════════════════════════╗"));
                        System.out.println(ColorText.warning("  ║") + ColorText.bold("              REGISTERED CUSTOMERS                ") + ColorText.warning("║"));
                        System.out.println(ColorText.warning("  ╠════════╦════════════════════════════════════════════╣"));
                        System.out.println(ColorText.warning("  ║") + ColorText.cyan("  ID    ") + ColorText.warning("║") + ColorText.cyan("  Name                                     ") + ColorText.warning("║"));
                        System.out.println(ColorText.warning("  ╠════════╬════════════════════════════════════════════╣"));
                        for (User u : users) {
                            System.out.printf(ColorText.warning("  ║") + "  %-6d" + ColorText.warning("║") + "  %-40s" + ColorText.warning("║") + "%n",
                                    u.getUserId(), truncate(u.getName(), 40));
                        }
                        System.out.println(ColorText.warning("  ╚════════╩════════════════════════════════════════════╝"));

                        System.out.print(ColorText.bold("\n  Enter Customer ID : "));
                        int cid = Integer.parseInt(sc.nextLine());
                        System.out.print(ColorText.bold("  Message           : "));
                        messageService.replyToCustomer(cid, sc.nextLine());
                        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════╗"));
                        System.out.println(ColorText.warning("  ║") + ColorText.success("  ✔  Reply sent successfully!          ") + ColorText.warning("║"));
                        System.out.println(ColorText.warning("  ╚══════════════════════════════════════╝"));
                    } catch (UserNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 3: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    // ================= BOOKING MENU =================
    private void bookingMenu() {
        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("           BOOKING MANAGEMENT         ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. View Booking                     " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. Cancel Booking                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int c = Integer.parseInt(sc.nextLine());

            if(c==1) {
                System.out.print(ColorText.bold("  Booking ID: "));
                int bookingId = Integer.parseInt(sc.nextLine());

                try {
                    Booking booking = bookingService.viewBooking(bookingId);

                    if (booking != null) {
                        System.out.println("\n  ─────────────────────────────────────");
                        System.out.println("  Booking ID   : " + booking.getBookingId());
                        System.out.println("  Package ID   : " + booking.getPackageId());
                        System.out.println("  Travelers    : " + booking.getTravelers());
                        System.out.println("  Booking Date : " + booking.getBookingDate());
                        System.out.printf ("  Total Amount : Rs. %.2f%n", booking.getTotalAmount());
                        System.out.println("  Status       : " + booking.getStatus());
                        System.out.println("  ─────────────────────────────────────");
                    } else {
                        System.out.println("Booking not found!");
                    }
                } catch (BookingNotFoundException e) {
                    System.out.println(ColorText.error("  " + e.getMessage()));
                }
            }
            else if(c==2) {
                System.out.print(ColorText.bold("  Booking ID: "));
                try {
                    bookingService.cancelBooking(Integer.parseInt(sc.nextLine()));
                } catch (BookingNotFoundException | InvalidBookingException e) {
                    System.out.println(ColorText.error("  " + e.getMessage()));
                }
            }
            else return;
        }
    }

    // ================= TRACK BOOKINGS =================
    private void trackAllBookings() {

        System.out.println("\n" + ColorText.bold("========== TRACK PACKAGE BOOKINGS ==========\n"));

        List<Booking> list = bookingService.getAllBookings();

        if (list.isEmpty()) {
            System.out.println(ColorText.error("No bookings found.\n"));
            return;
        }

        // Header
        System.out.printf(ColorText.cyan("%-6s | %-20s | %-20s | %-12s | %-10s%n"),
                "ID", "Customer", "Package", "Date", "Status");

        System.out.println("----------------------------------------------------------------------");

        // Rows
        for (Booking b : list) {
            System.out.printf("%-6d | %-20s | %-20s | %-12s | %-10s%n",
                    b.getBookingId(),
                    truncate(b.getCustomerName(), 20),
                    truncate(b.getPackageName(), 20),
                    b.getBookingDate(),
                    b.getStatus());
        }

        System.out.println("\n" + ColorText.bold("===========================================\n"));
    }

    // ================= ITINERARY =================
    private void itineraryMenu() {
        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("          ITINERARY MANAGEMENT        ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. Add Itinerary                    " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. View Itinerary                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Modify Itinerary                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4. Delete Itinerary                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int c = Integer.parseInt(sc.nextLine());

            switch(c) {
                case 1: addItinerary(); break;
                case 2: viewItinerary(); break;
                case 3: addItinerary(); break;
                case 4:
                    System.out.print(ColorText.bold("  Itinerary ID: "));
                    try {
                        itineraryService.deleteItinerary(Integer.parseInt(sc.nextLine()));
                    } catch (PackageNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;
                case 5: return;
            }
        }
    }

    private void addItinerary() {
        try {
            int pid = Integer.parseInt(sc.nextLine());
            int days = Integer.parseInt(sc.nextLine());

            List<ItineraryItem> items = new ArrayList<>();
            for(int i=1;i<=days;i++) {
                items.add(new ItineraryItem(0,i,sc.nextLine(),sc.nextLine()));
            }
            itineraryService.createItinerary(new Itinerary(0,pid,1,items));
        } catch (PackageNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }

    private void viewItinerary() {
        try {
            Itinerary it = itineraryService.viewItinerary(Integer.parseInt(sc.nextLine()));
            if(it!=null) it.getItems().forEach(i->System.out.println(
                    ColorText.cyan("  Day " + i.getDayNumber()) + " : " + i.getLocation()));
        } catch (PackageNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }

    // ================= REPORT =================
    private void reportsMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("               REPORTS                ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. All Bookings                     " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. Confirmed Bookings               " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Cancelled Bookings               " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4. Payment Report                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5. Package Availability             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  6. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print("Enter your choice : ");
            int c = Integer.parseInt(sc.nextLine());

            switch(c) {
                case 1: reportService.showAllBookingsReport(); break;
                case 2: reportService.showBookingReportByStatus("CONFIRMED"); break;
                case 3: reportService.showBookingReportByStatus("Cancelled"); break;
                case 4: reportService.showPaymentReport(); break;
                case 5: reportService.showPackageAvailabilityReport(); break;
                case 6: return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }
    private String truncate(String s, int len) {
        if (s == null) return "";
        return s.length() > len ? s.substring(0, len - 1) + "…" : s;
    }
}