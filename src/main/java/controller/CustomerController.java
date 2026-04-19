/*
 * Author         : Vetrivel B 
 * Description    : Central controller for customer operations, including tour package exploration and manage their bookings.
 * Module         : Customer Module
 * Java version   : 24
 */

package controller;

import exception.UserNotFoundException;
import model.User;
import service.TourPackageService;
import util.InputUtil;
import service.UserService;
import util.ColorText;
import java.util.Scanner;

public class CustomerController {

    private UserService userService = new UserService();
    private TourPackageService tourPackageService = new TourPackageService();
    private Scanner sc = new Scanner(System.in); 


    private int customerId;

    public CustomerController(int customerId) {
        this.customerId = customerId;
    }

    public void customerMenu() {

        while (true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("         CUSTOMER DASHBOARD           ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(ColorText.warning("║") + "  1.  View Tour Packages              " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  2.  Search Package                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  3.  View Package Itinerary          " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  4.  Manage Booking                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  5.  View Payment History            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  6.  Manage Profile                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  7.  Message Admin                   " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  8.  Feedback & Ratings              " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + "  9.  Delete Account                  " + ColorText.warning("║"));
            System.out.println(ColorText.warning("║") + " 10.  Exit                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            int choice = InputUtil.getInt(ColorText.bold("  Enter choice: "));

            switch (choice) {
                case 1: viewAndBookFlow(customerId); break;
                case 2: new PackageController().customerSearchPackage(customerId); break;
                case 3: new ItineraryController().customerViewItinerary(); break;
                case 4: new BookingController().customerManageBookingMenu(customerId); break;
                case 5: new BookingController().customerViewPaymentHistory(customerId); break;
                case 6: manageProfileMenu(); break;
                case 7: new MessageController().customerMessageMenu(customerId); break;
                case 8: new FeedbackController().customerFeedbackMenu(customerId); break;
                case 9: deleteAccount(); return;
                case 10:
                    System.out.println(ColorText.success("  Logging out..."));
                    return;
                default: System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    private void manageProfileMenu() {

        while (true) {

            System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
            System.out.println(ColorText.warning("│")
                    + ColorText.bold("          MANAGE PROFILE             ")
                    + ColorText.warning("│"));
            System.out.println(ColorText.warning("├─────────────────────────────────────┤"));

            System.out.println(ColorText.warning("│") + "  1. View Profile                    " + ColorText.warning("│"));
            System.out.println(ColorText.warning("│") + "  2. Update Profile                  " + ColorText.warning("│"));
            System.out.println(ColorText.warning("│") + "  3. Back                            " + ColorText.warning("│"));

            System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

            int choice = InputUtil.getInt(ColorText.bold("  Enter choice: "));

            switch (choice) {

                case 1: viewProfile(); break;
                case 2: updateProfile(); break;
                case 3: return;

                default:
                    System.out.println(ColorText.error("  Invalid choice!"));
            }
        }
    }

    // ── Profile ───────────────────────────────────────────────────────────────
    public void viewProfile() {
        try {
            User u = userService.getUserById(customerId);
            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("            MY PROFILE                ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.printf(ColorText.warning("║") + "  " + ColorText.cyan("Name  :") + " %-30s" + ColorText.warning("║") + "%n", u.getName());
            System.out.printf(ColorText.warning("║") + "  " + ColorText.cyan("Email :") + " %-30s" + ColorText.warning("║") + "%n", u.getEmail());
            System.out.printf(ColorText.warning("║") + "  " + ColorText.cyan("Phone :") + " %-30s" + ColorText.warning("║") + "%n", u.getPhone());
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        }
        catch (UserNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }

    public void updateProfile() {
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           UPDATE PROFILE             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        
        String name = InputUtil.getString(ColorText.bold("  New Name  : "));
        String phone = InputUtil.getString(ColorText.bold("  New Phone : "));
        
        try {
            userService.updateUser(customerId, name, phone);
            System.out.println(ColorText.success("  Profile updated successfully!"));
        }
        catch (UserNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }

    public void deleteAccount() {
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           DELETE ACCOUNT             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        
        if (InputUtil.getString(ColorText.bold("  Confirm delete (yes/no): ")).equalsIgnoreCase("yes")) {
            try {
                userService.deleteUser(customerId);
                System.out.println(ColorText.success("  Account deleted successfully."));
            }
            catch (UserNotFoundException e) {
                System.out.println(ColorText.error("  " + e.getMessage()));
            }
        }
        else {
            System.out.println(ColorText.yellow("  Account deletion cancelled."));
        }
    }
    public void viewAndBookFlow(int customerId) {

        tourPackageService.displayPackages();

        String choice = InputUtil.getString(
                ColorText.bold("  Do you want to book any package? (yes/no): ")
        );

        if(choice.equalsIgnoreCase("yes")) {
            new BookingController().createBooking(customerId);
        } else {
            System.out.println(ColorText.yellow("  Redirecting to Dashboard..."));
        }
    }  
}
