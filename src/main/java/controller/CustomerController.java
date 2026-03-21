package controller;
import java.util.Scanner;

import model.Booking;
import model.CreditCardPayment;
import model.DebitCardPayment;
import model.Payment;
import model.UPIPayment;
import model.User;
import service.BookingService;
import service.PaymentService;
import service.TourPackageService;
import service.UserService;

public class CustomerController {

    private Scanner sc = new Scanner(System.in);
    
    private TourPackageService tourService = new TourPackageService();
    private BookingService bookingService = new BookingService();
    private UserService userService = new UserService();
    
    private int customerId;

    public CustomerController(int customerId) {
        this.customerId = customerId;
    }
    
    public CustomerController() {}

    public void customerMenu() {

        while(true) {

        		System.out.println("\n========================================");
        		System.out.println("           CUSTOMER DASHBOARD           ");
	        	System.out.println("========================================");
	        	System.out.println("1.  View Tour Packages");
	        	System.out.println("2.  Create Booking");
	        	System.out.println("3.  View Booking");
	        	System.out.println("4.  Cancel Booking");
	        	System.out.println("5.  View Profile");
	        	System.out.println("6.  View Payment History");
	        	System.out.println("7.  Exit");
	        	System.out.println("========================================");
	        	System.out.print("Enter your choice: ");
	        	
            int choice = sc.nextInt();

            switch(choice) {
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
                    viewProfile();
                    break;

                case 6:
                    viewPaymentHistory();
                    break;

                case 7:
                    return;
            }
        }
    }

    public void viewProfile() {

        System.out.println("===== CUSTOMER PROFILE =====");

        User user = userService.getUserById(customerId);

        if(user != null) {

            System.out.println("Name : " + user.getName());
            System.out.println("Email : " + user.getEmail());
            System.out.println("Phone : " + user.getPhone());

        }
        else {
            System.out.println("Profile not found");
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
        
        int bookingId = booking.getBookingId();
        double amount = booking.getTotalAmount();
        String date = java.time.LocalDate.now().toString();

        System.out.println("\n===== PAYMENT =====");
        System.out.println("1 UPI");
        System.out.println("2 Credit Card");
        System.out.println("3 Debit Card");

        int choice = sc.nextInt();
        sc.nextLine();

        Payment payment = null;

        switch(choice) {
            case 1:
                System.out.print("Enter UPI ID: ");
                String upiId = sc.nextLine();
                payment = new UPIPayment(amount, date, "SUCCESS", bookingId, upiId);
                break;

            case 2:
                System.out.print("Enter Card Number: ");
                String cc = sc.nextLine();

                System.out.print("Enter Card Holder Name: ");
                String name = sc.nextLine();

                payment = new CreditCardPayment(amount, date, "SUCCESS", bookingId, cc, name);
                break;

            case 3:
                System.out.print("Enter Card Number: ");
                String cardNumber = sc.nextLine();

                System.out.print("Enter Bank Name: ");
                String bank = sc.nextLine();

                payment = new DebitCardPayment(amount, date, "SUCCESS", bookingId, cardNumber, bank);
                break;

            default:
                System.out.println("Invalid choice");
                return;
        }

        PaymentService paymentService = new PaymentService();
        paymentService.processPayment(payment);
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
            
        }
        else {
            System.out.println("Booking not found!");
        }
    }

    private void viewPaymentHistory() {

        System.out.print("Enter Booking ID: ");
        int bookingId = sc.nextInt();

        PaymentService paymentService = new PaymentService();
        paymentService.viewPaymentHistory(bookingId);
    }
    
    private void cancelBooking() {
        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = sc.nextInt();

        bookingService.cancelBooking(bookingId);
    }
}