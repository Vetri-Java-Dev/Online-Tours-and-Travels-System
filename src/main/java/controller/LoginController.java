/*
 * Author         : Vetrivel B 
 * Description    : Central controller for Managing login in application
 * Module         : Login Module
 * Java version   : 24
 */

package controller;

import exception.*;
import model.Admin;
import model.Customer;
import model.User;
import util.InputUtil;
import service.UserService;
import util.ColorText;
import util.ConsoleUtil;
import util.EmailUtil;

public class LoginController {

    UserService userService = new UserService();

    //Login menu
    public void login() {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("             LOGIN PORTAL             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
        System.out.println(ColorText.warning("║") + "  1.  Login                           " + ColorText.warning("║"));
        System.out.println(ColorText.warning("║") + "  2.  Forgot Password                 " + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        int choice = InputUtil.getInt(ColorText.bold("  Enter choice: "));

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

        String email = InputUtil.getString("  Email    : ");

        String password = ConsoleUtil.readPassword("  Password : ");

        try {
            User user = userService.login(email, password);

            if(user != null) {
                System.out.println(ColorText.success("\n  Login Successful! Welcome, " + user.getName()));

                if(user instanceof Admin) {
                    System.out.println(ColorText.yellow("  Redirecting to Admin Dashboard..."));
                    new AdminController().adminMenu();
                }
                else if(user instanceof Customer) {
                    System.out.println(ColorText.yellow("  Redirecting to Customer Dashboard..."));
                    new CustomerController(user.getUserId()).customerMenu();
                }
            }
            else {
                System.out.println(ColorText.error("\n  Invalid email or password. Please try again."));
            }
        }
        catch (InvalidCredentialsException e) {
            System.out.println(ColorText.error("\n  " + e.getMessage()));
        }
    }

    //Forgot password
    public void forgotPassword() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           FORGOT PASSWORD           ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        String email = InputUtil.getString("  Registered Email: ");

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

            String enteredOtp = InputUtil.getString(ColorText.bold("\n  Enter OTP: "));

            if(System.currentTimeMillis() - otpGeneratedTime > 5 * 60 * 1000) {
                System.out.println(ColorText.error("\n  OTP expired. Please try again."));
                return;
            }

            if(enteredOtp.equals(otp)) {
            	System.out.print("\n");

            	String newPassword = ConsoleUtil.readPassword("  Enter new password : ");
            	String confirmPassword = ConsoleUtil.readPassword("  Confirm password   : ");
            	
                if(!newPassword.equals(confirmPassword)) {
                    System.out.println(ColorText.error("\n  Passwords do not match. Please try again."));
                    return;
                }

                userService.updatePassword(email, newPassword);
                System.out.println(ColorText.success("\n  Password reset successful! Please login again."));
            }
            else {
                System.out.println(ColorText.error("\n  Invalid OTP. Please try again."));
            }
        }
        catch (UserNotFoundException e) {
            System.out.println(ColorText.error("\n  " + e.getMessage()));
        }
    }
}