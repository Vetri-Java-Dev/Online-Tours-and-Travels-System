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
            System.out.println("3. Message Customer");   // ← renamed
            System.out.println("4. Exit");
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
                    messageMenu();  
                    break;

                case 4:
                    System.out.println("Exiting Admin Menu...");
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void messageMenu() {

        System.out.println("\n========================================");
        System.out.println("           MESSAGE CUSTOMER             ");
        System.out.println("========================================");
        System.out.println("1. View Messages from Customers");
        System.out.println("2. Send Reply to Customer");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {

            case 1:
                List<String> messages = messageService.viewMessages();

                if (messages.isEmpty()) {
                    System.out.println("\nNo new messages from customers.");
                }
                else {
                    System.out.println("\n===== MESSAGES FROM CUSTOMERS =====");
                    for (String m : messages) {
                        System.out.println(m);
                        System.out.println("-----------------------------------");
                    }
                }
                break;

            case 2:
                System.out.print("Enter Customer ID to reply: ");
                int customerId = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter Reply: ");
                String reply = sc.nextLine();

                messageService.replyToCustomer(customerId, reply);
                break;

            default:
                System.out.println("Invalid Choice");
        }
    }
}