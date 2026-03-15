package service;

import dao.BookingDAO;
import model.Booking;

public class BookingService {

    private BookingDAO bookingDAO = new BookingDAO();

    public void createBooking(Booking booking) {

        if (booking.getStatus() == null) {
            System.out.println("Booking status cannot be empty");
            return;
        }

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