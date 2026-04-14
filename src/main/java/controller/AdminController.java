/*
 * Author         : Subhashree R
 * Description    : Central controller for administrative operations, including system management and report generation.
 * Module         : Admin Module
 * Java version   : 24
 */

package controller;

import java.util.Scanner;
import service.PaymentService;
import service.ReportService;
import util.ColorText;

public class AdminController {

    Scanner sc = new Scanner(System.in);

    PaymentService paymentService = new PaymentService();
    ReportService reportService   = new ReportService();

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
                case 1: new PackageController().adminPackageMenu(); break;
                case 2: new MessageController().adminMessageMenu(); break;
                case 3: new BookingController().adminBookingMenu(); break;
                case 4:
                    paymentService.viewAllPaymentHistory();
                    break;
                case 5: new BookingController().trackAllBookings(); break;
                case 6: new ItineraryController().adminItineraryMenu(); break;
                case 7: reportsMenu(); break;
                case 8: new FeedbackController().adminFeedbackMenu(); break;
                case 9:
                    System.out.println(ColorText.success("  Logging out..."));
                    return;
                default:
                    System.out.println(ColorText.error("  Invalid choice."));
            }
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
}