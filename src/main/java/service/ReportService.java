package service;

import dao.ReportDAO;
import model.ReportData.BookingReportRow;
import model.ReportData.PackageAvailabilityRow;
import model.ReportData.PaymentReportRow;
import util.ColorText;

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
        System.out.println(ColorText.warning("\n  ╔════════════════════════════════════════════════════════════════════════════════════════════════════╗"));
        System.out.printf (ColorText.warning("  ║") + ColorText.bold("  %-96s") + ColorText.warning("║") + "%n", title);
        System.out.println(ColorText.warning("  ╠════════╦══════════════╦════════════════════╦══════════════════╦═══════════╦═════════════╦═════════════╣"));
        System.out.printf (ColorText.warning("  ║") + ColorText.cyan("  %-6s") + ColorText.warning("║") + ColorText.cyan("  %-12s") + ColorText.warning("║") + ColorText.cyan("  %-18s") + ColorText.warning("║") + ColorText.cyan("  %-16s") + ColorText.warning("║") + ColorText.cyan("  %-9s") + ColorText.warning("║") + ColorText.cyan("  %-11s") + ColorText.warning("║") + ColorText.cyan("  %-11s") + ColorText.warning("║") + "%n",
            "BkgID", "Date", "Customer", "Destination", "Travelers", "Amount(Rs.)", "Status");
        System.out.println(ColorText.warning("  ╠════════╬══════════════╬════════════════════╬══════════════════╬═══════════╬═════════════╬═════════════╣"));
    }

    private void printBookingRows(List<BookingReportRow> rows) {
        if (rows.isEmpty()) {
            System.out.println(ColorText.warning("  ║") + ColorText.yellow("  No records found.                                                                                 ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("  ╚════════╩══════════════╩════════════════════╩══════════════════╩═══════════╩═════════════╩═════════════╝"));
            return;
        }
        for (BookingReportRow r : rows) {
            System.out.printf(ColorText.warning("  ║") + "  %-6d" + ColorText.warning("║") + "  %-12s" + ColorText.warning("║") + "  %-18s" + ColorText.warning("║") + "  %-16s" + ColorText.warning("║") + "  %-9d" + ColorText.warning("║") + "  %-11.2f" + ColorText.warning("║") + "  %-11s" + ColorText.warning("║") + "%n",
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

        System.out.println(ColorText.warning("  ╠════════════════════════════════════════════════════════════════════════════════════════════════════╣"));
        System.out.printf (ColorText.warning("  ║") + ColorText.cyan("  Total: %-4d  │  Confirmed: %-4d  │  Cancelled: %-4d  │  Total Amount: Rs. %-19.2f  ") + ColorText.warning("║") + "%n",
            rows.size(), confirmed, cancelled, total);
        System.out.println(ColorText.warning("  ╚════════════════════════════════════════════════════════════════════════════════════════════════════╝"));
        System.out.println();
    }

    public void showPaymentReport() {
        System.out.println(ColorText.warning("\n  ╔════════════════════════════════════════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("                              PAYMENT REPORT                                        ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╠══════════╦══════════╦══════════════╦════════════════╦══════════════════╦══════════╣"));
        System.out.printf (ColorText.warning("  ║") + ColorText.cyan("  %-8s") + ColorText.warning("║") + ColorText.cyan("  %-8s") + ColorText.warning("║") + ColorText.cyan("  %-12s") + ColorText.warning("║") + ColorText.cyan("  %-14s") + ColorText.warning("║") + ColorText.cyan("  %-16s") + ColorText.warning("║") + ColorText.cyan("  %-8s") + ColorText.warning("║") + "%n",
            "PmtID", "BkgID", "Date", "Amount(Rs.)", "Method", "Status");
        System.out.println(ColorText.warning("  ╠══════════╬══════════╬══════════════╬════════════════╬══════════════════╬══════════╣"));

        List<PaymentReportRow> rows = reportDAO.getAllPayments();

        if (rows.isEmpty()) {
            System.out.println(ColorText.warning("  ║") + ColorText.yellow("  No payment records found.                                                          ") + ColorText.warning("║"));
        } else {
            for (PaymentReportRow r : rows) {
                System.out.printf(ColorText.warning("  ║") + "  %-8d" + ColorText.warning("║") + "  %-8d" + ColorText.warning("║") + "  %-12s" + ColorText.warning("║") + "  %-14.2f" + ColorText.warning("║") + "  %-16s" + ColorText.warning("║") + "  %-8s" + ColorText.warning("║") + "%n",
                    r.getPaymentId(), r.getBookingId(), r.getPaymentDate(),
                    r.getAmount(), r.getPaymentMethod(), r.getPaymentStatus());
            }
        }

        double totalRevenue = reportDAO.getTotalRevenue();
        System.out.println(ColorText.warning("  ╠════════════════════════════════════════════════════════════════════════════════════╣"));
        System.out.printf (ColorText.warning("  ║") + ColorText.cyan("  Total Transactions : %-5d   │   Total Revenue (SUCCESS) : Rs. %-19.2f") + ColorText.warning("║") + "%n",
            rows.size(), totalRevenue);
        System.out.println(ColorText.warning("  ╚════════════════════════════════════════════════════════════════════════════════════╝"));

        System.out.println(ColorText.warning("\n  ╔════════════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("           Revenue Breakdown by Payment Method          ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╚════════════════════════════════════════════════════════╝"));
        reportDAO.printRevenueByMethod();
        System.out.println();
    }

    public void showPackageAvailabilityReport() {
        System.out.println(ColorText.warning("\n  ╔════════════════════════════════════════════════════════════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("                              PACKAGE AVAILABILITY REPORT                                               ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╠════════╦════════════════════╦══════════╦══════════╦════════════╦══════════╦═══════════╦═══════════╦════╣"));
        System.out.printf (ColorText.warning("  ║") + ColorText.cyan("  %-6s") + ColorText.warning("║") + ColorText.cyan("  %-18s") + ColorText.warning("║") + ColorText.cyan("  %-8s") + ColorText.warning("║") + ColorText.cyan("  %-8s") + ColorText.warning("║") + ColorText.cyan("  %-10s") + ColorText.warning("║") + ColorText.cyan("  %-8s") + ColorText.warning("║") + ColorText.cyan("  %-9s") + ColorText.warning("║") + ColorText.cyan("  %-9s") + ColorText.warning("║") + ColorText.cyan("%-4s") + ColorText.warning("║") + "%n",
            "PkgID", "Destination", "Price", "Days", "TotalSeats", "Booked", "Available", "Cancelled", "Occ");
        System.out.println(ColorText.warning("  ╠════════╬════════════════════╬══════════╬══════════╬════════════╬══════════╬═══════════╬═══════════╬════╣"));

        List<PackageAvailabilityRow> rows = reportDAO.getPackageAvailabilityReport();

        if (rows.isEmpty()) {
            System.out.println(ColorText.warning("  ║") + ColorText.yellow("  No packages found.                                                                                     ") + ColorText.warning("║"));
        } else {
            for (PackageAvailabilityRow r : rows) {
                String occupancy = occupancyBar(r.getBookedSeats(), r.getTotalSeats());
                System.out.printf(ColorText.warning("  ║") + "  %-6d" + ColorText.warning("║") + "  %-18s" + ColorText.warning("║") + "  %-8.0f" + ColorText.warning("║") + "  %-8d" + ColorText.warning("║") + "  %-10d" + ColorText.warning("║") + "  %-8d" + ColorText.warning("║") + "  %-9d" + ColorText.warning("║") + "  %-9d" + ColorText.warning("║") + "%-4s" + ColorText.warning("║") + "%n",
                    r.getPackageId(), truncate(r.getDestination(), 18), r.getPrice(), r.getDuration(),
                    r.getTotalSeats(), r.getBookedSeats(), r.getAvailableSeats(), r.getCancelledBookings(),
                    occupancy.length() > 4 ? occupancy.substring(0,4) : occupancy);
            }
        }

        int totalSeats     = rows.stream().mapToInt(PackageAvailabilityRow::getTotalSeats).sum();
        int totalBooked    = rows.stream().mapToInt(PackageAvailabilityRow::getBookedSeats).sum();
        int totalAvailable = rows.stream().mapToInt(PackageAvailabilityRow::getAvailableSeats).sum();

        System.out.println(ColorText.warning("  ╠════════════════════════════════════════════════════════════════════════════════════════════════════════╣"));
        System.out.printf (ColorText.warning("  ║") + ColorText.cyan("  Packages: %-4d  │  Total Seats: %-5d  │  Booked: %-5d  │  Available: %-5d                           ") + ColorText.warning("║") + "%n",
            rows.size(), totalSeats, totalBooked, totalAvailable);
        System.out.println(ColorText.warning("  ╚════════════════════════════════════════════════════════════════════════════════════════════════════════╝"));
        System.out.println();
    }

  
    private String occupancyBar(int booked, int total) {

        if (total <= 0) return "[N/A]";

        booked = Math.max(0, booked);
        booked = Math.min(booked, total);

        int pct = (int) ((booked * 10.0) / total);

   
        pct = Math.max(0, Math.min(10, pct));

        int empty = 10 - pct;

        return "[" + "█".repeat(pct) + "░".repeat(empty) + "] " +
               String.format("%d%%", (int)((booked * 100.0) / total));
    }

    private String truncate(String s, int len) {
        if (s == null) return "";
        return s.length() > len ? s.substring(0, len - 1) + "…" : s;
    }
}