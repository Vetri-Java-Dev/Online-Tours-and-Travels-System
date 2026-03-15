package main;

import java.util.Scanner;
import service.TourPackageService;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TourPackageService service = new TourPackageService();

        while (true) {

            System.out.println("1 Add Tour Package");
            System.out.println("2 View Tour Packages");
            System.out.println("3 Exit");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:

                    System.out.println("Enter Package Id");
                    int id = sc.nextInt();

                    System.out.println("Enter Destination");
                    String destination = sc.next();

                    System.out.println("Enter Price");
                    int price = sc.nextInt();

                    System.out.println("Enter Duration");
                    int duration = sc.nextInt();

                    service.createPackage(id, destination, price, duration);
                    break;

                case 2:
                    service.displayPackages();
                    break;

                case 3:
                    System.exit(0);
            }
        }
    }
}