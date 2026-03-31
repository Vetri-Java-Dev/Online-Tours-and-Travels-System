package util;

import java.io.InputStream;
import java.net.URL;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {

            InputStream is=DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            if (is==null) {
                System.out.println("ERROR: config.properties not found!");
                return null;
            }

            Properties config = new Properties();
            config.load(is);

            String url      = config.getProperty("db.url");
            String user     = config.getProperty("db.username");
            String password = config.getProperty("db.password");

            //System.out.println("DEBUG: Connecting to " + url);

            Class.forName("com.mysql.cj.jdbc.Driver");

            URL certUrl = DBConnection.class
                    .getClassLoader()
                    .getResource("ca.pem");

            Properties connProps = new Properties();
            connProps.setProperty("user",     user);
            connProps.setProperty("password", password);
            connProps.setProperty("ssl-mode", "VERIFY_CA");

            if (certUrl != null) {
                String certPath = new File(certUrl.toURI()).getAbsolutePath();
                connProps.setProperty("sslrootcert", certPath);
            }
            else {
                //System.out.println("Without cert ssl");
            }

            con = DriverManager.getConnection(url, connProps);

        }
        catch (Exception e) {
            System.out.println("DB CONNECTION FAILED : " + e.getMessage());
            e.printStackTrace();
        }

        return con;
    }
}