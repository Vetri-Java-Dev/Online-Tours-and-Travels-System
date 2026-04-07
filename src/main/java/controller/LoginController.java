package controller;

import java.util.Scanner;
import exception.*;
import model.User;
import service.UserService;
import util.ColorText;
import util.EmailUtil;

public class LoginController {

    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

    public void login() {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("             LOGIN PORTAL             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
        System.out.println(ColorText.warning("║") + "  1.  Login                           " + ColorText.warning("║"));
        System.out.println(ColorText.warning("║") + "  2.  Forgot Password                 " + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  Enter choice: "));

        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {
            case 1:  normalLogin();    break;
            case 2:  forgotPassword(); break;
            default: System.out.println(ColorText.error("\n  Invalid choice. Please try again."));
        }
    }

    public void normalLogin() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("              USER LOGIN             ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print("  Email    : ");
        String email = sc.next();

        System.out.print("  Password : ");
        String password = sc.next();

        try {
            User user = userService.login(email, password);

            if(user != null) {
                System.out.println(ColorText.success("\n  Login Successful! Welcome, " + user.getName()));

                if(user.getRole().equalsIgnoreCase("ADMIN")) {
                    System.out.println(ColorText.yellow("  Redirecting to Admin Dashboard..."));
                    new AdminController().adminMenu();
                } else if(user.getRole().equalsIgnoreCase("CUSTOMER")) {
                    System.out.println(ColorText.yellow("  Redirecting to Customer Dashboard..."));
                    new CustomerController(user.getUserId()).customerMenu();
                }
            } else {
                System.out.println(ColorText.error("\n  Invalid email or password. Please try again."));
            }
        } catch (InvalidCredentialsException e) {
            System.out.println(ColorText.error("\n  " + e.getMessage()));
        }
    }

    public void forgotPassword() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           FORGOT PASSWORD           ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print("  Registered Email: ");
        String email = sc.next();

        try {
            User user = userService.getUserByEmail(email);

            if(user == null) {
                System.out.println(ColorText.error("\n  Email not registered. Please check and try again."));
                return;
            }

            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
            long otpGeneratedTime = System.currentTimeMillis();

            System.out.println(ColorText.warning("\n  Sending OTP to your email..."));
            EmailUtil.sendOTPEmail(email, user.getName(), otp);
            System.out.println(ColorText.success("  OTP sent successfully!"));
            System.out.println(ColorText.warning("  Note: OTP is valid for 5 minutes only."));

            System.out.print(ColorText.bold("\n  Enter OTP: "));
            String enteredOtp = sc.next();

            if(System.currentTimeMillis() - otpGeneratedTime > 5 * 60 * 1000) {
                System.out.println(ColorText.error("\n  OTP expired. Please try again."));
                return;
            }

            if(enteredOtp.equals(otp)) {
                System.out.print("\n  Enter new password : ");
                String newPassword = sc.next();

                System.out.print("  Confirm password   : ");
                String confirmPassword = sc.next();

                if(!newPassword.equals(confirmPassword)) {
                    System.out.println(ColorText.error("\n  Passwords do not match. Please try again."));
                    return;
                }

                userService.updatePassword(email, newPassword);
                System.out.println(ColorText.success("\n  Password reset successful! Please login again."));
            } else {
                System.out.println(ColorText.error("\n  Invalid OTP. Please try again."));
            }
        } catch (UserNotFoundException e) {
            System.out.println(ColorText.error("\n  " + e.getMessage()));
        }
    }
}