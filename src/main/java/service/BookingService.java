package service;

import dao.BookingDAO;
import dao.TourPackageDAO;
import dao.UserDAO;
import model.Booking;
import model.TourPackage;
import model.User;
import util.EmailUtil;

public class BookingService {

    private BookingDAO bookingDAO = new BookingDAO();
    private TourPackageDAO tourPackageDAO = new TourPackageDAO();

    public void createBooking(Booking booking) {

        System.out.println("\n========================================");
        System.out.println("            CREATE BOOKING              ");
        System.out.println("========================================");

        if (booking.getBookingDate() == null || booking.getBookingDate().isEmpty()) {
            System.out.println("Booking date cannot be empty.");
            return;
        }

        if (booking.getTravelers() <= 0) {
            System.out.println("Number of travelers must be greater than 0.");
            return;
        }

        TourPackage tourPackage = tourPackageDAO.getPackageById(booking.getPackageId());

        if (tourPackage == null) {
            System.out.println("Invalid Package ID.");
            return;
        }

        double totalAmount = tourPackage.getPrice() * booking.getTravelers();
        booking.setTotalAmount(totalAmount);

        booking.setStatus("CONFIRMED");

        bookingDAO.createBooking(booking);

        System.out.println("\n----------------------------------------");
        System.out.println("Booking created successfully!");
        System.out.println("Booking ID   : " + booking.getBookingId());
        System.out.println("Total Amount : " + booking.getTotalAmount());
        System.out.println("Status       : " + booking.getStatus());
        System.out.println("----------------------------------------");

        User user = new UserDAO().getUserById(booking.getCustomerId());
        if (user != null) {
            EmailUtil.sendBookingConfirmationEmail(
                user.getEmail(),
                user.getName(),
                booking.getBookingId(),
                booking.getPackageId(),
                booking.getTravelers(),
                booking.getTotalAmount(),
                booking.getBookingDate()
            );
            
            EmailUtil.sendAdminBookingAlertEmail(
                    "onlinetats@gmail.com",
                    user.getName(),
                    user.getUserId(),
                    booking.getBookingId(),
                    booking.getPackageId(),
                    booking.getTravelers(),
                    booking.getTotalAmount(),
                    booking.getBookingDate()
                );
        }
    }

    public Booking viewBooking(int bookingId) {

        System.out.println("\n========================================");
        System.out.println("            VIEW BOOKING                ");
        System.out.println("========================================");

        if (bookingId <= 0) {
            System.out.println("Invalid Booking ID.");
            return null;
        }

        return bookingDAO.viewBooking(bookingId);
    }

    public void cancelBooking(int bookingId) {

        System.out.println("\n========================================");
        System.out.println("           CANCEL BOOKING               ");
        System.out.println("========================================");

        if (bookingId <= 0) {
            System.out.println("Invalid Booking ID.");
            return;
        }

        bookingDAO.cancelBooking(bookingId);

        Booking booking = bookingDAO.viewBooking(bookingId);
        
        if (booking != null) {
            User user = new UserDAO().getUserById(booking.getCustomerId());
            if (user != null) {
                EmailUtil.sendCancellationEmail(user.getEmail(), user.getName(), bookingId);
                EmailUtil.sendAdminCancellationAlertEmail("onlinetats@gmail.com",user.getName(),user.getUserId(),bookingId);
            }
        }
    }
}