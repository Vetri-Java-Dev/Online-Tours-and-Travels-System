package controller;

import java.util.Scanner;
import model.User;
import service.UserService;
import util.EmailUtil;

public class LoginController {

    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

    public void login() {

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║             LOGIN PORTAL             ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1.  Login                           ║");
        System.out.println("║  2.  Forgot Password                 ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("  Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch(choice) {
            case 1:
                normalLogin();
                break;
                
            case 2:
                forgotPassword();
                break;
                
            default:
                System.out.println("\n  Invalid choice. Please try again.");
        }
    }

    public void normalLogin() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│              USER LOGIN             │");
        System.out.println("└─────────────────────────────────────┘");
        
        System.out.print("  Email    : ");
        String email = sc.next();

        System.out.print("  Password : ");
        String password = sc.next();

        User user = userService.login(email, password);

        if(user!=null) {

            System.out.println("\n  Login Successful! Welcome, " + user.getName());

            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                System.out.println("  Redirecting to Admin Dashboard...");
                AdminController admin = new AdminController();
                admin.adminMenu();
            }
            else if (user.getRole().equalsIgnoreCase("CUSTOMER")) {
                System.out.println("  Redirecting to Customer Dashboard...");
                CustomerController customer = new CustomerController(user.getUserId());
                customer.customerMenu();
            }

        }
        else {
            System.out.println("\n  Invalid email or password. Please try again.");
        }
    }

    public void forgotPassword() {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           FORGOT PASSWORD           │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("  Registered Email: ");
        String email = sc.next();

        User user = userService.getUserByEmail(email);

        if (user == null) {
            System.out.println("\n  Email not registered. Please check and try again.");
            return;
        }

        String otp = String.valueOf((int)(Math.random()*900000)+100000);
        long otpGeneratedTime = System.currentTimeMillis();

        System.out.println("\n  Sending OTP to your email...");
        EmailUtil.sendOTPEmail(email, user.getName(), otp);
        
        System.out.println("  OTP sent successfully!");
        System.out.println("  Note: OTP is valid for 5 minutes only.");

        System.out.print("\n  Enter OTP: ");
        String enteredOtp = sc.next();

        long currentTime = System.currentTimeMillis();

        if (currentTime-otpGeneratedTime>5*60*1000) {
            System.out.println("\n  OTP expired. Please try again.");
            return;
        }

        if (enteredOtp.equals(otp)) {

            System.out.print("\n  Enter new password : ");
            String newPassword = sc.next();

            System.out.print("  Confirm password   : ");
            String confirmPassword = sc.next();

            if(!newPassword.equals(confirmPassword)) {
                System.out.println("\n  Passwords do not match. Please try again.");
                return;
            }

            userService.updatePassword(email, newPassword);
            System.out.println("\n  Password reset successful! Please login again.");

        }
        else {
            System.out.println("\n  Invalid OTP. Please try again.");
        }
    }
}