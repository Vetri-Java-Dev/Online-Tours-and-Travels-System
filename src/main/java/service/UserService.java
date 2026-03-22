package service;

import dao.UserDAO;
import model.User;
import util.EmailUtil;
import util.InputValidationUtil;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public void registerUser(User user) {

        System.out.println("\n========================================");
        System.out.println("           USER REGISTRATION            ");
        System.out.println("========================================");

        if (!InputValidationUtil.isValidEmail(user.getEmail())) {
            System.out.println("Invalid email format.");
            return;
        }

        if (!InputValidationUtil.isValidPassword(user.getPassword())) {
            System.out.println("Password must be strong (min 8 chars, uppercase, lowercase, digit, special char).");
            return;
        }

        userDAO.registerUser(user);

        
    }

    public User login(String email, String password) {

        System.out.println("\n========================================");
        System.out.println("              USER LOGIN                ");
        System.out.println("========================================");

        if(!InputValidationUtil.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return null;
        }

        if(!InputValidationUtil.isValidPassword(password)) {
            System.out.println("Invalid password format.");
            return null;
        }

        User user = userDAO.login(email, password);

        if(user == null) {
            System.out.println("Invalid email or password.");
        }

        return user;
    }

    public User getUserById(int userId) {

        if(userId <= 0) {
            System.out.println("Invalid User ID.");
            return null;
        }

        return userDAO.getUserById(userId);
    }
    
    public User getUserByEmail(String email) {

        if(email == null || email.trim().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return null;
        }

        return userDAO.getUserByEmail(email);
    }

    public void updatePassword(String email, String newPassword) {

        System.out.println("\n========================================");
        System.out.println("           UPDATE PASSWORD              ");
        System.out.println("========================================");

        if(newPassword == null || newPassword.length() < 4) {
            System.out.println("Weak password. Minimum 4 characters required.");
            return;
        }

        userDAO.updatePassword(email, newPassword);
    }
}