package test;

import model.User;
import service.UserService;

import util.InputUtil;

public class LoginTest {

    public static void main(String[] args) {

        UserService userService = new UserService();

        System.out.println("===== LOGIN TEST =====");

        String email = InputUtil.getString("Enter Email: ");
        String password = InputUtil.getString("Enter Password: ");

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

        InputUtil.close();
    }
}