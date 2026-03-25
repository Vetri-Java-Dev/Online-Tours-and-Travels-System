package dao;

import model.Message;
import util.DBConnection;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    // SEND MESSAGE
    public void sendMessage(Message msg) {
        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO messages (sender_id, receiver_id, content, sender_role) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, msg.getSenderId());
            ps.setInt(2, msg.getReceiverId());
            ps.setString(3, msg.getContent());
            ps.setString(4, msg.getSenderRole());

            ps.executeUpdate();
            System.out.println("✅ Message Sent!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ADMIN VIEW
    public List<String> getUnreadMessages() {
        List<String> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM messages WHERE is_read=FALSE AND sender_role='CUSTOMER'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int cid = rs.getInt("sender_id");
                String msg = rs.getString("content");

                list.add("Customer " + cid + " : " + msg);

                // mark as read
                int id = rs.getInt("message_id");
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE messages SET is_read=TRUE WHERE message_id=?"
                );
                ps.setInt(1, id);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // CUSTOMER VIEW
    public List<String> getReplies(int customerId) {
        List<String> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM messages WHERE receiver_id=? AND sender_role='ADMIN'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("content"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}