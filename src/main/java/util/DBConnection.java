package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	private static final String URL =
			"jdbc:mysql://toursystem-otats-01.c.aivencloud.com:22314/toursystem?sslMode=REQUIRED";

			private static final String USERNAME = "avnadmin";
			private static final String PASSWORD = "AVNS_9baH6bJThl2lS-eehN1";

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}