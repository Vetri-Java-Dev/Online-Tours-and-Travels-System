package test;

import model.User;
import service.UserService;

import util.InputUtil;

public class RegisterTest {

    public static void main(String[] args) {

        UserService userService = new UserService();

        System.out.println("===== USER REGISTRATION =====");

        String name = InputUtil.getString("Enter Name : ");
        String email = InputUtil.getString("Enter Email : ");
        String password = InputUtil.getString("Enter Password : ");
        String phone = InputUtil.getString("Enter Phone : ").trim();
        String role = InputUtil.getString("Enter Role (ADMIN/CUSTOMER) : ").toUpperCase().trim();

        User user = new User(0, name, email, password, phone, role);

        userService.registerUser(user);

        InputUtil.close();
    }
}
