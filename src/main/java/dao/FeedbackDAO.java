/*
 * Author         : Subhashree R
 * Created Date   : 26-March-2026
 * Description    : Data Access Object for persisting customer reviews and retrieving feedback rating statistics from the database.
 * Module         : Feedback Module
 * Java version   : 24
 */
package dao;

import model.Feedback;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    // ── Submit new feedback ───────────────────────────────────────────────────
    public boolean submitFeedback(Feedback feedback) {
        try {
            Connection con = DBConnection.getConnection();
            String checkSql = "SELECT feedbackId FROM feedback WHERE bookingId = ?";
            PreparedStatement check = con.prepareStatement(checkSql);
            check.setInt(1, feedback.getBookingId());
            if (check.executeQuery().next()) {
                System.out.println("  You have already submitted feedback for this booking.");
                return false;
            }

            String sql = "INSERT INTO feedback (bookingId, customerId, packageId, rating, title, description, status, feedbackDate) " +
                         "VALUES (?, ?, ?, ?, ?, ?, 'pending', CURRENT_DATE())";
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, feedback.getBookingId());
            ps.setInt(2, feedback.getCustomerId());
            ps.setInt(3, feedback.getPackageId());
            ps.setInt(4, feedback.getRating());
            ps.setString(5, feedback.getTitle());
            ps.setString(6, feedback.getDescription());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) feedback.setFeedbackId(rs.getInt(1));
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Get all feedback submitted by a customer ──────────────────────────────
    public List<Feedback> getFeedbackByCustomer(int customerId) {
        List<Feedback> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT f.*, p.destination " +
                         "FROM feedback f " +
                         "JOIN tour_package p ON f.packageId = p.packageId " +
                         "WHERE f.customerId = ? " +
                         "ORDER BY f.feedbackDate DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs, true));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Get approved feedback for a package (public view) ─────────────────────
    public List<Feedback> getApprovedFeedbackByPackage(int packageId) {
        List<Feedback> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT f.*, u.name AS customerName, p.destination " +
                         "FROM feedback f " +
                         "JOIN users u        ON f.customerId = u.userId " +
                         "JOIN tour_package p ON f.packageId  = p.packageId " +
                         "WHERE f.packageId = ? AND f.status = 'approved' " +
                         "ORDER BY f.feedbackDate DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Feedback fb = mapRow(rs, true);
                fb.setCustomerName(rs.getString("customerName"));
                list.add(fb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Get average rating for a package (approved only) ─────────────────────
    public double getAverageRating(int packageId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT ROUND(AVG(rating), 1) AS avg " +
                         "FROM feedback WHERE packageId = ? AND status = 'approved'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("avg");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ── Get total review count for a package (approved only) ─────────────────
    public int getReviewCount(int packageId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) AS cnt FROM feedback " +
                         "WHERE packageId = ? AND status = 'approved'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("cnt");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ── Admin: get all feedback (any status) ──────────────────────────────────
    public List<Feedback> getAllFeedback() {
        List<Feedback> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT f.*, u.name AS customerName, p.destination " +
                         "FROM feedback f " +
                         "JOIN users u        ON f.customerId = u.userId " +
                         "JOIN tour_package p ON f.packageId  = p.packageId " +
                         "ORDER BY f.feedbackDate DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Feedback fb = mapRow(rs, true);
                fb.setCustomerName(rs.getString("customerName"));
                list.add(fb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Admin: get only pending feedback ──────────────────────────────────────
    public List<Feedback> getPendingFeedback() {
        List<Feedback> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT f.*, u.name AS customerName, p.destination " +
                         "FROM feedback f " +
                         "JOIN users u        ON f.customerId = u.userId " +
                         "JOIN tour_package p ON f.packageId  = p.packageId " +
                         "WHERE f.status = 'pending' " +
                         "ORDER BY f.feedbackDate DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Feedback fb = mapRow(rs, true);
                fb.setCustomerName(rs.getString("customerName"));
                list.add(fb);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Admin: update status (approve / reject) ───────────────────────────────
    public boolean updateStatus(int feedbackId, String status) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE feedback SET status = ? WHERE feedbackId = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, feedbackId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Admin: delete feedback ────────────────────────────────────────────────
    public boolean deleteFeedback(int feedbackId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "DELETE FROM feedback WHERE feedbackId = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, feedbackId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Check if a booking already has feedback ───────────────────────────────
    public boolean hasFeedback(int bookingId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT feedbackId FROM feedback WHERE bookingId = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookingId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ── Private row mapper ────────────────────────────────────────────────────
    private Feedback mapRow(ResultSet rs, boolean hasDestination) throws Exception {
        Feedback f = new Feedback();
        f.setFeedbackId(rs.getInt("feedbackId"));
        f.setBookingId(rs.getInt("bookingId"));
        f.setCustomerId(rs.getInt("customerId"));
        f.setPackageId(rs.getInt("packageId"));
        f.setRating(rs.getInt("rating"));
        f.setTitle(rs.getString("title"));
        f.setDescription(rs.getString("description"));
        f.setStatus(rs.getString("status"));
        f.setFeedbackDate(rs.getString("feedbackDate"));
        f.setCreatedAt(rs.getString("createdAt"));
        f.setUpdatedAt(rs.getString("updatedAt"));
        if (hasDestination) {
            try { f.setDestination(rs.getString("destination")); } catch (Exception ignored) {}
        }
        return f;
    }
}
