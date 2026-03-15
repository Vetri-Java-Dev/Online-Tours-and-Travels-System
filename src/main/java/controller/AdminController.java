package controller;

import java.util.Scanner;

import service.TourPackageService;

public class AdminController {

    Scanner sc = new Scanner(System.in);
    TourPackageService service = new TourPackageService();

    public void adminMenu() {

        while(true) {

            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1 Add Tour Package");
            System.out.println("2 View Tour Packages");
            System.out.println("3 Exit");

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
                    System.out.println("Exiting Admin Menu...");
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}