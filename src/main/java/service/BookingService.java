/*
 * Author         : Harini R G
 * Description    : BookingService acts as a service layer that handles business 
 *                  logic for booking operations such as creating, viewing, modifying, 
 *                  cancelling bookings, validating inputs, calculating total amount, 
 *                  updating seat availability, and sending email notifications.
 * Module         : Booking Module (Service Layer)
 * Java version   : 25
 */
package service;
import dao.BookingDAO;
import dao.TourPackageDAO;
import dao.UserDAO;
import model.TourPackage;
import model.User;
import util.EmailUtil;
import java.time.LocalDate;
import java.util.List;
import model.Booking;

public class BookingService {

	private BookingDAO bookingDAO = new BookingDAO();
	private TourPackageDAO tourPackageDAO = new TourPackageDAO();

	public boolean createBooking(Booking booking) {

		if (booking.getBookingDate() == null) {
			System.out.println("  Booking date cannot be empty.");
			return false;
		}
		try {
			LocalDate bookingDate = booking.getBookingDate();
			LocalDate today = LocalDate.now();
			if (bookingDate.isBefore(today)) {
				System.out.println("  Booking date cannot be in the past.");
				return false;
			}

		} 
		catch (Exception e) {
			System.out.println("  Invalid date format! Use YYYY-MM-DD.");
			return false;
		}

		if (booking.getTravelers() <= 0) {
			System.out.println("  Travelers must be greater than 0.");
			return false;
		}

		TourPackage tourPackage = tourPackageDAO.getPackageById(booking.getPackageId());

		if (tourPackage == null) {
			System.out.println("  Invalid Package ID");
			return false;
		}

		if (booking.getTravelers() > tourPackage.getAvailableSeats()) {
			System.out.println("  Seats exceeded! Available seats: " + tourPackage.getAvailableSeats());
			return false;
		}

		double totalAmount = tourPackage.getPrice() * booking.getTravelers();
		booking.setTotalAmount(totalAmount);
		booking.setStatus("CONFIRMED");

		User user = new UserDAO().getUserById(booking.getCustomerId());
		
		bookingDAO.createBooking(booking);
		
		int remainingSeats = tourPackage.getAvailableSeats() - booking.getTravelers();
		tourPackageDAO.updateAvailableSeats(booking.getPackageId(), remainingSeats);

		if (user != null) {
			EmailUtil.sendBookingConfirmationEmail(user.getEmail(), user.getName(), booking.getBookingId(),
					booking.getPackageId(), booking.getTravelers(), booking.getTotalAmount(),
					booking.getBookingDate().toString());
			
			EmailUtil.sendAdminBookingAlertEmail("onlinetats@gmail.com", user.getName(), user.getUserId(),
					booking.getBookingId(), booking.getPackageId(), booking.getTravelers(), booking.getTotalAmount(),
					booking.getBookingDate().toString());
		}
		return true;

	}

	public Booking viewBooking(int bookingId) {
		if (bookingId <= 0) {
			System.out.println("  Invalid Booking ID");
			return null;
		}

		return bookingDAO.viewBooking(bookingId);
	}
	 public Booking viewBookingByCustomer(int bookingId, int customerId) {
		 
	        Booking booking = bookingDAO.viewBooking(bookingId);
	        if (booking == null) {
	            System.out.println("Booking not found!");
	            return null;
	        }
	        if (booking.getCustomerId() != customerId) {
	            System.out.println("Access Denied! This booking does not belong to you.");
	            return null;
	        }

	        return booking;
	    }


	public void modifyBooking(Booking booking) {

		if (booking.getBookingId() <= 0) {
			System.out.println("  Invalid Booking ID");
			return;
		}
		if (booking.getBookingDate() == null) {
			System.out.println("  Booking date cannot be empty.");
			return;
		}

		try {
			LocalDate bookingDate = booking.getBookingDate();
			LocalDate today = LocalDate.now();
			if (bookingDate.isBefore(today)) {
				System.out.println("  Booking date cannot be in the past.");
				return;
			}

		}
		catch (Exception e) {
			System.out.println("  Invalid date format! Use YYYY-MM-DD.");
			return;
		}
		if (booking.getTravelers() <= 0) {
			System.out.println("  Travelers must be greater than 0.");
			return;
		}

		TourPackage tourPackage = tourPackageDAO.getPackageById(booking.getPackageId());

		if (tourPackage == null) {
			System.out.println("  Invalid Package ID");
			return;
		}

		double totalAmount = tourPackage.getPrice() * booking.getTravelers();
		booking.setTotalAmount(totalAmount);
		bookingDAO.updateBooking(booking);

	}

	public List<Booking> getAllBookings() {
		return bookingDAO.getAllBookings();
	}

	public boolean cancelBooking(int bookingId) {

		if (bookingId <= 0) {
			return false;
		}
		bookingDAO.cancelBooking(bookingId);
		Booking booking = bookingDAO.viewBooking(bookingId);

		if (booking != null) {
			User user = new UserDAO().getUserById(booking.getCustomerId());
			if (user != null) {
				EmailUtil.sendCancellationEmail(user.getEmail(), user.getName(), bookingId);
				EmailUtil.sendAdminCancellationAlertEmail("onlinetats@gmail.com", user.getName(), user.getUserId(),
						bookingId);
			}
		}

		return true;
	}

	public List<Booking> getBookingsByCustomerId(int customerId) {
		return bookingDAO.getBookingsByCustomerId(customerId);
	}
}