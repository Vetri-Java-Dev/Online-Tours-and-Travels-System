/*
 * Author         : Harini R G
 * Description    : MessageDAO handles all database operations related to messages 
 *                  such as sending messages, retrieving unread customer messages, 
 *                  and fetching admin replies for customers.
 * Module         : Message Module (DAO Layer)
 * Java version   : 25
 */
package dao;
import model.Message;
import util.ColorText;
import util.DBConnection;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    public void sendMessage(Message msg) {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO messages (sender_id, receiver_id, content, sender_role) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, msg.getSenderId());
            ps.setInt(2, msg.getReceiverId());
            ps.setString(3, msg.getContent());
            ps.setString(4, msg.getSenderRole());

            ps.executeUpdate();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getUnreadMessages() {
        List<String> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT m.message_id, m.sender_id, m.content, u.name " +
                         "FROM messages m " +
                         "JOIN users u ON m.sender_id = u.userId " +
                         "WHERE m.is_read = FALSE AND m.sender_role = 'CUSTOMER'";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name"); 
                String msg  = rs.getString("content");
                int id      = rs.getInt("message_id");

                list.add(name + " : " + msg); 

                PreparedStatement ps = con.prepareStatement(
                    "UPDATE messages SET is_read = TRUE WHERE message_id = ?"
                );
                ps.setInt(1, id);
                ps.executeUpdate();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

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

        }
        catch (Exception e) {
        		e.printStackTrace();
        }

        return list;
    }
}