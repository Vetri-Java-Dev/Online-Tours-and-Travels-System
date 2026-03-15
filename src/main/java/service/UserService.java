package service;

import dao.UserDAO;
import model.User;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public void registerUser(User user) {

        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            System.out.println("Email cannot be empty");
            return;
        }

        if(user.getPassword() == null || user.getPassword().length() < 4) {
            System.out.println("Password must be at least 4 characters");
            return;
        }

        userDAO.registerUser(user);
    }


    public User login(String email, String password) {

        if(email == null || email.isEmpty()) {
            System.out.println("Email cannot be empty");
            return null;
        }

        if(password == null || password.isEmpty()) {
            System.out.println("Password cannot be empty");
            return null;
        }

        return userDAO.login(email, password);
    }

}
