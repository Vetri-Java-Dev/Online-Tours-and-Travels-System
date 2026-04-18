package main;

import controller.LoginController;
import util.InputUtil;
import model.Admin;
import model.Customer;
import model.User;
import service.UserService;
import util.ColorText;
import util.ConsoleUtil;

public class MainApp {

    public static void main(String[] args) {

        UserService userService = new UserService();
        LoginController loginController = new LoginController();
      
        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("     ONLINE TOUR & TRAVEL SYSTEM      ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1.  Register                        " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2.  Login                           " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3.  Exit                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            
            int choice = InputUtil.getInt(ColorText.bold("  Enter choice: "));

            switch(choice) {

                case 1:
                    System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
                    System.out.println(ColorText.warning("│") + ColorText.bold("           NEW REGISTRATION          ") + ColorText.warning("│"));
                    System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

                    String name = InputUtil.getString("  Name     : ");
                    String email = InputUtil.getString("  Email    : ");
                    String password = ConsoleUtil.readPassword("  Password : ");
                    String phone = InputUtil.getString("  Phone    : ");

                    System.out.println(ColorText.warning("\n  Select Role:"));
                    System.out.println("  1. Customer");
                    System.out.println("  2. Admin");
                    int roleChoice = InputUtil.getInt(ColorText.bold("  Choice   : "));

                    String role = (roleChoice == 2) ? "ADMIN" : "CUSTOMER";

                    User user;
                    if (role.equals("ADMIN")) {
                        user = new Admin(0, name, email, password, phone, role);
                    }
                    else {
                        user = new Customer(0, name, email, password, phone, role, null, null, null);
                    }

                    userService.registerUser(user);
                    break;

                case 2:
                    loginController.login();
                    break;

                case 3:
                	
                    System.out.println(ColorText.success("\n  Thank you for using Tour & Travel System!"));
                    System.out.println(ColorText.success("  Goodbye!\n"));
                    InputUtil.close();
                    
                    System.exit(0);

                default:
                    System.out.println(ColorText.error("\n  Invalid choice. Please enter 1, 2 or 3."));
            }
          }
        }
}