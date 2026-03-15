package dao;

import model.Booking;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookingDAO {

    public void createBooking(Booking booking) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO booking(bookingDate,travelers,totalAmount,status,customerId,packageId) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, booking.getBookingDate());
            ps.setInt(2, booking.getTravelers());
            ps.setDouble(3, booking.getTotalAmount());
            ps.setString(4, booking.getStatus());
            ps.setInt(5, booking.getCustomerId());
            ps.setInt(6, booking.getPackageId());

            ps.executeUpdate();

            System.out.println("Booking created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Booking viewBooking(int bookingId) {

        Booking booking = null;

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM booking WHERE bookingId=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, bookingId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                booking = new Booking(
                        rs.getInt("bookingId"),
                        rs.getString("bookingDate"),
                        rs.getInt("travelers"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status"),
                        rs.getInt("customerId"),
                        rs.getInt("packageId")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return booking;
    }


    public void cancelBooking(int bookingId) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "UPDATE booking SET status='Cancelled' WHERE bookingId=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, bookingId);

            ps.executeUpdate();

            System.out.println("Booking cancelled successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}