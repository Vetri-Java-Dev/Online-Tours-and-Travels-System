package dao;

import model.Payment;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentDAO {

    public void processPayment(Payment payment) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO payment(amount,paymentDate,status,bookingId,paymentMethod) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setDouble(1, payment.getAmount());
            ps.setString(2, payment.getPaymentDate());
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

                System.out.println("\n===== PAYMENT DETAILS =====");
                System.out.println("Payment ID: " + rs.getInt("paymentId"));
                System.out.println("Amount: " + rs.getDouble("amount"));
                System.out.println("Payment Date: " + rs.getString("paymentDate"));
                System.out.println("Payment Method: " + rs.getString("paymentMethod"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("---------------------------");
            }

            if(!found) {
                System.out.println("No payment records found for this booking.");
            }

        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}