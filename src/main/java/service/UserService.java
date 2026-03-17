package service;

import dao.UserDAO;
import model.User;
import util.InputValidationUtil;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public void registerUser(User user) {

        if(!InputValidationUtil.isValidEmail(user.getEmail())) {
            System.out.println("Invalid Email");
            return;
        }

        if(!InputValidationUtil.isValidPassword(user.getPassword())) {
            System.out.println("Password must be valid");
            return;
        }

        userDAO.registerUser(user);
        System.out.println("Registration Completed!");
    }

    public User login(String email, String password) {

        if(!InputValidationUtil.isValidEmail(email)) {
            System.out.println("Invalid Email");
            return null;
        }

        if(!InputValidationUtil.isValidPassword(password)) {
            System.out.println("Invalid Password");
            return null;
        }

        return userDAO.login(email, password);
    }

    public User getUserById(int userId) {

        if(userId <= 0) {
            System.out.println("Invalid User ID");
            return null;
        }

        return userDAO.getUserById(userId);
    }
    
    public User getUserByEmail(String email) {

        if(email == null || email.trim().isEmpty()) {
            return null;
        }

        return userDAO.getUserByEmail(email);
    }

    public void updatePassword(String email, String newPassword) {

        if(newPassword == null || newPassword.length() < 4) {
            System.out.println("Weak password!");
            return;
        }

        userDAO.updatePassword(email, newPassword);
    }
}