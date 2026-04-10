/*
 * Author         : Subhashree R
 * Created Date   : 2-Apr-2026
 * Description    : Data Access Object for executing complex SQL aggregations to retrieve holistic system metrics and administrative report data.
 * Module         : Admin Report Module
 * Java version   : 24
 */
package dao;

import model.ReportData.BookingReportRow;
import model.ReportData.PackageAvailabilityRow;
import model.ReportData.PaymentReportRow;
import util.ColorText;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    /** Returns all bookings joined with customer name and destination. */
    public List<BookingReportRow> getAllBookings() {
        List<BookingReportRow> list = new ArrayList<>();
        String sql =
            "SELECT b.bookingId, b.bookingDate, b.customerId, u.name AS customerName, " +
            "       b.packageId, tp.destination, b.travelers, b.totalAmount, b.status " +
            "FROM booking b " +
            "JOIN users u          ON b.customerId = u.userId " +
            "JOIN tour_package tp  ON b.packageId  = tp.packageId " +
            "ORDER BY b.bookingDate DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new BookingReportRow(
                    rs.getInt("bookingId"),
                    rs.getString("bookingDate"),
                    rs.getInt("customerId"),
                    rs.getString("customerName"),
                    rs.getInt("packageId"),
                    rs.getString("destination"),
                    rs.getInt("travelers"),
                    rs.getDouble("totalAmount"),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Filter bookings by status: "CONFIRMED", "Cancelled", or "ALL". */
    public List<BookingReportRow> getBookingsByStatus(String status) {
        List<BookingReportRow> list = new ArrayList<>();
        String sql =
            "SELECT b.bookingId, b.bookingDate, b.customerId, u.name AS customerName, " +
            "       b.packageId, tp.destination, b.travelers, b.totalAmount, b.status " +
            "FROM booking b " +
            "JOIN users u          ON b.customerId = u.userId " +
            "JOIN tour_package tp  ON b.packageId  = tp.packageId " +
            "WHERE b.status = ? " +
            "ORDER BY b.bookingDate DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new BookingReportRow(
                    rs.getInt("bookingId"),
                    rs.getString("bookingDate"),
                    rs.getInt("customerId"),
                    rs.getString("customerName"),
                    rs.getInt("packageId"),
                    rs.getString("destination"),
                    rs.getInt("travelers"),
                    rs.getDouble("totalAmount"),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Returns all payments. */
    public List<PaymentReportRow> getAllPayments() {
        List<PaymentReportRow> list = new ArrayList<>();
        String sql =
            "SELECT paymentId, bookingId, paymentDate, amount, paymentMethod, status " +
            "FROM payment ORDER BY paymentDate DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new PaymentReportRow(
                    rs.getInt("paymentId"),
                    rs.getInt("bookingId"),
                    rs.getString("paymentDate"),
                    rs.getDouble("amount"),
                    rs.getString("paymentMethod"),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Total revenue from successful payments. */
    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payment WHERE status = 'SUCCESS'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Revenue grouped by payment method — with color formatting. */
    public void printRevenueByMethod() {
        String sql =
            "SELECT paymentMethod, COUNT(*) AS txnCount, SUM(amount) AS total " +
            "FROM payment WHERE status = 'SUCCESS' " +
            "GROUP BY paymentMethod";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println(ColorText.warning("  ┌──────────────────┬──────────┬──────────────────┐"));
            System.out.println(ColorText.warning("  │") + ColorText.cyan("  Payment Method  ") + ColorText.warning("│") + ColorText.cyan("  Count   ") + ColorText.warning("│") + ColorText.cyan("  Total (Rs.)     ") + ColorText.warning("│"));
            System.out.println(ColorText.warning("  ├──────────────────┼──────────┼──────────────────┤"));

            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf(
                    ColorText.warning("  │") + " %-16s " + ColorText.warning("│") + " %-8d " + ColorText.warning("│") + " %-16.2f " + ColorText.warning("│") + "%n",
                    rs.getString("paymentMethod"),
                    rs.getInt("txnCount"),
                    rs.getDouble("total")
                );
            }

            if (!hasRows) {
                System.out.println(ColorText.warning("  │") + ColorText.yellow("  No revenue data found.                       ") + ColorText.warning("│"));
            }

            System.out.println(ColorText.warning("  └──────────────────┴──────────┴──────────────────┘"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns seat occupancy per package.
     * bookedSeats  = SUM of travelers for CONFIRMED bookings.
     * totalSeats   = Current available seats in DB + bookedSeats (Reconstructs original capacity).
     * available    = totalSeats - bookedSeats (Matches current available seats in DB).
     */
    public List<PackageAvailabilityRow> getPackageAvailabilityReport() {
        List<PackageAvailabilityRow> list = new ArrayList<>();
        String sql =
            "SELECT tp.packageId, tp.destination, tp.price, tp.duration, " +
            "       (tp.availableSeats + COALESCE(SUM(CASE WHEN b.status = 'CONFIRMED' THEN b.travelers ELSE 0 END), 0)) AS totalSeats, " +
            "       COALESCE(SUM(CASE WHEN b.status = 'CONFIRMED' THEN b.travelers ELSE 0 END), 0) AS bookedSeats, " +
            "       COUNT(CASE WHEN b.status = 'Cancelled' THEN 1 END) AS cancelledBookings " +
            "FROM tour_package tp " +
            "LEFT JOIN booking b ON tp.packageId = b.packageId " +
            "GROUP BY tp.packageId, tp.destination, tp.price, tp.duration, tp.availableSeats " +
            "ORDER BY tp.packageId";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int totalSeats  = rs.getInt("totalSeats");
                int bookedSeats = rs.getInt("bookedSeats");
                int available   = Math.max(0, totalSeats - bookedSeats);
                int cancelled   = rs.getInt("cancelledBookings");

                list.add(new PackageAvailabilityRow(
                    rs.getInt("packageId"),
                    rs.getString("destination"),
                    rs.getDouble("price"),
                    rs.getInt("duration"),
                    totalSeats,
                    bookedSeats,
                    available,
                    cancelled
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}