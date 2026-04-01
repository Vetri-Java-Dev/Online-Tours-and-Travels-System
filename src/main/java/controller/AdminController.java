package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Booking;
import model.Itinerary;
import model.ItineraryItem;
import model.User;
import service.*;

public class AdminController {

    Scanner sc = new Scanner(System.in);

    TourPackageService service       = new TourPackageService();
    MessageService messageService    = new MessageService();
    BookingService bookingService    = new BookingService();
    PaymentService paymentService    = new PaymentService();
    ItineraryService itineraryService = new ItineraryService();
    UserService userService          = new UserService();
    ReportService reportService = new ReportService();

    // ================= MAIN MENU =================
    public void adminMenu() {

        while (true) {

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           ADMIN DASHBOARD            ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Manage Tour Packages             ║");
            System.out.println("║  2. Message Customer                 ║");
            System.out.println("║  3. Manage Booking                   ║");
            System.out.println("║  4. View Payment History             ║");
            System.out.println("║  5. Track All Bookings               ║");
            System.out.println("║  6. Manage Itinerary                 ║");
            System.out.println("║  7. Reports                          ║");
            System.out.println("║  8. Exit                             ║");           
            System.out.println("╚══════════════════════════════════════╝");

            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1: packageMenu(); break;
                case 2: messageMenu(); break;
                case 3: bookingMenu(); break;

                case 4:
                    System.out.print("Enter Booking ID: ");
                    paymentService.viewPaymentHistory(Integer.parseInt(sc.nextLine()));
                    break;
                case 5: trackAllBookings(); break;
                case 6: itineraryMenu(); break;
                case 7: reportsMenu(); break;
                case 8:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= PACKAGE MENU =================
    private void packageMenu() {

        while (true) {

            System.out.println("\n===== MANAGE PACKAGES =====");
            System.out.println("1. Add");
            System.out.println("2. View");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("5. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    System.out.print("ID: ");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.print("Destination: ");
                    String dest = sc.nextLine();

                    System.out.print("Price: ");
                    int price = Integer.parseInt(sc.nextLine());

                    System.out.print("Duration: ");
                    int duration = Integer.parseInt(sc.nextLine());
                    
                    System.out.print("Available Seats: ");
                    int seats = Integer.parseInt(sc.nextLine());

                    service.createPackage(id, dest, price, duration, seats); break;

                case 2:
                    service.displayPackages();
                    break;

                case 3:
                    System.out.print("ID: ");
                    int uid = Integer.parseInt(sc.nextLine());

                    System.out.print("New Destination: ");
                    String nd = sc.nextLine();

                    System.out.print("New Price: ");
                    double np = Double.parseDouble(sc.nextLine());

                    System.out.print("New Duration: ");
                    int ndur = Integer.parseInt(sc.nextLine());

                    service.updatePackage(uid, nd, np, ndur);
                    break;

                case 4:
                    System.out.print("ID: ");
                    service.deletePackage(Integer.parseInt(sc.nextLine()));
                    break;

                case 5:
                    return;
            }
        }
    }

 // ================= MESSAGE MENU =================
    private void messageMenu() {

        while (true) {
        	

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           MESSAGE CENTER             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. View Customer Messages           ║");
            System.out.println("║  2. Send Reply to Customer           ║");
            System.out.println("║  3. Back                             ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│        CUSTOMER MESSAGES            │");
                    System.out.println("└─────────────────────────────────────┘");
                    List<String> messages = messageService.viewMessages();
                    if (messages.isEmpty()) {
                        System.out.println("  No unread messages.");
                    } else {
                        int i = 1;
                        for (String m : messages) {
                            System.out.println("  [" + i++ + "] " + m);
                        }
                    }
                    break;

                case 2:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│         SEND REPLY TO CUSTOMER      │");
                    System.out.println("└─────────────────────────────────────┘");

                    List<User> users = userService.getAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("  No customers found.");
                        break;
                    }

                    System.out.println("  ── Customer List ──");
                    for (User u : users) {
                        System.out.println("  " + u.getUserId() + " - " + u.getName());
                    }

                    System.out.print("  Enter Customer ID: ");
                    int cid = Integer.parseInt(sc.nextLine());

                    System.out.print("  Message           : ");
                    String msg = sc.nextLine();

                    messageService.replyToCustomer(cid, msg);
                    System.out.println("  Reply sent successfully!");
                    break;

                case 3:
                    return;

                default:
                    System.out.println("  Invalid choice!");
            }
        }
    }

    // ================= BOOKING MENU =================
    private void bookingMenu() {

        while (true) {

            System.out.println("\n===== MANAGE BOOKINGS =====");
            System.out.println("1. View Booking");
            System.out.println("2. Cancel Booking");
            System.out.println("3. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    System.out.print("Booking ID: ");
                    bookingService.viewBooking(Integer.parseInt(sc.nextLine()));
                    break;

                case 2:
                    System.out.print("Booking ID: ");
                    bookingService.cancelBooking(Integer.parseInt(sc.nextLine()));
                    break;

                case 3:
                    return;
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

        while (true) {

            System.out.println("\n===== MANAGE ITINERARY =====");
            System.out.println("1. Add");
            System.out.println("2. View");
            System.out.println("3. Modify");
            System.out.println("4. Delete");
            System.out.println("5. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1: addItinerary(); break;
                case 2: viewItinerary(); break;
                case 3: modifyItinerary(); break;
                case 4: deleteItinerary(); break;
                case 5: return;
            }
        }
    }

    // ================= ITINERARY METHODS =================

    private void addItinerary() {

        System.out.print("Package ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("Days: ");
        int days = Integer.parseInt(sc.nextLine());

        List<ItineraryItem> items = new ArrayList<>();

        for (int i = 1; i <= days; i++) {

            System.out.println("Day " + i);

            System.out.print("Location: ");
            String loc = sc.nextLine();

            System.out.print("Activity: ");
            String act = sc.nextLine();

            items.add(new ItineraryItem(0, i, act, loc));
        }

        itineraryService.createItinerary(new Itinerary(0, pid, 1, items));
    }

    private void viewItinerary() {

        System.out.print("Package ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        Itinerary it = itineraryService.viewItinerary(pid);

        if (it != null) {
            for (ItineraryItem item : it.getItems()) {
                System.out.println("Day " + item.getDayNumber() +
                        " | " + item.getLocation() +
                        " | " + item.getActivity());
            }
        } else {
            System.out.println("No itinerary found.");
        }
    }

    private void modifyItinerary() {
        addItinerary(); // simple replace logic
    }

    private void deleteItinerary() {
        System.out.print("Package ID: ");
        itineraryService.deleteItinerary(Integer.parseInt(sc.nextLine()));
    }
 // ================= REPORTS MENU =================
    private void reportsMenu() {

        while (true) {

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║        REPORTS & ANALYTICS             ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║  1. Booking Report (All)               ║");
            System.out.println("║  2. Booking Report (Confirmed only)    ║");
            System.out.println("║  3. Booking Report (Cancelled only)    ║");
            System.out.println("║  4. Payment & Revenue Report           ║");
            System.out.println("║  5. Package Availability Report        ║");
            System.out.println("║  6. Back                               ║");
            System.out.println("╚════════════════════════════════════════╝");

            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: reportService.showAllBookingsReport();                      break;
                case 2: reportService.showBookingReportByStatus("CONFIRMED");       break;
                case 3: reportService.showBookingReportByStatus("Cancelled");       break;
                case 4: reportService.showPaymentReport();                          break;
                case 5: reportService.showPackageAvailabilityReport();              break;
                case 6: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }
}