package controller;
import java.util.*;

import model.Booking;
import model.CreditCardPayment;
import model.DebitCardPayment;
import model.Payment;
import model.UPIPayment;
import model.User;
import model.TourPackage;
import comparator.PriceComparator;
import comparator.DurationComparator;
import service.BookingService;
import service.PaymentService;
import service.TourPackageService;
import service.UserService;
import service.ItineraryService;
import model.Itinerary;
import model.ItineraryItem;

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
	        	System.out.println("2.  View Package Itinerary");
	        	System.out.println("3.  Create Booking");
	        	System.out.println("4.  View Booking");
	        	System.out.println("5.  Cancel Booking");
	        	System.out.println("6.  View Profile");
	        	System.out.println("7.  View Payment History");
	        	System.out.println("8.  Search Package");
	        	System.out.println("9.  Exit");
	        	System.out.println("========================================");
	        	System.out.print("Enter your choice: ");
	        	
            int choice = sc.nextInt();

            switch(choice) {
            		case 1:
            			tourService.displayPackages();
            			break;
            			
            		case 2:
            		    viewItinerary();
            		    break;	
            
            		case 3:
            			createBooking();
            			break;

            		case 4:
            			viewBooking();
            			break;

            		case 5:
            			cancelBooking();
            			break;
            			
                    case 6:
                       viewProfile();
                       break;

                   case 7:
                       viewPaymentHistory();
                       break;
                    
                   case 8:
                	   searchPackage();
                	   break;

                   case 9:
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

        System.out.print("Enter payment choice : ");
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
    public void searchPackage() {

        System.out.println("\n===== SEARCH PACKAGE =====");
        System.out.println("1. Search by Destination");
        System.out.println("2. Filter Packages");
        System.out.print("Enter choice: ");

        int option = sc.nextInt();
        sc.nextLine();

        if(option == 1) {

            System.out.print("Enter destination: ");
            String dest = sc.nextLine();

            List<TourPackage> list = tourService.searchByDestination(dest);

            if(list.isEmpty()) {
                System.out.println("No packages found!");
            } else {
                System.out.println("\n===== SEARCH RESULTS =====");
                for(TourPackage tp : list) {
                    System.out.println(
                        tp.getPackageId() + " | " +
                        tp.getDestination() + " | " +
                        tp.getPrice() + " | " +
                        tp.getDuration() + " days"
                    );
                }
            }

        } else if(option == 2) {

            System.out.println("\n1. Filter by Price");
            System.out.println("2. Filter by Title");
            System.out.print("Enter choice: ");

            int filterChoice = sc.nextInt();
            sc.nextLine();

            List<TourPackage> list = tourService.getAllPackages();

            if(filterChoice == 1) {
                Collections.sort(list, new PriceComparator());
            } else if(filterChoice == 2) {
                Collections.sort(list, new DurationComparator());
            }

            System.out.println("\n===== FILTERED RESULTS =====");

            for(TourPackage tp : list) {
                System.out.println(
                    tp.getPackageId() + " | " +
                    tp.getDestination() + " | " +
                    tp.getPrice() + " | " +
                    tp.getDuration() + " days"
                );
            }
        }
    }
 private void viewItinerary() {

     System.out.println("\n========================================");
     System.out.println("        VIEW PACKAGE ITINERARY          ");
     System.out.println("========================================");

     System.out.print("Enter Package ID to view Itinerary : ");
     int packageId = sc.nextInt();

     ItineraryService itineraryService = new ItineraryService();

     Itinerary itinerary = itineraryService.viewItinerary(packageId);

     if (itinerary != null) {

         List<ItineraryItem> items = itinerary.getItems();

         // AC2 + AC3 - Display itinerary with day-wise schedule
         System.out.println("\n--- ITINERARY DETAILS ---");
         System.out.println("Package ID  : " + itinerary.getPackageId());
         System.out.println("Total Days  : " + items.size());
         System.out.println();
         System.out.println("--- DAY-WISE SCHEDULE ---");
         System.out.println("----------------------------------------------------------");
         System.out.printf("%-5s %-15s %-35s%n", "Day", "Location", "Activity");
         System.out.println("----------------------------------------------------------");

         for (ItineraryItem item : items) {
             System.out.printf("%-5d %-15s %-35s%n",
                 item.getDayNumber(),
                 item.getLocation(),
                 item.getActivity());
         }

         System.out.println("----------------------------------------------------------");

     } else {

         // Invalid Case - Itinerary not found
         System.out.println("\nItinerary not available for Package ID : " + packageId);
     }
 }

    }