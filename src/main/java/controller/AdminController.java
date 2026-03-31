package controller;

import java.util.List;
import java.util.Scanner;

import service.MessageService;
import service.TourPackageService;

public class AdminController {

    Scanner sc = new Scanner(System.in);
    TourPackageService service    = new TourPackageService();
    MessageService messageService = new MessageService();

    public void adminMenu() {

        while(true) {

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           ADMIN DASHBOARD            ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1.  Add Tour Package                ║");
            System.out.println("║  2.  View Tour Packages              ║");
            System.out.println("║  3.  Update Tour Package             ║");
            System.out.println("║  4.  Delete Tour Package             ║");
            System.out.println("║  5.  Message Customer                ║");
            System.out.println("║  6.  Exit                            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = sc.nextInt();

            switch(choice) {

                case 1:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│           ADD TOUR PACKAGE          │");
                    System.out.println("└─────────────────────────────────────┘");

                    System.out.print("  Package ID   : ");
                    int id = sc.nextInt();

                    System.out.print("  Destination  : ");
                    String destination = sc.next();

                    System.out.print("  Price (INR)  : ");
                    int price = sc.nextInt();

                    System.out.print("  Duration     : ");
                    int duration = sc.nextInt();

                    service.createPackage(id, destination, price, duration);
                    System.out.println("\n  Package added successfully!");
                    break;

                case 2:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│           TOUR PACKAGES             │");
                    System.out.println("└─────────────────────────────────────┘");
                    service.displayPackages();
                    break;

                case 3:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│         UPDATE TOUR PACKAGE         │");
                    System.out.println("└─────────────────────────────────────┘");

                    System.out.print("  Package ID      : ");
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("  New Destination : ");
                    String newDestination = sc.nextLine();

                    System.out.print("  New Price (INR) : ");
                    double newPrice = sc.nextDouble();

                    System.out.print("  New Duration    : ");
                    int newDuration = sc.nextInt();

                    service.updatePackage(updateId, newDestination, newPrice, newDuration);
                    System.out.println("\n  Package updated successfully!");
                    break;

                case 4:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│         DELETE TOUR PACKAGE         │");
                    System.out.println("└─────────────────────────────────────┘");

                    System.out.print("  Package ID : ");
                    int deleteId = sc.nextInt();

                    System.out.print("  Confirm delete? (yes/no) : ");
                    String confirm = sc.next();

                    if (confirm.equalsIgnoreCase("yes")) {
                        service.deletePackage(deleteId);
                        System.out.println("\n  Package deleted successfully!");
                    } else {
                        System.out.println("\n  Delete cancelled.");
                    }
                    break;

                case 5:
                    messageMenu();
                    break;

                case 6:
                    System.out.println("\n  Logging out of Admin Dashboard...");
                    return;

                default:
                    System.out.println("\n  Invalid choice. Please enter 1-6.");
            }
        }
    }

    private void messageMenu() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│         MESSAGE CUSTOMER            │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  1.  View Messages from Customers");
        System.out.println("  2.  Reply to Customer");
        System.out.print("  Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {

            case 1:
                List<String> messages = messageService.viewMessages();

                if (messages.isEmpty()) {
                    System.out.println("\n  No new messages from customers.");
                } else {
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│       MESSAGES FROM CUSTOMERS       │");
                    System.out.println("└─────────────────────────────────────┘");
                    for (String m : messages) {
                        System.out.println("  " + m);
                        System.out.println("  ─────────────────────────────────────");
                    }
                }
                break;

            case 2:
                System.out.print("  Customer ID : ");
                int customerId = sc.nextInt();
                sc.nextLine();

                System.out.print("  Reply       : ");
                String reply = sc.nextLine();

                messageService.replyToCustomer(customerId, reply);
                System.out.println("\n  Reply sent successfully!");
                break;

            default:
                System.out.println("\n  Invalid choice.");
        }
    }
}