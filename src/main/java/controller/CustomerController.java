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
	        	System.out.println("8.  Search / Filter Packages");
	        	System.out.println("9.  Update Profile");
	        	System.out.println("10. View Booking History");
	        	System.out.println("11. Delete Account");
	        	System.out.println("12. Exit");
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
                	    updateProfile();
                	    break;
                	case 10:
                	    viewBookingHistory();
                	    break;
                	case 11:
                	    deleteAccount();
                	    return;
                	case 12:
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
        System.out.println("\n===== SEARCH / FILTER PACKAGES =====");
        System.out.println("1. Search by Destination");
        System.out.println("2. Filter by Price Range");
        System.out.println("3. View Available Packages");
        System.out.println("4. Sort by Price (Low to High)");
        System.out.print("Enter choice: ");

        int option = sc.nextInt();
        sc.nextLine();

        if (option == 1) {
            System.out.print("Enter destination: ");
            String dest = sc.nextLine();
            List<TourPackage> list = tourService.searchByDestination(dest);
            if (list.isEmpty()) {
                System.out.println("No packages found for destination");
            } else {
                printPackageList(list);
            }

        } else if (option == 2) {
            System.out.print("Enter minimum price: ");
            double minPrice = sc.nextDouble();
            System.out.print("Enter maximum price: ");
            double maxPrice = sc.nextDouble();
            List<TourPackage> all = tourService.getAllPackages();
            List<TourPackage> filtered = new ArrayList<>();
            for (TourPackage tp : all) {
                if (tp.getPrice() >= minPrice && tp.getPrice() <= maxPrice) {
                    filtered.add(tp);
                }
            }
            if (filtered.isEmpty()) {
                System.out.println("No packages available in this price range");
            } else {
                printPackageList(filtered);
            }

        } else if (option == 3) {
            List<TourPackage> list = tourService.getAvailablePackages();
            if (list.isEmpty()) {
                System.out.println("No available packages");
            } else {
                printPackageList(list);
            }

        } else if (option == 4) {
            List<TourPackage> list = tourService.getPackagesSortedByPrice();
            if (list.isEmpty()) {
                System.out.println("No packages available to sort");
            } else {
                System.out.println("\n===== PACKAGES (Low to High Price) =====");
                printPackageList(list);
            }
        }
    }

    private void printPackageList(List<TourPackage> list) {
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-5s %-30s %-10s %-10s%n", "ID", "Destination", "Price", "Duration");
        System.out.println("----------------------------------------------------------");
        for (TourPackage tp : list) {
            System.out.printf("%-5d %-30s %-10.2f %-10d days%n",
                tp.getPackageId(), tp.getDestination(), tp.getPrice(), tp.getDuration());
        }
        System.out.println("----------------------------------------------------------");
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

         System.out.println("\nItinerary not available for Package ID : " + packageId);
     }
 }
 
 public void updateProfile() {
	    System.out.println("\n===== UPDATE PROFILE =====");
	    System.out.print("Enter new Name: ");
	    sc.nextLine();
	    String name = sc.nextLine();
	    System.out.print("Enter new Phone: ");
	    String phone = sc.nextLine();

	    if (name.isEmpty() || phone.isEmpty()) {
	        System.out.println("Invalid input - Please enter valid details");
	        return;
	    }
	    boolean updated = userService.updateUser(customerId, name, phone);
	    if (updated) {
	        System.out.println("Profile updated successfully");
	    } else {
	        System.out.println("Update failed. Please try again.");
	    }
	}

	public void viewBookingHistory() {
	    List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);
	    if (bookings == null || bookings.isEmpty()) {
	        System.out.println("No bookings found");
	    } else {
	        System.out.println("\n===== BOOKING HISTORY =====");
	        System.out.printf("%-12s %-12s %-15s %-10s%n", "BookingID", "PackageID", "Date", "Status");
	        System.out.println("--------------------------------------------------");
	        for (Booking b : bookings) {
	            System.out.printf("%-12d %-12d %-15s %-10s%n",
	                b.getBookingId(), b.getPackageId(), b.getBookingDate(), b.getStatus());
	        }
	    }
	}

	public void deleteAccount() {
	    System.out.println("\n===== DELETE ACCOUNT =====");
	    System.out.print("Are you sure you want to delete your account? (yes/no): ");
	    sc.nextLine();
	    String confirm = sc.nextLine().trim().toLowerCase();
	    if (confirm.equals("yes")) {
	        boolean deleted = userService.deleteUser(customerId);
	        if (deleted) {
	            System.out.println("Account deleted successfully");
	        } else {
	            System.out.println("Failed to delete account.");
	        }
	    } else {
	        System.out.println("Account deletion cancelled");
	    }
	}

    }