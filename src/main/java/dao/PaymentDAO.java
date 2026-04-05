package dao;

import model.Payment;
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

                    System.out.println("Payment successful! Payment ID: " + paymentId);
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
                payment = new Payment(
                        rs.getInt("paymentId"),
                        rs.getDouble("amount"),
                        rs.getString("paymentDate"),
                        rs.getString("status"),
                        rs.getInt("bookingId"),
                        rs.getString("paymentMethod")
                );
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

            while(rs.next()) {

                found = true;

                System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
                System.out.println(ColorText.warning("  ║") + ColorText.bold("                PAYMENT DETAILS                   ") + ColorText.warning("║"));
                System.out.println(ColorText.warning("  ╠══════════════════════════════════════════════════╣"));
                System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Payment ID   ") + ": %-32d" + ColorText.warning("║") + "%n", rs.getInt("paymentId"));
                System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Amount       ") + ": Rs. %-28.2f" + ColorText.warning("║") + "%n", rs.getDouble("amount"));
                System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Payment Date ") + ": %-32s" + ColorText.warning("║") + "%n", rs.getString("paymentDate"));
                System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Method       ") + ": %-32s" + ColorText.warning("║") + "%n", rs.getString("paymentMethod"));
                System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Status       ") + ": %-32s" + ColorText.warning("║") + "%n", rs.getString("status"));
                System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));
            }

            if(!found) {
                System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
                System.out.println(ColorText.warning("  ║") + ColorText.yellow("  No payment records found for this booking.      ") + ColorText.warning("║"));
                System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}