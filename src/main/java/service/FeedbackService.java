package service;

import dao.FeedbackDAO;
import model.Feedback;
import util.ColorText;

import java.util.List;

public class FeedbackService {

    private final FeedbackDAO dao = new FeedbackDAO();

    // ── Submit feedback (customer) ────────────────────────────────────────────
    public boolean submitFeedback(int bookingId, int customerId, int packageId,
                                  int rating, String title, String description) {

        // --- validation ---
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
            System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
            System.out.println(ColorText.warning("  ║") + ColorText.success("  ✔  Feedback submitted successfully!             ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("  ║") + ColorText.yellow("     It will appear after admin approval.         ") + ColorText.warning("║"));
            System.out.println(ColorText.warning("  ╠══════════════════════════════════════════════════╣"));
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Feedback ID") + " : %-34d" + ColorText.warning("║") + "%n", feedback.getFeedbackId());
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Rating     ") + " : %-34s" + ColorText.warning("║") + "%n", feedback.getStarDisplay());
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Title      ") + " : %-34s" + ColorText.warning("║") + "%n", feedback.getTitle());
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Status     ") + " : %-34s" + ColorText.warning("║") + "%n", "pending (awaiting approval)");
            System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));
        }
        return ok;
    }

    // ── View my feedback (customer) ───────────────────────────────────────────
    public void viewMyFeedback(int customerId) {
        List<Feedback> list = dao.getFeedbackByCustomer(customerId);

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("            MY FEEDBACK & RATINGS             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════════════╝"));

        if (list.isEmpty()) {
            System.out.println(ColorText.yellow("  No feedback submitted yet."));
            return;
        }

        for (Feedback f : list) {
            System.out.println(ColorText.warning("\n  ┌───────────────────────────────────────────┐"));
            System.out.printf (ColorText.warning("  │") + ColorText.bold("  #%-4d") + " │ %-28s " + ColorText.warning("│") + "%n", f.getFeedbackId(), truncate(f.getDestination(), 28));
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Rating  ") + ": %-34s" + ColorText.warning("│") + "%n", f.getStarDisplay());
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Title   ") + ": %-34s" + ColorText.warning("│") + "%n", truncate(f.getTitle(), 34));
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Status  ") + ": %-34s" + ColorText.warning("│") + "%n", f.getStatus().toUpperCase());
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Date    ") + ": %-34s" + ColorText.warning("│") + "%n", f.getFeedbackDate());
            System.out.println(ColorText.warning("  │") + "  " + ColorText.cyan("Review  ") + ":                                " + ColorText.warning("│"));
            for (String line : wrapText(f.getDescription(), 43)) {
                System.out.printf(ColorText.warning("  │") + "    %-43s" + ColorText.warning("│") + "%n", line);
            }
            System.out.println(ColorText.warning("  └───────────────────────────────────────────┘"));
        }
    }

    // ── View approved reviews for a package (customer) ────────────────────────
    public void viewPackageReviews(int packageId) {
        List<Feedback> list = dao.getApprovedFeedbackByPackage(packageId);
        double avg    = dao.getAverageRating(packageId);
        int    count  = dao.getReviewCount(packageId);

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════════════╗"));
        System.out.printf (ColorText.warning("║") + ColorText.bold("       PACKAGE #%-4d REVIEWS                  ") + ColorText.warning("║") + "%n", packageId);
        System.out.println(ColorText.warning("╠══════════════════════════════════════════════╣"));
        System.out.printf (ColorText.warning("║") + "  " + ColorText.cyan("★  Average Rating") + " : %-4.1f / 5.0  (%2d reviews) " + ColorText.warning("║") + "%n", avg, count);
        System.out.println(ColorText.warning("╚══════════════════════════════════════════════╝"));

        if (list.isEmpty()) {
            System.out.println(ColorText.yellow("  No approved reviews for this package yet."));
            return;
        }

        for (Feedback f : list) {
            System.out.println(ColorText.warning("  ┌───────────────────────────────────────────┐"));
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Customer") + " : %-31s" + ColorText.warning("│") + "%n", truncate(f.getCustomerName(), 31));
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Rating  ") + " : %-31s" + ColorText.warning("│") + "%n", f.getStarDisplay());
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Title   ") + " : %-31s" + ColorText.warning("│") + "%n", truncate(f.getTitle(), 31));
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Date    ") + " : %-31s" + ColorText.warning("│") + "%n", f.getFeedbackDate());
            System.out.println(ColorText.warning("  │") + "  " + ColorText.cyan("Review  ") + " :                               " + ColorText.warning("│"));
            for (String line : wrapText(f.getDescription(), 43)) {
                System.out.printf(ColorText.warning("  │") + "    %-41s" + ColorText.warning("│") + "%n", line);
            }
            System.out.println(ColorText.warning("  └───────────────────────────────────────────┘"));
        }
    }

    public void viewAllFeedback() {
        List<Feedback> list = dao.getAllFeedback();

        System.out.println(ColorText.warning("\n  ╔═══════════════════════════════════════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("                           ALL CUSTOMER FEEDBACK                           ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╠══════╦══════════════════╦══════════════════╦══════════════╦═══════════════╦═══════╣"));
        System.out.println(ColorText.warning("  ║") + ColorText.cyan("  ID  ") + ColorText.warning("║") + ColorText.cyan(" Customer         ") + ColorText.warning("║") + ColorText.cyan(" Destination    ") + ColorText.warning("║") + ColorText.cyan(" Rating       ") + ColorText.warning("║") + ColorText.cyan(" Status        ") + ColorText.warning("║") + ColorText.cyan(" PkgID ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╠══════╬══════════════════╬══════════════════╬══════════════╬═══════════════╬═══════╣"));

        if (list.isEmpty()) {
            System.out.println(ColorText.warning("  ║") + ColorText.yellow("                    No feedback records found.                       ") + ColorText.warning("║"));
        } else {
            for (Feedback f : list) {
                System.out.printf(ColorText.warning("  ║") + " %-4d " + ColorText.warning("║") + " %-16s " + ColorText.warning("║") + " %-16s " + ColorText.warning("║") + " %-12s " + ColorText.warning("║") + " %-13s " + ColorText.warning("║") + " %-5d " + ColorText.warning("║") + "%n",
                    f.getFeedbackId(),
                    truncate(f.getCustomerName(), 16),
                    truncate(f.getDestination(), 16),
                    f.getStarDisplay().length() > 12 ? f.getStarDisplay().substring(0, 11) + "…" : f.getStarDisplay(),
                    f.getStatus().toUpperCase(),
                    f.getPackageId());
                
                System.out.printf(ColorText.warning("  ║") + "      " + ColorText.warning("╠") + "══════════════════╩══════════════════╩══════════════╩═══════════════╩═══════╣" + "%n");
                System.out.printf(ColorText.warning("  ║") + "      " + ColorText.warning("║") + " " + ColorText.cyan("Title:") + " %-66s " + ColorText.warning("║") + "%n", truncate(f.getTitle(), 66));
                System.out.println(ColorText.warning("  ╠══════╩═══════════════════════════════════════════════════════════════════════════╣"));
            }
        }
        System.out.println(ColorText.warning("  ╚═══════════════════════════════════════════════════════════════════════════════════╝"));
    }

    public void viewPendingFeedback() {
        List<Feedback> list = dao.getPendingFeedback();

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           PENDING FEEDBACK QUEUE             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════════════╝"));

        if (list.isEmpty()) {
            System.out.println(ColorText.success("  No pending feedback. All reviews are actioned."));
            return;
        }

        for (Feedback f : list) {
            System.out.println(ColorText.warning("\n  ┌───────────────────────────────────────────┐"));
            System.out.printf (ColorText.warning("  │") + ColorText.bold("  Feedback #%-4d") + "  by %-20s" + ColorText.warning("│") + "%n",
                               f.getFeedbackId(), truncate(f.getCustomerName(), 20));
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Package  ") + ": %-31s" + ColorText.warning("│") + "%n", truncate(f.getDestination(), 31));
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Rating   ") + ": %-31s" + ColorText.warning("│") + "%n", f.getStarDisplay());
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Title    ") + ": %-31s" + ColorText.warning("│") + "%n", truncate(f.getTitle(), 31));
            System.out.printf (ColorText.warning("  │") + "  " + ColorText.cyan("Date     ") + ": %-31s" + ColorText.warning("│") + "%n", f.getFeedbackDate());
            System.out.println(ColorText.warning("  │") + "  " + ColorText.cyan("Description:") + "                             " + ColorText.warning("│"));
            for (String line : wrapText(f.getDescription(), 43)) {
                System.out.printf(ColorText.warning("  │") + "    %-43s" + ColorText.warning("│") + "%n", line);
            }
            System.out.println(ColorText.warning("  └───────────────────────────────────────────┘"));
        }
    }

    // ── Admin: approve feedback ───────────────────────────────────────────────
    public void approveFeedback(int feedbackId) {
        if (dao.updateStatus(feedbackId, "approved")) {
            System.out.println(ColorText.success("  ╔══════════════════════════════════════╗"));
            System.out.println(ColorText.success("  ║  ✔  Feedback #" + String.format("%-4d", feedbackId) + " APPROVED.          ║"));
            System.out.println(ColorText.success("  ╚══════════════════════════════════════╝"));
        } else {
            System.out.println(ColorText.error("  ╔══════════════════════════════════════╗"));
            System.out.println(ColorText.error("  ║  ✘  Feedback #" + String.format("%-4d", feedbackId) + " not found.         ║"));
            System.out.println(ColorText.error("  ╚══════════════════════════════════════╝"));
        }
    }

    // ── Admin: reject feedback ────────────────────────────────────────────────
    public void rejectFeedback(int feedbackId) {
        if (dao.updateStatus(feedbackId, "rejected")) {
            System.out.println(ColorText.warning("  ╔══════════════════════════════════════╗"));
            System.out.println(ColorText.warning("  ║  ✘  Feedback #" + String.format("%-4d", feedbackId) + " REJECTED.          ║"));
            System.out.println(ColorText.warning("  ╚══════════════════════════════════════╝"));
        } else {
            System.out.println(ColorText.error("  ╔══════════════════════════════════════╗"));
            System.out.println(ColorText.error("  ║  ✘  Feedback #" + String.format("%-4d", feedbackId) + " not found.         ║"));
            System.out.println(ColorText.error("  ╚══════════════════════════════════════╝"));
        }
    }

    // ── Admin: delete feedback ────────────────────────────────────────────────
    public void deleteFeedback(int feedbackId) {
        if (dao.deleteFeedback(feedbackId)) {
            System.out.println(ColorText.success("  ╔══════════════════════════════════════╗"));
            System.out.println(ColorText.success("  ║  ✔  Feedback #" + String.format("%-4d", feedbackId) + " DELETED.           ║"));
            System.out.println(ColorText.success("  ╚══════════════════════════════════════╝"));
        } else {
            System.out.println(ColorText.error("  ╔══════════════════════════════════════╗"));
            System.out.println(ColorText.error("  ║  ✘  Feedback #" + String.format("%-4d", feedbackId) + " not found.         ║"));
            System.out.println(ColorText.error("  ╚══════════════════════════════════════╝"));
        }
    }

    // ── Check duplicate ───────────────────────────────────────────────────────
    public boolean hasFeedback(int bookingId) {
        return dao.hasFeedback(bookingId);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private String truncate(String s, int max) {
        if (s == null || s.isEmpty()) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private String colorStatus(String status) {
        if (status == null) return "";
        switch (status.toLowerCase()) {
            case "approved": return ColorText.success(status);
            case "rejected": return ColorText.error(status);
            default:         return ColorText.warning(status);
        }
    }

    private String[] wrapText(String text, int width) {
        if (text == null || text.isEmpty()) return new String[]{""};
        int len = text.length();
        int lines = (int) Math.ceil((double) len / width);
        String[] result = new String[lines];
        for (int i = 0; i < lines; i++) {
            int start = i * width;
            int end   = Math.min(start + width, len);
            result[i] = text.substring(start, end);
        }
        return result;
    }
}