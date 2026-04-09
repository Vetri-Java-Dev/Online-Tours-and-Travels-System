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
        printBookingReport("All Bookings Report", reportDAO.getAllBookings());
    }

    public void showBookingReportByStatus(String status) {
        printBookingReport("Bookings Report — " + status.toUpperCase(), reportDAO.getBookingsByStatus(status));
    }

    private void printBookingReport(String title, List<BookingReportRow> rows) {
        long confirmed = rows.stream().filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus())).count();
        long cancelled = rows.stream().filter(r -> "Cancelled".equalsIgnoreCase(r.getStatus())).count();
        double total   = rows.stream().mapToDouble(BookingReportRow::getTotalAmount).sum();

        System.out.println("\n  " + ColorText.bold(title));
        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────────────"));
        System.out.printf("  %-6s  %-20s  %-18s  %-12s  %-10s  %-12s  %s%n",
                ColorText.cyan("BkgID"), ColorText.cyan("Customer"),
                ColorText.cyan("Destination"), ColorText.cyan("Date"),
                ColorText.cyan("Travelers"), ColorText.cyan("Amount(Rs.)"),
                ColorText.cyan("Status"));
        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────────────"));

        if (rows.isEmpty()) {
            System.out.println(ColorText.yellow("  No records found."));
        } else {
            for (BookingReportRow r : rows) {
                System.out.printf("  %-6d  %-20s  %-18s  %-12s  %-10d  %-12.2f  %s%n",
                        r.getBookingId(),
                        r.getCustomerName(),
                        r.getDestination(),
                        r.getBookingDate(),
                        r.getTravelers(),
                        r.getTotalAmount(),
                        colorStatus(r.getStatus()));
            }
        }

        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────────────"));
        System.out.println("  Total: " + rows.size()
                + "   Confirmed: " + ColorText.success(String.valueOf(confirmed))
                + "   Cancelled: " + ColorText.error(String.valueOf(cancelled))
                + "   Revenue: " + ColorText.cyan("Rs. " + String.format("%.2f", total)));
        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────────────"));
    }

    public void showPaymentReport() {
        List<PaymentReportRow> rows = reportDAO.getAllPayments();
        double totalRevenue = reportDAO.getTotalRevenue();

        System.out.println("\n  " + ColorText.bold("Payment Report"));
        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────"));
        System.out.printf("  %-8s  %-8s  %-14s  %-14s  %-18s  %s%n",
                ColorText.cyan("PmtID"), ColorText.cyan("BkgID"),
                ColorText.cyan("Date"), ColorText.cyan("Amount(Rs.)"),
                ColorText.cyan("Method"), ColorText.cyan("Status"));
        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────"));

        if (rows.isEmpty()) {
            System.out.println(ColorText.yellow("  No payment records found."));
        } else {
            for (PaymentReportRow r : rows) {
                System.out.printf("  %-8d  %-8d  %-14s  %-14.2f  %-18s  %s%n",
                        r.getPaymentId(),
                        r.getBookingId(),
                        r.getPaymentDate(),
                        r.getAmount(),
                        r.getPaymentMethod(),
                        colorStatus(r.getPaymentStatus()));
            }
        }

        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────"));
        System.out.println("  Total Transactions: " + rows.size()
                + "   Total Revenue: " + ColorText.cyan("Rs. " + String.format("%.2f", totalRevenue)));
        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────"));

        System.out.println("\n  " + ColorText.bold("Revenue by Payment Method"));
        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────"));
        reportDAO.printRevenueByMethod();
        System.out.println(ColorText.warning("  ─────────────────────────────────────────────────────────────────"));
    }

    public void showPackageAvailabilityReport() {
        List<PackageAvailabilityRow> rows = reportDAO.getPackageAvailabilityReport();

        int totalSeats     = rows.stream().mapToInt(PackageAvailabilityRow::getTotalSeats).sum();
        int totalBooked    = rows.stream().mapToInt(PackageAvailabilityRow::getBookedSeats).sum();
        int totalAvailable = rows.stream().mapToInt(PackageAvailabilityRow::getAvailableSeats).sum();

        System.out.println("\n  " + ColorText.bold("Package Availability & Occupancy Report"));
        System.out.println(ColorText.warning("  ──────────────────────────────────────────────────────────────────────────────────────────"));
        System.out.printf("  %-6s  %-20s  %-10s  %-6s  %-10s  %-8s  %-10s  %-10s  %s%n",
                ColorText.cyan("PkgID"), ColorText.cyan("Destination"),
                ColorText.cyan("Price"), ColorText.cyan("Days"),
                ColorText.cyan("TotalSeats"), ColorText.cyan("Booked"),
                ColorText.cyan("Available"), ColorText.cyan("Cancelled"),
                ColorText.cyan("Occupancy"));
        System.out.println(ColorText.warning("  ──────────────────────────────────────────────────────────────────────────────────────────"));

        if (rows.isEmpty()) {
            System.out.println(ColorText.yellow("  No packages found in the system."));
        } else {
            for (PackageAvailabilityRow r : rows) {
                System.out.printf("  %-6d  %-20s  %-10.0f  %-6d  %-10d  %-8d  %-10d  %-10d  %s%n",
                        r.getPackageId(),
                        r.getDestination(),
                        r.getPrice(),
                        r.getDuration(),
                        r.getTotalSeats(),
                        r.getBookedSeats(),
                        r.getAvailableSeats(),
                        r.getCancelledBookings(),
                        ColorText.yellow(occupancyBar(r.getBookedSeats(), r.getTotalSeats())));
            }
        }

        System.out.println(ColorText.warning("  ──────────────────────────────────────────────────────────────────────────────────────────"));
        System.out.println("  Packages: " + rows.size()
                + "   Total Seats: " + totalSeats
                + "   Booked: " + ColorText.cyan(String.valueOf(totalBooked))
                + "   Available: " + ColorText.success(String.valueOf(totalAvailable)));
        System.out.println(ColorText.warning("  ──────────────────────────────────────────────────────────────────────────────────────────"));
    }

    private String occupancyBar(int booked, int total) {
        if (total <= 0) return "[N/A]";
        booked = Math.max(0, Math.min(booked, total));
        int pct   = Math.max(0, Math.min(10, (int) ((booked * 10.0) / total)));
        int empty = 10 - pct;
        return "[" + "█".repeat(pct) + "░".repeat(empty) + "] "
               + String.format("%d%%", (int) ((booked * 100.0) / total));
    }

    private String colorStatus(String status) {
        if (status == null) return "";
        switch (status.toUpperCase()) {
            case "CONFIRMED":
            case "SUCCESS":   return ColorText.success(status.toUpperCase());
            case "CANCELLED":
            case "FAILED":    return ColorText.error(status.toUpperCase());
            default:          return ColorText.yellow(status.toUpperCase());
        }
    }
}