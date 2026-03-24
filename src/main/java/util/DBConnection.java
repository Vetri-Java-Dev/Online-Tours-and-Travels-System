package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://centerbeam.proxy.rlwy.net:28342/railway";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "OUKaYjOKodUAFIgaspleIsRRReqHmLBq";

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