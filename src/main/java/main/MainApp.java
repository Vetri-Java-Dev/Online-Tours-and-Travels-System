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

        while(true) {

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║     ONLINE TOUR & TRAVEL SYSTEM      ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1.  Register                        ║");
            System.out.println("║  2.  Login                           ║");
            System.out.println("║  3.  Exit                            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    System.out.println("\n┌─────────────────────────────────────┐");
                    System.out.println("│           NEW REGISTRATION          │");
                    System.out.println("└─────────────────────────────────────┘");

                    System.out.print("  Name     : ");
                    String name = sc.nextLine();

                    System.out.print("  Email    : ");
                    String email = sc.nextLine();

                    System.out.print("  Password : ");
                    String password = sc.nextLine();

                    System.out.print("  Phone    : ");
                    String phone = sc.nextLine();

                    System.out.println("\n  Select Role:");
                    System.out.println("  1. Customer");
                    System.out.println("  2. Admin");
                    System.out.print("  Choice   : ");
                    int roleChoice = sc.nextInt();
                    sc.nextLine();

                    String role = (roleChoice == 2) ? "ADMIN" : "CUSTOMER";

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
                    System.out.println("\n  Thank you for using Tour & Travel System!");
                    System.out.println("  Goodbye!\n");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("\n  Invalid choice. Please enter 1, 2 or 3.");
            }
        }
    }
}