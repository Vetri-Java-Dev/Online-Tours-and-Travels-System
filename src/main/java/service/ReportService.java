package service;

import dao.ReportDAO;
import model.ReportData.BookingReportRow;
import model.ReportData.PackageAvailabilityRow;
import model.ReportData.PaymentReportRow;

import java.util.List;

public class ReportService {

    private final ReportDAO reportDAO = new ReportDAO();

    public void showAllBookingsReport() {
        printBookingReportHeader("ALL BOOKINGS REPORT");
        List<BookingReportRow> rows = reportDAO.getAllBookings();
        printBookingRows(rows);
        printBookingSummary(rows);
    }

    public void showBookingReportByStatus(String status) {
        printBookingReportHeader("BOOKINGS REPORT — STATUS: " + status.toUpperCase());
        List<BookingReportRow> rows = reportDAO.getBookingsByStatus(status);
        printBookingRows(rows);
        printBookingSummary(rows);
    }

    private void printBookingReportHeader(String title) {
        System.out.println("\n┌────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf ("│  %-74s│%n", title);
        System.out.println("└────────────────────────────────────────────────────────────────────────────┘");
        System.out.printf("  %-6s  %-12s  %-18s  %-16s  %-10s  %-12s  %-12s%n",
            "BkgID", "Date", "Customer", "Destination", "Travelers", "Amount(Rs.)", "Status");
        System.out.println("  " + "─".repeat(96));
    }

    private void printBookingRows(List<BookingReportRow> rows) {
        if (rows.isEmpty()) {
            System.out.println("  No records found.");
            return;
        }
        for (BookingReportRow r : rows) {
            System.out.printf("  %-6d  %-12s  %-18s  %-16s  %-10d  %-12.2f  %-12s%n",
                r.getBookingId(),
                r.getBookingDate(),
                truncate(r.getCustomerName(), 18),
                truncate(r.getDestination(), 16),
                r.getTravelers(),
                r.getTotalAmount(),
                r.getStatus());
        }
    }

    private void printBookingSummary(List<BookingReportRow> rows) {
        long confirmed  = rows.stream().filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus())).count();
        long cancelled  = rows.stream().filter(r -> "Cancelled".equalsIgnoreCase(r.getStatus())).count();
        double total    = rows.stream().mapToDouble(BookingReportRow::getTotalAmount).sum();

        System.out.println("  " + "─".repeat(96));
        System.out.printf("  Total Bookings : %d   |   Confirmed : %d   |   Cancelled : %d   |   Total Amount : Rs. %.2f%n",
            rows.size(), confirmed, cancelled, total);
        System.out.println();
    }

   

    public void showPaymentReport() {
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│                    PAYMENT REPORT                         │");
        System.out.println("└────────────────────────────────────────────────────────────┘");

        List<PaymentReportRow> rows = reportDAO.getAllPayments();

        System.out.printf("  %-8s  %-8s  %-12s  %-14s  %-16s  %-10s%n",
            "PmtID", "BkgID", "Date", "Amount(Rs.)", "Method", "Status");
        System.out.println("  " + "─".repeat(80));

        if (rows.isEmpty()) {
            System.out.println("  No payment records found.");
        } else {
            for (PaymentReportRow r : rows) {
                System.out.printf("  %-8d  %-8d  %-12s  %-14.2f  %-16s  %-10s%n",
                    r.getPaymentId(),
                    r.getBookingId(),
                    r.getPaymentDate(),
                    r.getAmount(),
                    r.getPaymentMethod(),
                    r.getPaymentStatus());
            }
        }

        System.out.println("  " + "─".repeat(80));
        double totalRevenue = reportDAO.getTotalRevenue();
        System.out.printf("  Total Transactions : %d   |   Total Revenue (SUCCESS) : Rs. %.2f%n",
            rows.size(), totalRevenue);

        System.out.println("\n  ── Revenue by Payment Method ──");
        reportDAO.printRevenueByMethod();
        System.out.println();
    }

   

    public void showPackageAvailabilityReport() {
        System.out.println("\n┌────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                   PACKAGE AVAILABILITY REPORT                         │");
        System.out.println("└────────────────────────────────────────────────────────────────────────┘");

        List<PackageAvailabilityRow> rows = reportDAO.getPackageAvailabilityReport();

        System.out.printf("  %-6s  %-18s  %-8s  %-8s  %-10s  %-8s  %-9s  %-9s%n",
            "PkgID", "Destination", "Price", "Days", "TotalSeats", "Booked", "Available", "Cancelled");
        System.out.println("  " + "─".repeat(92));

        if (rows.isEmpty()) {
            System.out.println("  No packages found.");
        } else {
            for (PackageAvailabilityRow r : rows) {
                // Occupancy indicator
                String occupancy = occupancyBar(r.getBookedSeats(), r.getTotalSeats());

                System.out.printf("  %-6d  %-18s  %-8.0f  %-8d  %-10d  %-8d  %-9d  %-9d  %s%n",
                    r.getPackageId(),
                    truncate(r.getDestination(), 18),
                    r.getPrice(),
                    r.getDuration(),
                    r.getTotalSeats(),
                    r.getBookedSeats(),
                    r.getAvailableSeats(),
                    r.getCancelledBookings(),
                    occupancy);
            }
        }

        System.out.println("  " + "─".repeat(92));

        // Summary
        int totalSeats     = rows.stream().mapToInt(PackageAvailabilityRow::getTotalSeats).sum();
        int totalBooked    = rows.stream().mapToInt(PackageAvailabilityRow::getBookedSeats).sum();
        int totalAvailable = rows.stream().mapToInt(PackageAvailabilityRow::getAvailableSeats).sum();

        System.out.printf("  Packages : %d   |   Total Seats : %d   |   Booked : %d   |   Available : %d%n",
            rows.size(), totalSeats, totalBooked, totalAvailable);
        System.out.println();
    }

    

    /** Builds a simple ASCII occupancy bar: e.g. [████░░░░] */
    private String occupancyBar(int booked, int total) {
        if (total <= 0) return "[N/A]";
        int pct   = (int) ((booked * 10.0) / total);
        int empty = 10 - pct;
        return "[" + "█".repeat(pct) + "░".repeat(empty) + "] " +
               String.format("%d%%", (int)((booked * 100.0) / total));
    }

    private String truncate(String s, int len) {
        if (s == null) return "";
        return s.length() > len ? s.substring(0, len - 1) + "…" : s;
    }
}