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

    private BookingDAO bookingDAO         = new BookingDAO();
    private TourPackageDAO tourPackageDAO = new TourPackageDAO();

    public void createBooking(Booking booking) {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           CREATE BOOKING            │");
        System.out.println("└─────────────────────────────────────┘");

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

        } catch (Exception e) {
            System.out.println("  Invalid date format! Use YYYY-MM-DD.");
            return;
        }

        if (booking.getTravelers() <= 0) {
            System.out.println("  Travelers must be greater than 0.");
            return;
        }
        TourPackage tourPackage = tourPackageDAO.getPackageById(booking.getPackageId());
  
        if (booking.getTravelers() > tourPackage.getAvailableSeats()) {
            System.out.println("Seats exceeded! Available seats: " + tourPackage.getAvailableSeats());
            return;
        }

        double totalAmount = tourPackage.getPrice() * booking.getTravelers();
        booking.setTotalAmount(totalAmount);
        booking.setStatus("CONFIRMED");
        User user = new UserDAO().getUserById(booking.getCustomerId());
        TourPackage tourPackageDetails = tourPackageDAO.getPackageById(booking.getPackageId());

        if (user != null && tourPackageDetails != null) {
            booking.setCustomerName(user.getName());
            booking.setPackageName(tourPackageDetails.getDestination());
        }
        bookingDAO.createBooking(booking);
        int remainingSeats = tourPackage.getAvailableSeats() - booking.getTravelers();
        tourPackageDAO.updateAvailableSeats(booking.getPackageId(), remainingSeats);
        System.out.println("  Booking created successfully!");
        System.out.println("  ─────────────────────────────────────");
        System.out.println("  Booking ID   : " + booking.getBookingId());
        System.out.println("  Package ID   : " + booking.getPackageId());
        System.out.println("  Travelers    : " + booking.getTravelers());
        System.out.printf ("  Total Amount : Rs. %.2f%n", booking.getTotalAmount());
        System.out.println("  Status       : " + booking.getStatus());
        System.out.println("  ─────────────────────────────────────");

        User user2 = new UserDAO().getUserById(booking.getCustomerId());
        if (user2 != null) {
            EmailUtil.sendBookingConfirmationEmail(
                user2.getEmail(), user2.getName(),
                booking.getBookingId(), booking.getPackageId(),
                booking.getTravelers(), booking.getTotalAmount(),
                booking.getBookingDate().toString()
            );   
            EmailUtil.sendAdminBookingAlertEmail(
                "onlinetats@gmail.com", user.getName(), user.getUserId(),
                booking.getBookingId(), booking.getPackageId(),
                booking.getTravelers(), booking.getTotalAmount(),
                booking.getBookingDate().toString()  );
        }
    }

    public Booking viewBooking(int bookingId) {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│            VIEW BOOKING             │");
        System.out.println("└─────────────────────────────────────┘");

        if (bookingId <= 0) {
            System.out.println("  Invalid Booking ID.");
            return null;
        }

        Booking booking = bookingDAO.viewBooking(bookingId);

        if (booking != null) {
            System.out.println("  ─────────────────────────────────────");
            System.out.println("  Booking ID   : " + booking.getBookingId());
            System.out.println("  Package ID   : " + booking.getPackageId());
            System.out.println("  Travelers    : " + booking.getTravelers());
            System.out.println("  Booking Date : " + booking.getBookingDate());
            System.out.printf ("  Total Amount : Rs. %.2f%n", booking.getTotalAmount());
            System.out.println("  Status       : " + booking.getStatus());
            System.out.println("  ─────────────────────────────────────");
        }
        else {
            System.out.println("  Booking not found.");
        }

        return booking;
    }
    public void modifyBooking(Booking booking) {

        if (booking.getBookingId() <= 0) {
            System.out.println("Invalid Booking ID");
            return;
        }

        if (booking.getBookingDate() == null ) {
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

        } catch (Exception e) {
            System.out.println("  Invalid date format! Use YYYY-MM-DD.");
            return;
        }

        if (booking.getTravelers() <= 0) {
            System.out.println("  Travelers must be greater than 0.");
            return;
        }

      
        TourPackage tourPackage = tourPackageDAO.getPackageById(booking.getPackageId());

        if (tourPackage == null) {
            System.out.println("Invalid Package ID");
            return;
        }

        double totalAmount = tourPackage.getPrice() * booking.getTravelers();
        booking.setTotalAmount(totalAmount);

        bookingDAO.updateBooking(booking);

    }
    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
   

    public void cancelBooking(int bookingId) {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           CANCEL BOOKING            │");
        System.out.println("└─────────────────────────────────────┘");

        if (bookingId <= 0) {
            System.out.println("  Invalid Booking ID.");
            return;
        }

        bookingDAO.cancelBooking(bookingId);

        Booking booking = bookingDAO.viewBooking(bookingId);

        System.out.println("  Booking cancelled successfully!");
        System.out.println("  ─────────────────────────────────────");
        System.out.println("  Booking ID : " + bookingId);

        if (booking != null) {
            System.out.println("  Status     : " + booking.getStatus());
        }

        System.out.println("  ─────────────────────────────────────");

        if (booking != null) {
            User user = new UserDAO().getUserById(booking.getCustomerId());
            if (user != null) {
                EmailUtil.sendCancellationEmail(user.getEmail(), user.getName(), bookingId);
                EmailUtil.sendAdminCancellationAlertEmail(
                    "onlinetats@gmail.com", user.getName(), user.getUserId(), bookingId
                );
            }
        }
    }
    public List<Booking> getBookingsByCustomerId(int customerId) {
        return bookingDAO.getBookingsByCustomerId(customerId);
    }
}