/*
 * Author         : Harini R G
 * Description    : BookingController handles all booking-related operations 
 *                  for both admin and customer such as creating, viewing, 
 *                  modifying, cancelling bookings, tracking bookings, and 
 *                  managing payments.
 * Module         : Booking Module
 * Java version   : 25
 */
package controller;
import java.time.LocalDate;
import java.util.List;
import exception.*;
import util.InputUtil;
import model.Booking;
import model.Payment;
import model.CreditCardPayment;
import model.DebitCardPayment;
import model.UPIPayment;
import service.BookingService;
import service.PaymentService;
import util.ColorText;

public class BookingController {

    private BookingService bookingService = new BookingService();

    // ================= ADMIN: BOOKING MENU =================
    public void adminBookingMenu() {
        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("           BOOKING MANAGEMENT         ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1. View Booking                     " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2. Cancel Booking                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            int c = InputUtil.getInt(ColorText.bold("  Enter choice: "));

            if(c==1) {
                int bookingId = InputUtil.getInt(ColorText.bold("  Booking ID: "));

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
                    }
                    else {
                        System.out.println("Booking not found!");
                    }
                }
                catch (BookingNotFoundException e) {
                    System.out.println(ColorText.error("  " + e.getMessage()));
                }
            }
            else if(c==2) {
                try {
                	int bookingId = InputUtil.getInt(ColorText.bold("  Booking ID: "));
                    bookingService.cancelBooking(bookingId);
                    System.out.println(ColorText.success("  Booking cancelled successfully!"));
                }
                catch (BookingNotFoundException | InvalidBookingException e) {
                    System.out.println(ColorText.error("  " + e.getMessage()));
                }
            }
            else return;
        }
    }

    // ================= ADMIN: TRACK BOOKINGS =================
    public void trackAllBookings() {

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
           
        	System.out.printf("%-6d | %-12s | %-10s%n",
        	        b.getBookingId(),
        	        b.getBookingDate(),
        	        b.getStatus());
        }

        System.out.println("\n" + ColorText.bold("===========================================\n"));
    }

    private String truncate(String s, int len) {
        if (s == null) return "";
        return s.length() > len ? s.substring(0, len - 1) + "…" : s;
    }

    // ================= CUSTOMER: MANAGE BOOKING =================
    public void customerManageBookingMenu(int customerId) {

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

            int choice = InputUtil.getInt(ColorText.bold("Enter choice: "));

            switch (choice) {
                case 1: createBooking(customerId); break;
                case 2: viewBooking(customerId); break;
                case 3: modifyBooking(customerId); break;  
                case 4: cancelBooking(customerId); break;
                case 5: viewBookingHistory(customerId); break;
                case 6: return;
                default:
                    System.out.println(ColorText.error("Invalid choice!"));
            }
        }
    }

    private void createBooking(int customerId) {

        int packageId = InputUtil.getInt(ColorText.bold("  Package ID : "));
        int travelers = InputUtil.getInt(ColorText.bold("  Travelers  : "));

        LocalDate date = null;
        String bookingDate;

        while (true) {
            bookingDate = InputUtil.getString(ColorText.bold("  Booking Date (YYYY-MM-DD): "));
            try {
                date = LocalDate.parse(bookingDate);
                LocalDate today = LocalDate.now();

                if (date.isBefore(today)) {
                    System.out.println("Date is in the past. Enter again.");
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

        try {
        	boolean result = bookingService.createBooking(booking);

        	if (!result) {
        	    System.out.println(ColorText.error("Booking failed. Returning to menu..."));
        	    return;
        	}
        }
        catch (PackageNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
            return;
        }
        catch (NotAvailableSeatsException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
            return;
        }
        catch (BookingFailedException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
            return;
        }

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

        if (bookingId <= 0) {
            System.out.println(ColorText.error("  ✘  Booking could not be created. Payment cancelled."));
            return;
        }

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           SELECT PAYMENT             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
        System.out.println(ColorText.warning("║") + "  1.  UPI                             " + ColorText.warning("║"));
        System.out.println(ColorText.warning("║") + "  2.  Credit Card                     " + ColorText.warning("║"));
        System.out.println(ColorText.warning("║") + "  3.  Debit Card                      " + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        int choice = InputUtil.getInt(ColorText.bold("  Enter choice: "));

        Payment payment = null;
        String today = LocalDate.now().toString();

        switch (choice) {

            case 1:
                payment = new UPIPayment(amount, today, "SUCCESS", bookingId, InputUtil.getString(ColorText.bold("  UPI ID         : ")));
                break;

            case 2:
                String cc = InputUtil.getString(ColorText.bold("  Credit Card No : "));
                String holder = InputUtil.getString(ColorText.bold("  Holder         : "));
                String ccExpiry = InputUtil.getString(ColorText.bold("  Expiry (MM/YY) : "));
                String ccCvv = InputUtil.getString(ColorText.bold("  CVV            : "));

                payment = new CreditCardPayment(amount, today, "SUCCESS", bookingId, cc, holder, ccExpiry, ccCvv);

                break;

            case 3:
                String dc = InputUtil.getString(ColorText.bold("  Debit Card No  : "));
                String bank = InputUtil.getString(ColorText.bold("  Bank           : "));
                String dcExpiry = InputUtil.getString(ColorText.bold("  Expiry (MM/YY) : "));
                String dcCvv = InputUtil.getString(ColorText.bold("  CVV            : "));

                payment = new DebitCardPayment(amount, today, "SUCCESS", bookingId, dc, bank, dcExpiry, dcCvv);

                break;
            default:
                System.out.println(ColorText.error("  ✘  Invalid payment choice."));
                return;
        }

        try {
            new PaymentService().processPayment(payment);
        }
        catch (PaymentFailedException e) {
            System.out.println(ColorText.error("  ✘  " + e.getMessage()));
        }
    }

    private void viewBooking(int customerId) {
        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│")
                + ColorText.bold("            VIEW BOOKING             ")
                + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        int bookingId = InputUtil.getInt("Enter Booking ID: ");

        try {
        	Booking booking = bookingService.viewBookingByCustomer(bookingId, customerId);
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
        } catch (BookingNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }

    private void cancelBooking(int customerId) {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│")
                + ColorText.bold("           CANCEL BOOKING            ")
                + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        int bookingId = InputUtil.getInt(ColorText.bold("Enter Booking ID: "));

        try {
            Booking booking = bookingService.viewBookingByCustomer(bookingId, customerId);

            if (booking == null) return;

            boolean result = bookingService.cancelBooking(bookingId);

            if (result) {
                System.out.println(ColorText.success("Booking cancelled successfully!"));
            } else {
                System.out.println(ColorText.error("Invalid Booking ID!"));
            }

        } catch (Exception e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }
    private void modifyBooking(int customerId) {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│")
                + ColorText.bold("           MODIFY BOOKING            ")
                + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        int bookingId = InputUtil.getInt(ColorText.bold("Enter Booking ID: "));

        Booking booking;

        try {
            booking = bookingService.viewBookingByCustomer(bookingId, customerId);

            if (booking == null) return;

        } catch (Exception e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
            return;
        }

        if (booking.getStatus().equalsIgnoreCase("Cancelled")) {
            System.out.println("\n  Cannot modify a cancelled booking!");
            return;
        }

        System.out.println("\n  Current Details:");
        System.out.println("  Date       : " + booking.getBookingDate());
        System.out.println("  Travelers  : " + booking.getTravelers());

        String newDate;

        while (true) {
            newDate = InputUtil.getString("\n  Enter New Booking Date (YYYY-MM-DD): ");

            try {
                LocalDate date = LocalDate.parse(newDate);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Date is in the past. Enter again.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid format. Enter again.");
            }
        }

        int newTravelers = InputUtil.getInt("Enter New Travelers: ");

        booking.setBookingDate(LocalDate.parse(newDate));
        booking.setTravelers(newTravelers);

        try {
            bookingService.modifyBooking(booking);
            System.out.println(ColorText.success("Booking modified successfully!"));
        } catch (Exception e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }
    public void viewBookingHistory(int customerId) {
        List<Booking> list = bookingService.getBookingsByCustomerId(customerId);
        System.out.println(ColorText.warning("\n╔════════════╦════════════════╦════════════════╗"));

        System.out.printf(
                ColorText.warning("║")
              + "%-12s"
              + ColorText.warning("║")
              + "%-16s"
              + ColorText.warning("║")
              + "%-16s"
              + ColorText.warning("║\n"),
              "Booking ID", "Package", "Status"
        );

        System.out.println(ColorText.warning("╠════════════╬════════════════╬════════════════╣"));

        for (Booking b : list) {
            System.out.printf(
                    ColorText.warning("║")
                  + "%-12d"
                  + ColorText.warning("║")
                  + "%-16s"
                  + ColorText.warning("║")
                  + "%-16s"
                  + ColorText.warning("║\n"),
                  b.getBookingId(),
                  b.getPackageName(),
                  b.getStatus()
            );
        }

        System.out.println(ColorText.warning("╚════════════╩════════════════╩════════════════╝"));
        }
   
    public void customerViewPaymentHistory(int customerId) {
        new PaymentService().viewPaymentHistoryByCustomerId(customerId);
    }
}

