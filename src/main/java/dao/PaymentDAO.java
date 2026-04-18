/*
 * Author         : Subhashree R
 * Created Date   : 20-March-2026
 * Description    : Data Access Object for persisting and retrieving user/admin payment records from the database.
 * Module         : Payment Module
 * Java version   : 24
 */
package dao;

import model.*;
import util.ColorText;
import util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class PaymentDAO {

    public void processPayment(Payment payment) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO payment(amount, paymentDate, status, bookingId, paymentMethod) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            // Safely resolve paymentDate — fall back to today if null or unparseable
            java.sql.Date sqlDate;
            try {
                String dateStr = payment.getPaymentDate();
                sqlDate = (dateStr != null && !dateStr.isEmpty())
                        ? java.sql.Date.valueOf(LocalDate.parse(dateStr))
                        : java.sql.Date.valueOf(LocalDate.now());
            } catch (Exception dateEx) {
                sqlDate = java.sql.Date.valueOf(LocalDate.now());
            }

            ps.setDouble(1, payment.getAmount());
            ps.setDate(2, sqlDate);
            ps.setString(3, payment.getStatus());
            ps.setInt(4, payment.getBookingId());
            ps.setString(5, payment.getPaymentMethod());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {

                    int paymentId = rs.getInt(1);
                    payment.setPaymentId(paymentId);


                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Payment viewPayment(int paymentId) {

        Payment payment = null;

        try {
        	
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM payment WHERE paymentId=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, paymentId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String method = rs.getString("paymentMethod");
                int pId = rs.getInt("paymentId");
                double amt = rs.getDouble("amount");
                String date = rs.getString("paymentDate");
                String stat = rs.getString("status");
                int bId = rs.getInt("bookingId");

                if ("UPI".equalsIgnoreCase(method)) {
                    payment = new UPIPayment(pId, amt, date, stat, bId, "N/A");
                } else if ("CREDIT_CARD".equalsIgnoreCase(method)) {
                    payment = new CreditCardPayment(pId, amt, date, stat, bId, "N/A", "N/A", "N/A", "N/A");
                } else if ("DEBIT_CARD".equalsIgnoreCase(method)) {
                    payment = new DebitCardPayment(pId, amt, date, stat, bId, "N/A", "N/A", "N/A", "N/A");
                } else {
                    payment = new UPIPayment(pId, amt, date, stat, bId, "N/A");
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return payment;
    }



    public void viewPaymentHistory(int bookingId) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM payment WHERE bookingId=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println(ColorText.bold("\n  PAYMENT DETAILS"));
                System.out.println(ColorText.warning("  ─────────────────────────────────────"));
                System.out.println("  " + ColorText.cyan("Payment ID   ") + ": " + rs.getInt("paymentId"));
                System.out.println("  " + ColorText.cyan("Amount       ") + ": Rs. " + String.format("%.2f", rs.getDouble("amount")));
                System.out.println("  " + ColorText.cyan("Payment Date ") + ": " + rs.getString("paymentDate"));
                System.out.println("  " + ColorText.cyan("Method       ") + ": " + rs.getString("paymentMethod"));
                System.out.println("  " + ColorText.cyan("Status       ") + ": " + rs.getString("status"));
                System.out.println(ColorText.warning("  ─────────────────────────────────────"));
            }

            if (!found) {
                System.out.println(ColorText.yellow("\n  No payment records found for this booking."));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewPaymentHistoryByCustomerId(int customerId) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT pay.*, p.destination FROM payment pay " +
                           "JOIN booking b ON pay.bookingId = b.bookingId " +
                           "JOIN tour_package p ON b.packageId = p.packageId " +
                           "WHERE b.customerId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                if (!found) {
                    System.out.println(ColorText.warning("\n  ╔════════════╦══════════════════════╦═════════════╦══════════════╦════════════╗"));
                    System.out.println(ColorText.warning("  ║") + ColorText.cyan(" Payment ID ") + ColorText.warning("║") + ColorText.cyan(" Package Name         ") + ColorText.warning("║") + ColorText.cyan(" Amount      ") + ColorText.warning("║") + ColorText.cyan(" Method       ") + ColorText.warning("║") + ColorText.cyan(" Status     ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("  ╠════════════╬══════════════════════╬═════════════╬══════════════╬════════════╣"));
                    found = true;
                }
                String packageName = rs.getString("destination");
                if (packageName.length() > 20) packageName = packageName.substring(0, 17) + "...";

                System.out.printf(ColorText.warning("  ║") + "  %-10d" + ColorText.warning("║") + "  %-20s" + ColorText.warning("║") + " Rs. %-8.2f" + ColorText.warning("║") + "  %-12s" + ColorText.warning("║") + "  %-10s" + ColorText.warning("║") + "%n",
                        rs.getInt("paymentId"),
                        packageName,
                        rs.getDouble("amount"),
                        rs.getString("paymentMethod"),
                        rs.getString("status"));
            }

            if (found) {
                System.out.println(ColorText.warning("  ╚════════════╩══════════════════════╩═════════════╩══════════════╩════════════╝"));
            } else {
                System.out.println(ColorText.yellow("\n  No payment history found for your account."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewAllPaymentHistory() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT pay.*, u.name, p.destination FROM payment pay " +
                           "JOIN booking b ON pay.bookingId = b.bookingId " +
                           "JOIN users u ON b.customerId = u.userId " +
                           "JOIN tour_package p ON b.packageId = p.packageId";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                if (!found) {
                    System.out.println(ColorText.warning("\n  ╔════════════╦══════════════════════╦══════════════════════╦═════════════╦════════════╗"));
                    System.out.println(ColorText.warning("  ║") + ColorText.cyan(" Payment ID ") + ColorText.warning("║") + ColorText.cyan(" Customer Name        ") + ColorText.warning("║") + ColorText.cyan(" Package Name         ") + ColorText.warning("║") + ColorText.cyan(" Amount      ") + ColorText.warning("║") + ColorText.cyan(" Status     ") + ColorText.warning("║"));
                    System.out.println(ColorText.warning("  ╠════════════╬══════════════════════╬══════════════════════╬═════════════╬════════════╣"));
                    found = true;
                }
                String customerName = rs.getString("name");
                if (customerName.length() > 20) customerName = customerName.substring(0, 17) + "...";
                String packageName = rs.getString("destination");
                if (packageName.length() > 20) packageName = packageName.substring(0, 17) + "...";

                System.out.printf(ColorText.warning("  ║") + "  %-10d" + ColorText.warning("║") + "  %-20s" + ColorText.warning("║") + "  %-20s" + ColorText.warning("║") + " Rs. %-8.2f" + ColorText.warning("║") + "  %-10s" + ColorText.warning("║") + "%n",
                        rs.getInt("paymentId"),
                        customerName,
                        packageName,
                        rs.getDouble("amount"),
                        rs.getString("status"));
            }

            if (found) {
                System.out.println(ColorText.warning("  ╚════════════╩══════════════════════╩══════════════════════╩═════════════╩════════════╝"));
            } else {
                System.out.println(ColorText.yellow("\n  No payment records found in the system."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}