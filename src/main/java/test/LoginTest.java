package test;

import model.User;
import service.UserService;

import java.util.Scanner;

public class LoginTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();

        System.out.println("===== LOGIN TEST =====");

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        User user = userService.login(email, password);

        if (user != null) {

            System.out.println("Login Successful!");

            System.out.println("User Name: " + user.getName());
            System.out.println("Role: " + user.getRole());

            if(user.getRole().equalsIgnoreCase("ADMIN")){
                System.out.println("Admin access granted");
            }
            else{
                System.out.println("Customer access granted");
            }

        } 
        else {

            System.out.println("Invalid Email or Password");

        }

        sc.close();
    }
}