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

            int choice = sc.nextInt();

            switch(choice) {
                case 1:
                    System.out.print("Enter Package Id : ");
                    int id = sc.nextInt();
                    System.out.print("Enter Destination : ");
                    String destination = sc.next();
                    System.out.print("Enter Price : ");
                    int price = sc.nextInt();
                    System.out.print("Enter Duration(Days) : ");
                    int duration = sc.nextInt();
                    service.createPackage(id, destination, price, duration);
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
                    service.updatePackage(updateId, newDest, newPrice, newDuration);
                    break;

                case 4:
                    System.out.print("Enter Package ID to delete: ");
                    int deleteId = sc.nextInt();
                    System.out.print("Are you sure? (yes/no): ");
                    String confirm = sc.next();
                    if (confirm.equalsIgnoreCase("yes")) {
                        service.deletePackage(deleteId);
                    } else {
                        System.out.println("Package deletion cancelled");
                    }
                    break;

                case 5:
                    System.out.println("Exiting Admin Menu...");
                    return;

                default:
                    System.out.println("Invalid Choice");
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