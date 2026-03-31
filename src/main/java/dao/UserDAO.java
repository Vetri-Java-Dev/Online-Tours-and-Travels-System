package dao;

import model.User;
import util.DBConnection;
import util.EmailUtil;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

	public boolean isEmailExists(String email) {

	    try {
	        Connection con = DBConnection.getConnection();

	        String query = "SELECT 1 FROM users WHERE email = ?";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setString(1, email);

	        ResultSet rs = ps.executeQuery();

	        return rs.next();

	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	public void registerUser(User user) {

	    try {

	        Connection con = DBConnection.getConnection();

	        if (isEmailExists(user.getEmail())) {
	            System.out.println("Email already registered! Try login.");
	            return;
	        }

	        String query = "INSERT INTO users(name,email,password,phone,role) VALUES(?,?,?,?,?)";

	        PreparedStatement ps = con.prepareStatement(query);

	        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

	        ps.setString(1, user.getName());
	        ps.setString(2, user.getEmail());
	        ps.setString(3, hashedPassword);
	        ps.setString(4, user.getPhone());
	        ps.setString(5, user.getRole());

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println("User registered successfully!");
	            EmailUtil.sendWelcomeEmail(user.getEmail(), user.getName()); // ← fires only on actual insert
	        }

	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	}

    public User login(String email, String password) {

        User user = null;

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE email=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String storedPassword = rs.getString("password");

                if (PasswordUtil.verifyPassword(password, storedPassword)) {

                    user = new User(
                            rs.getInt("userId"),
                            rs.getString("name"),
                            rs.getString("email"),
                            storedPassword,
                            rs.getString("phone"),
                            rs.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
    
    public User getUserById(int userId) {

        User user = null;

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT name,email,phone FROM users WHERE userId=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                user = new User();
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return user;
    }
    
    public User getUserByEmail(String email) {

        User user = null;

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE email=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                user = new User(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("role")
                );
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return user;
    }
    
    public void updatePassword(String email, String newPassword) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "UPDATE users SET password=? WHERE email=?";

            PreparedStatement ps = con.prepareStatement(query);

            String hashedPassword = PasswordUtil.hashPassword(newPassword);

            ps.setString(1, hashedPassword);
            ps.setString(2, email);

            int rows = ps.executeUpdate();

            if(rows > 0) {
                System.out.println("Password Updated Successfully!");
            } else {
                System.out.println("Email not found!");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public boolean updateUser(int userId, String name, String phone) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "UPDATE users SET name=?, phone=? WHERE userId=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteUser(int userId) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "DELETE FROM users WHERE userId=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    
    public List<User> getAllUsers() {

        List<User> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT userId, name, email FROM users WHERE role='CUSTOMER'";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("userId"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));

                list.add(u);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    
    
    
}