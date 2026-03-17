package controller;

import java.util.Scanner;
import model.User;
import service.UserService;
import util.EmailUtil;

public class LoginController {

    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

    public void login() {

        System.out.println("===== LOGIN =====");
        System.out.println("1 Login");
        System.out.println("2 Forgot Password");
        System.out.print("Enter choice: ");

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
                System.out.println("Invalid choice");
        }
    }

    public void normalLogin() {

        System.out.print("Enter Email: ");
        String email = sc.next();

        System.out.print("Enter Password: ");
        String password = sc.next();

        User user = userService.login(email, password);

        if(user != null) {

            System.out.println("Login Successful");

            if(user.getRole().equalsIgnoreCase("ADMIN")) {

                AdminController admin = new AdminController();
                admin.adminMenu();

            } 
            else if(user.getRole().equalsIgnoreCase("CUSTOMER")) {

                CustomerController customer = new CustomerController(user.getUserId());
                customer.customerMenu();
            }

        } 
        else {
            System.out.println("Invalid Email or Password");
        }
    }

    public void forgotPassword() {

        System.out.println("===== FORGOT PASSWORD =====");

        System.out.print("Enter your registered email: ");
        String email = sc.next();

        User user = userService.getUserByEmail(email);

        if(user == null) {
            System.out.println("Email not registered! Reset password failed.");
            return;
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        long otpGeneratedTime = System.currentTimeMillis();
        
        EmailUtil.sendOTP(email, otp);

        System.out.print("Enter OTP sent to email: ");
        String enteredOtp = sc.next();

        long currentTime = System.currentTimeMillis();

        if(currentTime-otpGeneratedTime>70000) {
            System.out.println("OTP expired !!");
            return;
        }
        
        if(enteredOtp.equals(otp)) {

            System.out.print("Enter new password: ");
            String newPassword = sc.next();

            userService.updatePassword(email, newPassword);

            System.out.println("Password reset successful!");

        }
        else {
            System.out.println("Invalid OTP");
        }
    }
}