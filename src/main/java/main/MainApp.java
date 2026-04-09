package main;

import java.util.Scanner;
import controller.LoginController;
import model.User;
import service.UserService;
import util.ColorText;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        LoginController loginController = new LoginController();
        
        if (args.length > 0) {
            runAutomationMode(args);
            return;
        }

        while(true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("     ONLINE TOUR & TRAVEL SYSTEM      ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1.  Register                        " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2.  Login                           " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3.  Exit                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:
                    System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
                    System.out.println(ColorText.warning("│") + ColorText.bold("           NEW REGISTRATION          ") + ColorText.warning("│"));
                    System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

                    System.out.print("  Name     : ");
                    String name = sc.nextLine();

                    System.out.print("  Email    : ");
                    String email = sc.nextLine();

                    System.out.print("  Password : ");
                    String password = sc.nextLine();

                    System.out.print("  Phone    : ");
                    String phone = sc.nextLine();

                    System.out.println(ColorText.warning("\n  Select Role:"));
                    System.out.println("  1. Customer");
                    System.out.println("  2. Admin");
                    System.out.print(ColorText.bold("  Choice   : "));

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
                    System.out.println(ColorText.success("\n  Thank you for using Tour & Travel System!"));
                    System.out.println(ColorText.success("  Goodbye!\n"));
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println(ColorText.error("\n  Invalid choice. Please enter 1, 2 or 3."));
            }
          }
        }
        

	private static void runAutomationMode(String[] args) {
		UserService userService = new UserService();
        LoginController loginController = new LoginController();

        try {
            String operation = args[0];

            switch (operation.toLowerCase()) {

                case "register":
                    User user = new User();
                    user.setName(args[1]);
                    user.setEmail(args[2]);
                    user.setPassword(args[3]);
                    user.setPhone(args[4]);
                    user.setRole(args[5]);

                    userService.registerUser(user);
                    System.out.println("✅ Registered via Jenkins");
                    break;

                case "login":
                    // small overload method needed
                    //loginController.login(args[1], args[2]);
                    break;

                default:
                    System.out.println("❌ Unknown command");
            }

        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
		
	}
}