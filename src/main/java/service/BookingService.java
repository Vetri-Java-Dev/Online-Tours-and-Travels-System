package service;

import dao.BookingDAO;
import dao.TourPackageDAO;
import model.Booking;
import model.TourPackage;

public class BookingService {

    private BookingDAO bookingDAO = new BookingDAO();
    private TourPackageDAO tourPackageDAO = new TourPackageDAO();

    public void createBooking(Booking booking) {

        if (booking.getBookingDate() == null || booking.getBookingDate().isEmpty()) {
            System.out.println("Booking date cannot be empty");
            return;
        }

        if (booking.getTravelers() <= 0) {
            System.out.println("Number of travelers must be greater than 0");
            return;
        }

        TourPackage tourPackage = tourPackageDAO.getPackageById(booking.getPackageId());
        if (tourPackage == null) {
            System.out.println("Invalid Package ID");
            return;
        }

        double totalAmount = tourPackage.getPrice() * booking.getTravelers();
        booking.setTotalAmount(totalAmount);

        booking.setStatus("Confirmed");

        bookingDAO.createBooking(booking);
    }

    public Booking viewBooking(int bookingId) {

        if (bookingId <= 0) {
            System.out.println("Invalid Booking ID");
            return null;
        }

        return bookingDAO.viewBooking(bookingId);
    }

    public void cancelBooking(int bookingId) {

        if (bookingId <= 0) {
            System.out.println("Invalid Booking ID");
            return;
        }

        bookingDAO.cancelBooking(bookingId);
    }
}