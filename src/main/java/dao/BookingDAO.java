/*
 * Author         : Harini R G
 * Description    : BookingDAO handles all database operations related to booking 
 *                  such as creating, viewing, updating, cancelling bookings, and 
 *                  retrieving booking details from the database.
 * Module         : Booking Module (DAO Layer)
 * Java version   : 25
 */
package dao;

import model.Booking;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class BookingDAO {

	public void createBooking(Booking booking) {

		try {
			Connection con = DBConnection.getConnection();
			String query = "INSERT INTO booking(bookingDate, travelers, totalAmount, status, customerId, packageId) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

			ps.setDate(1, java.sql.Date.valueOf(booking.getBookingDate()));
			ps.setInt(2, booking.getTravelers());
			ps.setDouble(3, booking.getTotalAmount());
			ps.setString(4, booking.getStatus());
			ps.setInt(5, booking.getCustomerId());
			ps.setInt(6, booking.getPackageId());

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
			if (rs.next()) {
				booking = new Booking();

				booking.setBookingId(rs.getInt("bookingId"));
				booking.setBookingDate(rs.getDate("bookingDate").toLocalDate());
				booking.setTravelers(rs.getInt("travelers"));
				booking.setTotalAmount(rs.getDouble("totalAmount"));
				booking.setStatus(rs.getString("status"));
				booking.setCustomerId(rs.getInt("customerId"));
				booking.setPackageId(rs.getInt("packageId"));
			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return booking;
	}

	public void updateBooking(Booking booking) {

		try {
			Connection con = DBConnection.getConnection();
			String query = "UPDATE booking SET bookingDate=?, travelers=?, totalAmount=? WHERE bookingId=?";
			PreparedStatement ps = con.prepareStatement(query);

			ps.setDate(1, java.sql.Date.valueOf(booking.getBookingDate()));
			ps.setInt(2, booking.getTravelers());
			ps.setDouble(3, booking.getTotalAmount());
			ps.setInt(4, booking.getBookingId());
			ps.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Booking> getAllBookings() {

		List<Booking> list = new ArrayList<>();

		try {
			Connection con = DBConnection.getConnection();
			String query = "SELECT b.bookingId, u.name, p.destination, b.bookingDate, b.status " + "FROM booking b "
					+ "JOIN users u ON b.customerId = u.userId " + "JOIN tour_package p ON b.packageId = p.packageId";

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Booking b = new Booking();

				b.setBookingId(rs.getInt("bookingId"));
				LocalDate date = rs.getObject("bookingDate", LocalDate.class);
				if (date != null) {
					b.setBookingDate(date);
				}
				b.setStatus(rs.getString("status"));

				list.add(b);

			}

		}
		catch (Exception e) {
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

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Booking> getBookingsByCustomerId(int customerId) {

		List<Booking> list = new ArrayList<>();

		try {
			Connection con = DBConnection.getConnection();
			String query = "SELECT b.*, p.destination FROM booking b "
			        + "JOIN tour_package p ON b.packageId = p.packageId WHERE b.customerId=?";
			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, customerId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				
				Booking b = new Booking();
				b.setBookingId(rs.getInt("bookingId"));
				b.setBookingDate(rs.getObject("bookingDate", LocalDate.class));
				b.setTravelers(rs.getInt("travelers"));
				b.setTotalAmount(rs.getDouble("totalAmount"));
				b.setStatus(rs.getString("status"));
				b.setCustomerId(rs.getInt("customerId"));
				b.setPackageId(rs.getInt("packageId"));
				b.setPackageName(rs.getString("destination"));

				list.add(b);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}