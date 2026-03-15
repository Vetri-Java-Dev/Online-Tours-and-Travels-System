package service;

import java.util.Scanner;

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
                    // add package
                    break;

                case 2:
                    service.displayPackages();
                    break;

                case 3:
                    return;
            }
        }
    }

}