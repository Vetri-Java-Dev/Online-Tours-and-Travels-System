package service;

import dao.UserDAO;
import model.User;
import util.EmailUtil;
import util.InputValidationUtil;

import java.util.List;
import java.util.Scanner;

public class UserService {

    private UserDAO userDAO = new UserDAO();
    

    private static final String ADMIN_EMAIL = "onlinetats@gmail.com";

    public void registerUser(User user) {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           USER REGISTRATION         │");
        System.out.println("└─────────────────────────────────────┘");

        if (!InputValidationUtil.isValidEmail(user.getEmail())) {
            System.out.println("\n  Invalid email format.");
            return;
        }

        if (!InputValidationUtil.isValidPassword(user.getPassword())) {
            System.out.println("\n  Password must be strong.");
            System.out.println("  Requires: min 8 chars, uppercase, lowercase,");
            System.out.println("            digit and special character.");
            return;
        }

        if (user.getRole().equalsIgnoreCase("ADMIN")) {

            boolean verified = verifyAdminOTP();

            if (!verified) {
                System.out.println("\n  Admin verification failed. Registration cancelled.");
                return;
            }

            System.out.println("\n  Admin verified successfully!");
        }

        userDAO.registerUser(user);
    }

    private boolean verifyAdminOTP() {

        Scanner sc = new Scanner(System.in);

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        long generatedTime = System.currentTimeMillis();

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│          ADMIN VERIFICATION         │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.println("  Sending OTP to admin email...");

        EmailUtil.sendOTPEmail(ADMIN_EMAIL, "Admin", otp);

        System.out.println("  OTP sent to : " + ADMIN_EMAIL);
        System.out.println("  Note        : OTP is valid for 5 minutes only.");
        System.out.print("\n  Enter OTP   : ");

        String enteredOtp = sc.nextLine().trim();

        if (System.currentTimeMillis() - generatedTime > 5 * 60 * 1000) {
            System.out.println("\n  OTP expired. Please try again.");
            return false;
        }

        if (enteredOtp.equals(otp)) {
            return true;
        }
        else {
            System.out.println("\n  Invalid OTP. Registration denied.");
            return false;
        }
    }

    public User login(String email, String password) {

        if (!InputValidationUtil.isValidEmail(email)) {
            System.out.println("\n  Invalid email format.");
            return null;
        }

        if (!InputValidationUtil.isValidPassword(password)) {
            System.out.println("\n  Invalid password format.");
            return null;
        }

        User user = userDAO.login(email, password);

        if (user == null) {
            System.out.println("\n  Invalid email or password.");
        }

        return user;
    }

    public User getUserById(int userId) {

        if (userId <= 0) {
            System.out.println("\n  Invalid User ID.");
            return null;
        }

        return userDAO.getUserById(userId);
    }

    public User getUserByEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            System.out.println("\n  Email cannot be empty.");
            return null;
        }

        return userDAO.getUserByEmail(email);
    }

    public void updatePassword(String email, String newPassword) {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           UPDATE PASSWORD           │");
        System.out.println("└─────────────────────────────────────┘");

        if (newPassword == null || newPassword.length() < 4) {
            System.out.println("\n  Weak password. Minimum 4 characters required.");
            return;
        }

        userDAO.updatePassword(email, newPassword);
    }

    public boolean updateUser(int userId, String name, String phone) {
        return userDAO.updateUser(userId, name, phone);
    }

    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();

    }
}