package controller;

import java.util.List;
import java.util.Scanner;

import exception.*;
import model.Booking;
import service.BookingService;
import service.FeedbackService;
import service.TourPackageService;
import util.ColorText;

public class FeedbackController {

    private Scanner sc = new Scanner(System.in);
    private FeedbackService feedbackService = new FeedbackService();
    private BookingService bookingService = new BookingService();
    private TourPackageService tourService = new TourPackageService();

    // ================= ADMIN: FEEDBACK MENU =================
    public void adminFeedbackMenu() {
        while (true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("      MANAGE FEEDBACK & RATINGS       ")
                    + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(
                    ColorText.warning("║") + "  1. View Pending Feedback            " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  2. Approve Feedback                 " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  3. Reject Feedback                  " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  4. View All Feedback                " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  5. View Feedback by Package         " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  6. Delete Feedback                  " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  7. Back                             " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

            System.out.print(ColorText.bold("  Enter choice: "));
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1:
                    feedbackService.viewPendingFeedback();
                    break;

                case 2:
                    System.out.print(ColorText.bold("  Feedback ID : "));
                    try {
                        feedbackService.approveFeedback(Integer.parseInt(sc.nextLine()));
                    } catch (BookingNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 3:
                    System.out.print(ColorText.bold("  Feedback ID : "));
                    try {
                        feedbackService.rejectFeedback(Integer.parseInt(sc.nextLine()));
                    } catch (BookingNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 4:
                    feedbackService.viewAllFeedback();
                    break;

                case 5:
                    tourService.displayPackages();
                    System.out.print(ColorText.bold("\n  Enter Package ID to view feedback: "));
                    try {
                        feedbackService.viewPackageReviews(Integer.parseInt(sc.nextLine()));
                    } catch (PackageNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;
                case 6:
                    feedbackService.viewAllFeedback();
                    System.out.print(ColorText.bold("\n  Enter Feedback ID to delete: "));

                    try {
                        feedbackService.deleteFeedback(Integer.parseInt(sc.nextLine()));
                    } catch (BookingNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;

                case 7:
                    return;

                default:
                    System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    // ================= CUSTOMER: FEEDBACK MENU =================
    public void customerFeedbackMenu(int customerId) {
        while (true) {

            System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("║") + ColorText.bold("         FEEDBACK & RATINGS           ")
                    + ColorText.warning("║"));
            System.out.println(ColorText.warning("╠══════════════════════════════════════╣"));
            System.out.println(
                    ColorText.warning("║") + "  1.  Submit Feedback                 " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  2.  View My Feedback                " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  3.  View Package Reviews            " + ColorText.warning("║"));
            System.out.println(
                    ColorText.warning("║") + "  4.  Back                            " + ColorText.warning("║"));
            System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
            System.out.print(ColorText.bold("  Enter choice: "));

            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1:
                    submitFeedback(customerId);
                    break;
                case 2:
                    System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
                    System.out.println(ColorText.warning("║") + ColorText.bold("           MY FEEDBACK HISTORY        ")
                            + ColorText.warning("║"));
                    System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
                    try {
                        feedbackService.viewMyFeedback(customerId);
                    } catch (UserNotFoundException e) {
                        System.out.println(ColorText.error("  " + e.getMessage()));
                    }
                    break;
                case 3:
                    viewPackageReviews();
                    break;
                case 4:
                    return;
                default:
                    System.out.println(ColorText.error("  Invalid choice."));
            }
        }
    }

    private void submitFeedback(int customerId) {
        // Get only this logged-in user's bookings
        List<Booking> myBookings = bookingService.getBookingsByCustomerId(customerId);

        if (myBookings.isEmpty()) {
            System.out.println(ColorText.warning("\n  You have no booked packages yet."));
            return;
        }

        // Show only logged-in user's booked packages
        System.out.println(ColorText.warning("\n  ╔═════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║")
                + ColorText.bold("         BOOKINGS ELIGIBLE FOR FEEDBACK          ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╠══════════════╦══════════════════╦══════════════╣"));
        System.out.println(ColorText.warning("  ║") + ColorText.cyan("  Booking ID  ") + ColorText.warning("║")
                + ColorText.cyan("  Date            ") + ColorText.warning("║") + ColorText.cyan("  Package ID  ")
                + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╠══════════════╬══════════════════╬══════════════╣"));
        for (Booking b : myBookings) {
            System.out.printf(
                    ColorText.warning("  ║") + "  %-12d" + ColorText.warning("║") + "  %-16s" + ColorText.warning("║")
                            + "  %-12d" + ColorText.warning("║") + "%n",
                    b.getBookingId(), b.getBookingDate(), b.getPackageId());
        }
        System.out.println(ColorText.warning("  ╚══════════════╩══════════════════╩══════════════╝"));

        // Ask user to select a booking
        System.out.print(ColorText.bold("\n  Enter Booking ID: "));
        int bookingId;
        try {
            bookingId = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(ColorText.error("  Invalid input."));
            return;
        }

        // Validate selected booking belongs to this logged-in user
        Booking selected = null;
        for (Booking b : myBookings) {
            if (b.getBookingId() == bookingId) {
                selected = b;
                break;
            }
        }
        if (selected == null) {
            System.out.println(ColorText.error("  Invalid Booking ID."));
            return;
        }

        // Check if feedback already submitted
        if (feedbackService.hasFeedback(bookingId)) {
            System.out.println(ColorText.error("  You have already submitted feedback for this booking."));
            return;
        }

        // ── Rating ──
        int rating = 0;
        while (rating < 1 || rating > 5) {
            System.out.println(ColorText.warning("\n  Rate your experience:"));
            System.out
                    .println(ColorText.yellow("1 ★ Poor\n2 ★★ Fair\n3 ★★★ Good\n4 ★★★★ Very Good\n5 ★★★★★ Excellent"));
            System.out.print(ColorText.bold("  Your rating (1-5): "));
            try {
                rating = Integer.parseInt(sc.nextLine());
                if (rating < 1 || rating > 5)
                    System.out.println(ColorText.error("  Please enter a number between 1 and 5."));
            } catch (NumberFormatException e) {
                System.out.println(ColorText.error("  Invalid input."));
            }
        }

        // ── Title ──
        String title = "";
        while (title.isEmpty()) {
            System.out.print(ColorText.bold("\n  Review title (max 150 chars): "));
            title = sc.nextLine().trim();

            if (title.isEmpty())
                System.out.println(ColorText.error("  Title cannot be empty."));
            else if (title.length() > 150) {
                System.out.println(ColorText.error("  Title too long. Max 150 characters."));
                title = "";
            }
        }

        // ── Description ──
        String description = "";
        while (description.length() < 10) {
            System.out.print(ColorText.bold("\n  Write your review (min 10 chars): "));
            description = sc.nextLine().trim();

            if (description.length() < 10)
                System.out.println(ColorText.error("  Review must be at least 10 characters."));
        }

        try {
            feedbackService.submitFeedback(
                    bookingId, customerId, selected.getPackageId(),
                    rating, title, description);
        } catch (BookingNotFoundException | InvalidBookingException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }

    private void viewPackageReviews() {
        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("       VIEW PACKAGE REVIEWS           ")
                + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));
        System.out.print(ColorText.bold("  Enter Package ID : "));

        try {
            int packageId = Integer.parseInt(sc.nextLine());
            feedbackService.viewPackageReviews(packageId);
        } catch (NumberFormatException e) {
            System.out.println(ColorText.error("  Invalid Package ID."));
        } catch (PackageNotFoundException e) {
            System.out.println(ColorText.error("  " + e.getMessage()));
        }
    }
}
