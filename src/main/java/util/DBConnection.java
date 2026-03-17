package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/toursystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "vetri@1234";

    public static Connection getConnection() {

        Connection con = null;

        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace(); 
        }

        return con;
    }
}