package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Itinerary;
import model.ItineraryItem;
import model.User;
import service.*;
import util.ColorText;

public class AdminController {

    Scanner sc = new Scanner(System.in);

    TourPackageService service        = new TourPackageService();
    MessageService messageService     = new MessageService();
    BookingService bookingService     = new BookingService();
    PaymentService paymentService     = new PaymentService();
    ItineraryService itineraryService = new ItineraryService();
    UserService userService           = new UserService();

    public void adminMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("           ADMIN DASHBOARD            ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1.  Manage Tour Packages            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2.  Message Customer                " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3.  Manage Bookings                 " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4.  View Payment History            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5.  Manage Itinerary                " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  6.  Exit                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));

            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                case 1: packageMenu();   break;
                case 2: messageMenu();   break;
                case 3: bookingMenu();   break;

                case 4:
                    System.out.print(ColorText.bold("  Enter Booking ID: "));
                    paymentService.viewPaymentHistory(Integer.parseInt(sc.nextLine()));
                    break;

                case 5: itineraryMenu(); break;

                case 6:
                    System.out.println(ColorText.yellow("\n  Logging out of Admin Dashboard..."));
                    return;

                default:
                    System.out.println(ColorText.error("\n  Invalid choice. Please enter 1-6."));
            }
        }
    }

    private void packageMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
            System.out.println(ColorText.warning("│") + ColorText.bold("         MANAGE TOUR PACKAGES        ") + ColorText.warning("│"));
            System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
            System.out.println("  1.  Add Package");
            System.out.println("  2.  View Packages");
            System.out.println("  3.  Update Package");
            System.out.println("  4.  Delete Package");
            System.out.println("  5.  Back");
            System.out.print(ColorText.bold("  Enter choice: "));

            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {

                case 1:
                    System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
                    System.out.println(ColorText.warning("│") + ColorText.bold("           ADD TOUR PACKAGE          ") + ColorText.warning("│"));
                    System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

                    System.out.print("  Package ID   : ");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.print("  Destination  : ");
                    String dest = sc.nextLine();

                    System.out.print("  Price (INR)  : ");
                    int price = Integer.parseInt(sc.nextLine());

                    System.out.print("  Duration     : ");
                    int duration = Integer.parseInt(sc.nextLine());

                    service.createPackage(id, dest, price, duration);
                    System.out.println(ColorText.success("\n  Package added successfully!"));
                    break;

                case 2:
                    System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
                    System.out.println(ColorText.warning("│") + ColorText.bold("           TOUR PACKAGES             ") + ColorText.warning("│"));
                    System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
                    service.displayPackages();
                    break;

                case 3:
                    System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
                    System.out.println(ColorText.warning("│") + ColorText.bold("         UPDATE TOUR PACKAGE         ") + ColorText.warning("│"));
                    System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

                    System.out.print("  Package ID      : ");
                    int uid = Integer.parseInt(sc.nextLine());

                    System.out.print("  New Destination : ");
                    String nd = sc.nextLine();

                    System.out.print("  New Price (INR) : ");
                    double np = Double.parseDouble(sc.nextLine());

                    System.out.print("  New Duration    : ");
                    int ndur = Integer.parseInt(sc.nextLine());

                    service.updatePackage(uid, nd, np, ndur);
                    System.out.println(ColorText.success("\n  Package updated successfully!"));
                    break;

                case 4:
                    System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
                    System.out.println(ColorText.warning("│") + ColorText.bold("         DELETE TOUR PACKAGE         ") + ColorText.warning("│"));
                    System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

                    System.out.print("  Package ID : ");
                    int deleteId = Integer.parseInt(sc.nextLine());

                    System.out.print(ColorText.warning("  Confirm delete? (yes/no) : "));
                    String confirm = sc.nextLine();

                    if(confirm.equalsIgnoreCase("yes")) {
                        service.deletePackage(deleteId);
                        System.out.println(ColorText.success("\n  Package deleted successfully!"));
                    } else {
                        System.out.println(ColorText.yellow("\n  Delete cancelled."));
                    }
                    break;

                case 5:
                    return;

                default:
                    System.out.println(ColorText.error("\n  Invalid choice."));
            }
        }
    }

    private void messageMenu() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("         MESSAGE CUSTOMER            ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
        System.out.println("  1.  View Messages from Customers");
        System.out.println("  2.  Reply to Customer");
        System.out.print(ColorText.bold("  Enter choice: "));

        int choice = Integer.parseInt(sc.nextLine());

        switch(choice) {

            case 1:
                List<String> messages = messageService.viewMessages();
                if(messages.isEmpty()) {
                    System.out.println(ColorText.yellow("\n  No new messages from customers."));
                } else {
                    System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
                    System.out.println(ColorText.warning("│") + ColorText.bold("       MESSAGES FROM CUSTOMERS       ") + ColorText.warning("│"));
                    System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
                    for(String m : messages) {
                        System.out.println("  " + m);
                        System.out.println(ColorText.warning("  ─────────────────────────────────────"));
                    }
                }
                break;

            case 2:
                List<User> users = userService.getAllUsers();
                System.out.println(ColorText.warning("\n  --- CUSTOMER LIST ---"));
                for(User u : users) {
                    System.out.println("  " + u.getUserId() + " - " + u.getName());
                }
                System.out.print(ColorText.bold("  Enter Customer ID : "));
                int cid = Integer.parseInt(sc.nextLine());

                System.out.print("  Reply              : ");
                String msg = sc.nextLine();

                messageService.replyToCustomer(cid, msg);
                System.out.println(ColorText.success("\n  Reply sent successfully!"));
                break;

            default:
                System.out.println(ColorText.error("\n  Invalid choice."));
        }
    }

    private void bookingMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
            System.out.println(ColorText.warning("│") + ColorText.bold("         MANAGE BOOKINGS             ") + ColorText.warning("│"));
            System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
            System.out.println("  1.  View Booking");
            System.out.println("  2.  Cancel Booking");
            System.out.println("  3.  Back");
            System.out.print(ColorText.bold("  Enter choice: "));

            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                case 1:
                    System.out.print(ColorText.bold("  Booking ID: "));
                    bookingService.viewBooking(Integer.parseInt(sc.nextLine()));
                    break;

                case 2:
                    System.out.print(ColorText.bold("  Booking ID: "));
                    bookingService.cancelBooking(Integer.parseInt(sc.nextLine()));
                    break;

                case 3:
                    return;

                default:
                    System.out.println(ColorText.error("\n  Invalid choice."));
            }
        }
    }

    private void itineraryMenu() {

        while(true) {

            System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
            System.out.println(ColorText.warning("│") + ColorText.bold("         MANAGE ITINERARY            ") + ColorText.warning("│"));
            System.out.println(ColorText.warning("└─────────────────────────────────────┘"));
            System.out.println("  1.  Add Itinerary");
            System.out.println("  2.  View Itinerary");
            System.out.println("  3.  Modify Itinerary");
            System.out.println("  4.  Delete Itinerary");
            System.out.println("  5.  Back");
            System.out.print(ColorText.bold("  Enter choice: "));

            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                case 1: addItinerary();    break;
                case 2: viewItinerary();   break;
                case 3: modifyItinerary(); break;
                case 4: deleteItinerary(); break;
                case 5: return;
                default: System.out.println(ColorText.error("\n  Invalid choice."));
            }
        }
    }

    private void addItinerary() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           ADD ITINERARY             ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print("  Package ID : ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("  Days       : ");
        int days = Integer.parseInt(sc.nextLine());

        List<ItineraryItem> items = new ArrayList<>();

        for(int i = 1; i <= days; i++) {
            System.out.println(ColorText.warning("\n  Day " + i));
            System.out.print("  Location : ");
            String loc = sc.nextLine();
            System.out.print("  Activity : ");
            String act = sc.nextLine();
            items.add(new ItineraryItem(0, i, act, loc));
        }

        itineraryService.createItinerary(new Itinerary(0, pid, 1, items));
    }

    private void viewItinerary() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("          VIEW ITINERARY             ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print("  Package ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        Itinerary it = itineraryService.viewItinerary(pid);

        if(it != null) {
            System.out.println(ColorText.bold("\n  ┌──────┬─────────────────┬───────────────────────────────────┐"));
            System.out.printf(ColorText.bold("  │ %-4s │ %-15s │ %-33s │%n"), "Day", "Location", "Activity");
            System.out.println(ColorText.bold("  ├──────┼─────────────────┼───────────────────────────────────┤"));
            for(ItineraryItem item : it.getItems()) {
                System.out.printf("  │ %-4d │ %-15s │ %-33s │%n",
                        item.getDayNumber(), item.getLocation(), item.getActivity());
            }
            System.out.println(ColorText.bold("  └──────┴─────────────────┴───────────────────────────────────┘"));
        } else {
            System.out.println(ColorText.error("\n  No itinerary found for this package."));
        }
    }

    private void modifyItinerary() {
        System.out.println(ColorText.warning("\n  Modifying itinerary replaces existing data."));
        addItinerary();
    }

    private void deleteItinerary() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("         DELETE ITINERARY            ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print("  Package ID : ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print(ColorText.warning("  Confirm delete? (yes/no) : "));
        String confirm = sc.nextLine();

        if(confirm.equalsIgnoreCase("yes")) {
            itineraryService.deleteItinerary(pid);
            System.out.println(ColorText.success("\n  Itinerary deleted successfully!"));
        } else {
            System.out.println(ColorText.yellow("\n  Delete cancelled."));
        }
    }
}