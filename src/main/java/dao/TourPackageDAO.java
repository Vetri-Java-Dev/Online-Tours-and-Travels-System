package dao;

import model.TourPackage;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TourPackageDAO {

    List<TourPackage> packages = new ArrayList<>();

    public void addPackage(TourPackage tourPackage) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "insert into tour_package values(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, tourPackage.getPackageId());
            ps.setString(2, tourPackage.getDestination());
            ps.setInt(3, tourPackage.getPrice());
            ps.setInt(4, tourPackage.getDuration());

            ps.executeUpdate();

            System.out.println("Package saved to database");

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewPackages() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "select * from tour_package";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {

                System.out.println(
                    rs.getInt("packageId") + " " +
                    rs.getString("destination") + " " +
                    rs.getInt("price") + " " +
                    rs.getInt("duration")
                );
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}