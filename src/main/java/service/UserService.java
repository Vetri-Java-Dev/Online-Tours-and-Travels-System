package service;

import java.util.Scanner;

import dao.UserDAO;
import model.User;
import service.TourPackageService;
import util.InputValidationUtil;
import util.PasswordUtil;

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

        // hash password before storing
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

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

        String hashedPassword = PasswordUtil.hashPassword(password);

        return userDAO.login(email, hashedPassword);
    }


    // CUSTOMER MENU
    public void customerMenu(){

        Scanner sc = new Scanner(System.in);
        TourPackageService service = new TourPackageService();

        while(true){

            System.out.println("CUSTOMER MENU");
            System.out.println("1 View Packages");
            System.out.println("2 Exit");

            int choice = sc.nextInt();

            switch(choice){

                case 1:
                    service.displayPackages();
                    break;

                case 2:
                    return;
            }
        }
    }


    // ADMIN MENU
    public void adminMenu() {

        Scanner sc = new Scanner(System.in);
        TourPackageService service = new TourPackageService();

        while(true){

            System.out.println("ADMIN MENU");
            System.out.println("1 Add Package");
            System.out.println("2 View Packages");
            System.out.println("3 Exit");

            int choice = sc.nextInt();

            switch(choice){

                case 1:
                    // add package logic
                    break;

                case 2:
                    service.displayPackages();
                    break;

                case 3:
                    return;
            }
        }
    }


    // GET USER BY ID (PROFILE FEATURE)
    public User getUserById(int userId) {

        if(userId <= 0) {
            System.out.println("Invalid User ID");
            return null;
        }

        return userDAO.getUserById(userId);
    }


    // NEW METHOD (FOR FORGOT PASSWORD)
    public User getUserByEmail(String email) {

        if(!InputValidationUtil.isValidEmail(email)) {
            System.out.println("Invalid Email");
            return null;
        }

        return userDAO.getUserByEmail(email);
    }


    // NEW METHOD (RESET PASSWORD)
    public void updatePassword(String email, String newPassword) {

        if(!InputValidationUtil.isValidPassword(newPassword)) {
            System.out.println("Password must be at least 4 characters");
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(newPassword);

        userDAO.updatePassword(email, hashedPassword);

        System.out.println("Password Updated Successfully!");
    }
}