package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Itinerary;
import model.ItineraryItem;
import model.User;
import service.*;

public class AdminController {

    Scanner sc = new Scanner(System.in);

    TourPackageService service       = new TourPackageService();
    MessageService messageService    = new MessageService();
    BookingService bookingService    = new BookingService();
    PaymentService paymentService    = new PaymentService();
    ItineraryService itineraryService = new ItineraryService();
    UserService userService          = new UserService();

    // ================= MAIN MENU =================
    public void adminMenu() {

        while(true) {
            System.out.println("\n========================================");
            System.out.println("            ADMIN DASHBOARD             ");
            System.out.println("========================================");
            System.out.println("1. Add Tour Package");
            System.out.println("2. View Tour Packages");
            System.out.println("3. Update Tour Package");
            System.out.println("4. Delete Tour Package");
            System.out.println("5. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice: ");


        while (true) {

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           ADMIN DASHBOARD            ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Manage Tour Packages            ║");
            System.out.println("║  2. Message Customer                ║");
            System.out.println("║  3. Manage Booking                  ║");
            System.out.println("║  4. View Payment History            ║");
            System.out.println("║  5. Manage Itinerary                ║");
            System.out.println("║  6. Exit                            ║");
            System.out.println("╚══════════════════════════════════════╝");


            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                case 1:
                    System.out.print("Enter Package Id : ");
                    int id = sc.nextInt();
                    System.out.print("Enter Destination : ");
                    sc.nextLine();;
                    String destination = sc.next();
                    System.out.print("Enter Price : ");
                    int price = sc.nextInt();
                    System.out.print("Enter Duration(Days) : ");
                    int duration = sc.nextInt();
                    service.createPackage(id, destination, price, duration);

            switch (choice) {

                case 1: packageMenu(); break;
                case 2: messageMenu(); break;
                case 3: bookingMenu(); break;

                case 4:
                    System.out.print("Enter Booking ID: ");
                    paymentService.viewPaymentHistory(Integer.parseInt(sc.nextLine()));
                    break;

                case 5: itineraryMenu(); break;

                case 6:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
            }
        }
        }
    }

    // ================= PACKAGE MENU =================
    private void packageMenu() {

        while (true) {

            System.out.println("\n===== MANAGE PACKAGES =====");
            System.out.println("1. Add");
            System.out.println("2. View");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("5. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    System.out.print("ID: ");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.print("Destination: ");
                    String dest = sc.nextLine();

                    System.out.print("Price: ");
                    int price = Integer.parseInt(sc.nextLine());

                    System.out.print("Duration: ");
                    int duration = Integer.parseInt(sc.nextLine());

                    service.createPackage(id, dest, price, duration);

                    break;

                case 2:
                    service.displayPackages();
                    break;

                case 3:
                    System.out.print("Enter Package ID to update: ");
                    int updateId = sc.nextInt();
                    System.out.print("Enter new Destination: ");
                    String newDest = sc.next();
                    System.out.print("Enter new Price: ");
                    int newPrice = sc.nextInt();
                    System.out.print("Enter new Duration(Days): ");
                    int newDuration = sc.nextInt();
                    boolean updated = service.updatePackage(updateId, newDest, newPrice, newDuration);
                    System.out.println(updated ? "Package updated!" : "Package not found.");
                    break;

                case 4:
                    System.out.print("ID: ");
                    service.deletePackage(Integer.parseInt(sc.nextLine()));
                    break;

                case 5:
                    System.out.println("Exiting Admin Menu...");
                    return;

                default:
                    System.out.println("Invalid Choice");
                    System.out.print("ID: ");
                    int uid = Integer.parseInt(sc.nextLine());

                    System.out.print("New Destination: ");
                    String nd = sc.nextLine();

                    System.out.print("New Price: ");
                    double np = Double.parseDouble(sc.nextLine());

                    System.out.print("New Duration: ");
                    int ndur = Integer.parseInt(sc.nextLine());

                    service.updatePackage(uid, nd, np, ndur);
                    break;



            }
        }
    }

    // ================= MESSAGE MENU =================
    private void messageMenu() {

        System.out.println("\n1. View Messages");
        System.out.println("2. Send Message");
        int choice = Integer.parseInt(sc.nextLine());

        switch (choice) {

            case 1:
                List<String> messages = messageService.viewMessages();
                messages.forEach(System.out::println);
                break;

            case 2:
                List<User> users = userService.getAllUsers();

                System.out.println("\n--- CUSTOMER LIST ---");
                for (User u : users) {
                    System.out.println(u.getUserId() + " - " + u.getName());
                }

                System.out.print("Enter Customer ID: ");
                int cid = Integer.parseInt(sc.nextLine());

                System.out.print("Message: ");
                String msg = sc.nextLine();

                messageService.replyToCustomer(cid, msg);
                break;
        }
    }

    // ================= BOOKING MENU =================
    private void bookingMenu() {

        while (true) {

            System.out.println("\n===== MANAGE BOOKINGS =====");
            System.out.println("1. View Booking");
            System.out.println("2. Cancel Booking");
            System.out.println("3. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    System.out.print("Booking ID: ");
                    bookingService.viewBooking(Integer.parseInt(sc.nextLine()));
                    break;

                case 2:
                    System.out.print("Booking ID: ");
                    bookingService.cancelBooking(Integer.parseInt(sc.nextLine()));
                    break;

                case 3:
                    return;
            }
        }
    }

    // ================= ITINERARY MENU =================
    private void itineraryMenu() {

        while (true) {

            System.out.println("\n===== MANAGE ITINERARY =====");
            System.out.println("1. Add");
            System.out.println("2. View");
            System.out.println("3. Modify");
            System.out.println("4. Delete");
            System.out.println("5. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1: addItinerary(); break;
                case 2: viewItinerary(); break;
                case 3: modifyItinerary(); break;
                case 4: deleteItinerary(); break;
                case 5: return;
            }
        }
    }

    // ================= ITINERARY METHODS =================

    private void addItinerary() {

        System.out.print("Package ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("Days: ");
        int days = Integer.parseInt(sc.nextLine());

        List<ItineraryItem> items = new ArrayList<>();

        for (int i = 1; i <= days; i++) {

            System.out.println("Day " + i);

            System.out.print("Location: ");
            String loc = sc.nextLine();

            System.out.print("Activity: ");
            String act = sc.nextLine();

            items.add(new ItineraryItem(0, i, act, loc));
        }

        itineraryService.createItinerary(new Itinerary(0, pid, 1, items));
    }

    private void viewItinerary() {

        System.out.print("Package ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        Itinerary it = itineraryService.viewItinerary(pid);

        if (it != null) {
            for (ItineraryItem item : it.getItems()) {
                System.out.println("Day " + item.getDayNumber() +
                        " | " + item.getLocation() +
                        " | " + item.getActivity());
            }
        } else {
            System.out.println("No itinerary found.");
        }
    }

    private void modifyItinerary() {
        addItinerary(); // simple replace logic
    }

    private void deleteItinerary() {
        System.out.print("Package ID: ");
        itineraryService.deleteItinerary(Integer.parseInt(sc.nextLine()));
    }
    
}