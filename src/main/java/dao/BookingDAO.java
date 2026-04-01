package dao;
import model.Booking;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
public class BookingDAO {

	public void createBooking(Booking booking) {

	    try {

	        Connection con = DBConnection.getConnection();

	       
	        String query = "INSERT INTO booking(bookingDate,travelers,totalAmount,status,customerId,packageId,customerName,packageName) VALUES(?,?,?,?,?,?,?,?)";
	        PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

	        ps.setString(1, booking.getBookingDate());
	        ps.setInt(2, booking.getTravelers());
	        ps.setDouble(3, booking.getTotalAmount());
	        ps.setString(4, booking.getStatus());
	        ps.setInt(5, booking.getCustomerId());
	        ps.setInt(6, booking.getPackageId());
	        ps.setString(7, booking.getCustomerName());   
	        ps.setString(8, booking.getPackageName());    

	        int rowsAffected = ps.executeUpdate();

	        if (rowsAffected > 0) {
	        	
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                int bookingId = rs.getInt(1);
	                booking.setBookingId(bookingId);
	            }
	            
	        }

	    }
	    catch (Exception e) {
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
            	
            	booking = new Booking();

            	booking.setBookingId(rs.getInt("bookingId"));
            	booking.setBookingDate(rs.getString("bookingDate"));
            	booking.setTravelers(rs.getInt("travelers"));
            	booking.setTotalAmount(rs.getDouble("totalAmount"));
            	booking.setStatus(rs.getString("status"));
            	booking.setCustomerId(rs.getInt("customerId"));
            	booking.setPackageId(rs.getInt("packageId"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return booking;
    }
    public void updateBooking(Booking booking) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE booking SET bookingDate=?, travelers=?, totalAmount=? WHERE bookingId=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, booking.getBookingDate());
            ps.setInt(2, booking.getTravelers());
            ps.setDouble(3, booking.getTotalAmount());
            ps.setInt(4, booking.getBookingId());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Booking updated successfully");
            } else {
                System.out.println("Booking update failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Booking> getAllBookings() {

        List<Booking> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT b.bookingId, u.name, p.destination, b.bookingDate, b.status " +
                           "FROM booking b " +
                           "JOIN users u ON b.customerId = u.userId " +
                           "JOIN tour_package p ON b.packageId = p.packageId";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Booking b = new Booking();

                b.setBookingId(rs.getInt("bookingId"));
                b.setCustomerName(rs.getString("name"));        // ✅
                b.setPackageName(rs.getString("destination"));  // ✅
                b.setBookingDate(rs.getString("bookingDate"));
                b.setStatus(rs.getString("status"));

                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public void cancelBooking(int bookingId) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "UPDATE booking SET status='Cancelled' WHERE bookingId=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, bookingId);

            ps.executeUpdate();

            System.out.println("Booking cancelled successfully!");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}