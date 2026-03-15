package main;

import java.util.Scanner;
import controller.LoginController;
import model.User;
import service.UserService;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        LoginController loginController = new LoginController();

        while (true) {

            System.out.println("\n===== TOUR SYSTEM =====");
            System.out.println("1 Register");
            System.out.println("2 Login");
            System.out.println("3 Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {

                case 1: 
                    System.out.println("===== REGISTER =====");

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Password (At least 1 uppercase, 1 lowercase, 1 digit, 8+ chars): ");
                    String password = sc.nextLine();

                    System.out.print("Enter Phone (10 digits starting with 6-9): ");
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
                    System.out.println("Exiting the system...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice, try again!");
            }
        }
    }
}