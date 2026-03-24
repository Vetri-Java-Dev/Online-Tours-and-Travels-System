package controller;

import java.util.Scanner;

import service.TourPackageService;

public class AdminController {

    Scanner sc = new Scanner(System.in);
    TourPackageService service = new TourPackageService();

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
                    System.out.print("Enter Package Id to Update : ");
                    int updateId = sc.nextInt();

                    System.out.print("Enter New Destination : ");
                    String newDestination = sc.next();

                    System.out.print("Enter New Price : ");
                    double newPrice = sc.nextDouble();

                    System.out.print("Enter New Duration(Days) : ");
                    int newDuration = sc.nextInt();

                    service.updatePackage(updateId, newDestination, newPrice, newDuration);
                    break;

                case 4:
                    System.out.print("Enter Package Id to Delete : ");
                    int deleteId = sc.nextInt();

                    System.out.print("Are you sure you want to delete package " + deleteId + "? (yes/no): ");
                    String confirm = sc.next();

                    if (confirm.equalsIgnoreCase("yes")) {
                        service.deletePackage(deleteId);
                    } else {
                        System.out.println("Delete cancelled.");
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
}