package controller;

import java.util.Scanner;
import model.User;
import service.UserService;
import util.EmailUtil;

public class LoginController {

    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

    public void login() {

        System.out.println("\n========================================");
        System.out.println("              LOGIN PORTAL              ");
        System.out.println("========================================");
        System.out.println("1. Login");
        System.out.println("2. Forgot Password");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");

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
                System.out.println("\nInvalid choice. Please try again.");
        }
    }

    public void normalLogin() {

        System.out.println("\n------------- USER LOGIN --------------");

        System.out.print("Email    : ");
        String email = sc.next();

        System.out.print("Password : ");
        String password = sc.next();

        User user = userService.login(email, password);

        if(user!=null) {

            System.out.println("\nLogin Successful");

            if(user.getRole().equalsIgnoreCase("ADMIN")) {

                System.out.println("Redirecting to Admin Dashboard...");
                AdminController admin = new AdminController();
                admin.adminMenu();

            } 
            else if(user.getRole().equalsIgnoreCase("CUSTOMER")) {

                System.out.println("Redirecting to Customer Dashboard...");
                CustomerController customer = new CustomerController(user.getUserId());
                customer.customerMenu();
            }

        } 
        else {
            System.out.println("\nInvalid Email or Password");
        }
    }

    public void forgotPassword() {
    	
        System.out.println("\n========================================");
        System.out.println("            FORGOT PASSWORD            ");
        System.out.println("========================================");
        System.out.print("Enter your registered email: ");
        String email = sc.next();

        User user = userService.getUserByEmail(email);

        if(user == null) {
            System.out.println("\nEmail not registered! Reset failed.");
            return;
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        long otpGeneratedTime = System.currentTimeMillis();

        System.out.println("\nSending OTP to your email...");

        EmailUtil.sendOTPEmail(email, user.getName(), otp); // ← only change, was sendOTP()

        System.out.println("OTP sent successfully");
        System.out.println("Note: OTP valid for 5 minutes.");
        System.out.print("\nEnter OTP: ");
        String enteredOtp = sc.next();

        long currentTime = System.currentTimeMillis();

        if(currentTime - otpGeneratedTime > 5*60*1000) {
            System.out.println("\nOTP expired. Please try again.");
            return;
        }

        if(enteredOtp.equals(otp)) {
        	
            System.out.print("\nEnter new password: ");
            String newPassword = sc.next();
            
            userService.updatePassword(email, newPassword);
            System.out.println("\nPassword reset successful!");
        }
        else {
            System.out.println("\nInvalid OTP");
        }
    }
}