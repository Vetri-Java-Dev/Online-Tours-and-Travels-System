package controller;

import java.util.Scanner;

import model.Booking;
import service.BookingService;
import service.TourPackageService;

public class CustomerController {

    private Scanner sc = new Scanner(System.in);
    
    private TourPackageService tourService = new TourPackageService();
    private BookingService bookingService = new BookingService();
    
    private int customerId;

    public CustomerController(int customerId) {
        this.customerId = customerId;
    }
    
    public CustomerController() {}

    public void customerMenu() {

        while (true) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1 View Tour Packages");
            System.out.println("2 Create Booking");
            System.out.println("3 View Booking");
            System.out.println("4 Cancel Booking");
            System.out.println("5 Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    tourService.displayPackages();
                    break;

                case 2:
                    createBooking(); 
                    break;

                case 3:
                    viewBooking();
                    break;

                case 4:
                    cancelBooking();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    
    private void createBooking() {
        System.out.print("Enter Package ID: ");
        int packageId = sc.nextInt();

        System.out.print("Enter Number of Travelers: ");
        int travelers = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Booking Date (YYYY-MM-DD): ");
        String bookingDate = sc.nextLine();

        Booking booking = new Booking();
        booking.setCustomerId(customerId);
        booking.setPackageId(packageId);
        booking.setTravelers(travelers);
        booking.setBookingDate(bookingDate);
        
        bookingService.createBooking(booking);
    }

    private void viewBooking() {
        System.out.print("Enter Booking ID: ");
        int bookingId = sc.nextInt();

        Booking booking = bookingService.viewBooking(bookingId);

        if (booking != null) {
            System.out.println("\n===== BOOKING DETAILS =====");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Package ID: " + booking.getPackageId());
            System.out.println("Travelers: " + booking.getTravelers());
            System.out.println("Booking Date: " + booking.getBookingDate());
            System.out.println("Total Amount: " + booking.getTotalAmount());
            System.out.println("Status: " + booking.getStatus());
        } else {
            System.out.println("Booking not found!");
        }
    }

    private void cancelBooking() {
        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = sc.nextInt();

        bookingService.cancelBooking(bookingId);
    }
}