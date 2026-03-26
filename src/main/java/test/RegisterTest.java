package test;

import model.User;
import service.UserService;

import java.util.Scanner;

public class RegisterTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();

        System.out.println("===== USER REGISTRATION =====");

        System.out.print("Enter Name : ");
        String name = sc.nextLine();

        System.out.print("Enter Email : ");
        String email = sc.nextLine();

        System.out.print("Enter Password : ");
        String password = sc.nextLine();

        System.out.print("Enter Phone : ");
        String phone = sc.nextLine().trim();

        System.out.print("Enter Role (ADMIN/CUSTOMER) : ");
        String role = sc.nextLine().toUpperCase().trim();

        User user = new User(0, name, email, password, phone, role);

        userService.registerUser(user);

        sc.close();
    }
}
