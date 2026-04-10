/*
 * Author         : Subhashree R
 * Created Date   : 10-Apr-2026
 * Description    : Service layer responsible for handling customer package reviews, rating submissions, and administrator moderation workflows.
 * Module         : Feedback Module
 * Java version   : 24
 */
package service;

import dao.FeedbackDAO;
import model.Feedback;
import util.ColorText;

import java.util.List;

public class FeedbackService {

    private final FeedbackDAO dao = new FeedbackDAO();

    
    public boolean submitFeedback(int bookingId, int customerId, int packageId,
                                  int rating, String title, String description) {

        if (rating < 1 || rating > 5) {
            System.out.println(ColorText.error("  Rating must be between 1 and 5."));
            return false;
        }
        if (title == null || title.trim().isEmpty()) {
            System.out.println(ColorText.error("  Title cannot be empty."));
            return false;
        }
        if (title.trim().length() > 150) {
            System.out.println(ColorText.error("  Title must be 150 characters or less."));
            return false;
        }
        if (description == null || description.trim().length() < 10) {
            System.out.println(ColorText.error("  Description must be at least 10 characters."));
            return false;
        }

        Feedback feedback = new Feedback(
            bookingId, customerId, packageId,
            rating, title.trim(), description.trim()
        );

        boolean ok = dao.submitFeedback(feedback);

        if (ok) {
            System.out.println(ColorText.success("\n    Feedback submitted successfully!"));
            System.out.println(ColorText.yellow("     It will appear after admin approval."));
            System.out.println(ColorText.warning("  ─────────────────────────────────────"));
            System.out.println("  Feedback ID : " + feedback.getFeedbackId());
            System.out.println("  Rating      : " + ColorText.cyan(feedback.getStarDisplay()));
            System.out.println("  Title       : " + feedback.getTitle());
            System.out.println("  Status      : " + ColorText.yellow("PENDING"));
            System.out.println(ColorText.warning("  ─────────────────────────────────────"));
        }
        return ok;
    }

    
    public void viewMyFeedback(int customerId) {
        List<Feedback> list = dao.getFeedbackByCustomer(customerId);

        System.out.println("\n  " + ColorText.bold("My Feedback & Ratings"));
        System.out.println(ColorText.warning("  ─────────────────────────────────────"));

        if (list.isEmpty()) {
            System.out.println(ColorText.yellow("  No feedback submitted yet."));
            return;
        }

        for (Feedback f : list) {
            System.out.println("\n  " + ColorText.bold("#" + f.getFeedbackId() + " — " + f.getDestination()));
            System.out.println("  Rating  : " + ColorText.cyan(f.getStarDisplay()));
            System.out.println("  Title   : " + f.getTitle());
            System.out.println("  Status  : " + colorStatus(f.getStatus()));
            System.out.println("  Date    : " + f.getFeedbackDate());
            System.out.println("  Review  : " + f.getDescription());
            System.out.println(ColorText.warning("  ─────────────────────────────────────"));
        }
    }

    // ── View approved reviews for a package (customer)
    public void viewPackageReviews(int packageId) {
        List<Feedback> list = dao.getApprovedFeedbackByPackage(packageId);
        double avg   = dao.getAverageRating(packageId);
        int    count = dao.getReviewCount(packageId);

        System.out.println("\n  " + ColorText.bold("Package #" + packageId + " Reviews"));
        System.out.println("  Average Rating : " + ColorText.cyan(avg + " / 5.0") + "  (" + count + " reviews)");
        System.out.println(ColorText.warning("  ─────────────────────────────────────"));

        if (list.isEmpty()) {
            System.out.println(ColorText.yellow("  No approved reviews for this package yet."));
            return;
        }

        for (Feedback f : list) {
            System.out.println("\n  " + ColorText.bold(f.getCustomerName()));
            System.out.println("  Rating  : " + ColorText.cyan(f.getStarDisplay()));
            System.out.println("  Title   : " + f.getTitle());
            System.out.println("  Date    : " + f.getFeedbackDate());
            System.out.println("  Review  : " + f.getDescription());
            System.out.println(ColorText.warning("  ─────────────────────────────────────"));
        }
    }

    // ── Admin: view all feedback
    public void viewAllFeedback() {
        List<Feedback> list = dao.getAllFeedback();

        System.out.println("\n  " + ColorText.bold("All Customer Feedback"));
        System.out.println(ColorText.warning("  ─────────────────────────────────────"));

        if (list.isEmpty()) {
            System.out.println(ColorText.yellow("  No feedback records found."));
            return;
        }

        for (Feedback f : list) {
            System.out.println("\n  " + ColorText.bold("#" + f.getFeedbackId() + " — " + f.getCustomerName())
                               + ColorText.warning("  [Pkg #" + f.getPackageId() + "]"));
            System.out.println("  Destination : " + f.getDestination());
            System.out.println("  Rating      : " + ColorText.cyan(f.getStarDisplay()));
            System.out.println("  Title       : " + f.getTitle());
            System.out.println("  Status      : " + colorStatus(f.getStatus()));
            System.out.println("  Review      : " + f.getDescription());
            System.out.println(ColorText.warning("  ─────────────────────────────────────"));
        }
    }

    // ── Admin: view pending feedback 
    public void viewPendingFeedback() {
        List<Feedback> list = dao.getPendingFeedback();

        System.out.println("\n  " + ColorText.bold("Pending Feedback Queue"));
        System.out.println(ColorText.warning("  ─────────────────────────────────────"));

        if (list.isEmpty()) {
            System.out.println(ColorText.success("  No pending feedback. All reviews are actioned."));
            return;
        }

        for (Feedback f : list) {
            System.out.println("\n  " + ColorText.bold("#" + f.getFeedbackId() + " — " + f.getCustomerName()));
            System.out.println("  Package  : " + f.getDestination());
            System.out.println("  Rating   : " + ColorText.cyan(f.getStarDisplay()));
            System.out.println("  Title    : " + f.getTitle());
            System.out.println("  Date     : " + f.getFeedbackDate());
            System.out.println("  Review   : " + f.getDescription());
            System.out.println(ColorText.warning("  ─────────────────────────────────────"));
        }
    }

    // ── Admin: approve feedback 
    public void approveFeedback(int feedbackId) {
        if (dao.updateStatus(feedbackId, "approved")) {
            System.out.println(ColorText.success("  ✔  Feedback #" + feedbackId + " approved successfully."));
        } else {
            System.out.println(ColorText.error("  ✘  Feedback #" + feedbackId + " not found."));
        }
    }

    // ── Admin: reject feedback ────────────────────────────────────────────────
    public void rejectFeedback(int feedbackId) {
        if (dao.updateStatus(feedbackId, "rejected")) {
            System.out.println(ColorText.warning("  ✘  Feedback #" + feedbackId + " rejected."));
        } else {
            System.out.println(ColorText.error("  ✘  Feedback #" + feedbackId + " not found."));
        }
    }

    // ── Admin: delete feedback 
    public void deleteFeedback(int feedbackId) {
        if (dao.deleteFeedback(feedbackId)) {
            System.out.println(ColorText.success("  ✔  Feedback #" + feedbackId + " deleted successfully."));
        } else {
            System.out.println(ColorText.error("  ✘  Feedback #" + feedbackId + " not found."));
        }
    }

    // ── Check duplicate
    public boolean hasFeedback(int bookingId) {
        return dao.hasFeedback(bookingId);
    }

    // ── Helpers 
    private String colorStatus(String status) {
        if (status == null) return "";
        switch (status.toLowerCase()) {
            case "approved": return ColorText.success(status.toUpperCase());
            case "rejected": return ColorText.error(status.toUpperCase());
            default:         return ColorText.yellow(status.toUpperCase());
        }
    }
}