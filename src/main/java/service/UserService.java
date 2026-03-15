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
            System.out.println("Password must be at least 4 characters");
            return;
        }
        
        System.out.println("Registration Completed!");
        userDAO.registerUser(user);
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

}