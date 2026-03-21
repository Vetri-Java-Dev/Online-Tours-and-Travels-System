package main;

import java.util.Scanner;

import controller.LoginController;
import model.User;
import service.UserService;
import service.TourPackageService;
import model.TourPackage;
import main.PriceComparator;
import main.DurationComparator;
import java.util.List;
import java.util.Collections;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        LoginController loginController = new LoginController();
        TourPackageService tourService = new TourPackageService();

        while(true) {

            System.out.println("\n===== TOUR SYSTEM =====");
            System.out.println("1 Register");
            System.out.println("2 Login");
            System.out.println("3 Search Packages"); 
            System.out.println("4 Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:

                    System.out.println("===== REGISTER =====");

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    System.out.print("Enter Phone: ");
                    String phone = sc.nextLine();

                    System.out.print("Enter Role (ADMIN / CUSTOMER): ");
                    String role = sc.nextLine().toUpperCase();

                    User user = new User();

                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setPhone(phone);
                    user.setRole(role);

                    userService.registerUser(user);
                    break;

                case 2:
                    loginController.login();
                    break;

                case 3:

                    System.out.println("===== SEARCH PACKAGE =====");
                    System.out.print("Enter destination: ");
                    String dest = sc.nextLine();

                    List<TourPackage> list = tourService.searchByDestination(dest);

                    if(list.isEmpty()) {
                        System.out.println("No packages found!");
                        break;
                    }

                    // Ask sorting option
                    System.out.println("1 Sort by Price");
                    System.out.println("2 Sort by Duration");
                    int sortChoice = sc.nextInt();
                    sc.nextLine();

                    if(sortChoice == 1) {
                        Collections.sort(list, new PriceComparator());
                    } else {
                        Collections.sort(list, new DurationComparator());
                    }

                    // Display result
                    for(TourPackage tp : list) {
                        System.out.println(
                            tp.getPackageId() + " " +
                            tp.getDestination() + " " +
                            tp.getPrice() + " " +
                            tp.getDuration()
                        );
                    }

                    break;
                    
                case 4:
                    System.out.println("Exiting the system...");
                    sc.close();
                    System.exit(0);
                    
                default:
                    System.out.println("Invalid choice, try again!");
            }
        }
    }
}