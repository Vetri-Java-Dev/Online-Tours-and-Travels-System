package dao;

import model.User;
import util.DBConnection;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public void registerUser(User user) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO users(name,email,password,phone,role) VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashedPassword);
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());

            ps.executeUpdate();

            System.out.println("User registered successfully!");

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
}