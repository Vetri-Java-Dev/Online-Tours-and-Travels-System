package controller;

import java.util.Scanner;

import model.User;
import service.UserService;

public class LoginController {

    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

    public void login() {

        System.out.println("===== LOGIN =====");

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
}