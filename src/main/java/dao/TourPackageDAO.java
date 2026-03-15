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

    public TourPackage getPackageById(int packageId) {
        TourPackage tourPackage = null;

        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM tour_package WHERE packageId=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, packageId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tourPackage = new TourPackage(
                    rs.getInt("packageId"),
                    rs.getString("destination"),
                    rs.getInt("price"),
                    rs.getInt("duration")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tourPackage;
    }
    
    public void viewPackages() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM tour_package";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            System.out.println("---------------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-8s | %-8s |\n", "PackageID", "Destination", "Price", "Duration (Days)");
            System.out.println("---------------------------------------------------------------------");

            while(rs.next()) {
                System.out.printf("| %-10d | %-20s | %-8d | %-8d |\n",
                        rs.getInt("packageId"),
                        rs.getString("destination"),
                        rs.getInt("price"),
                        rs.getInt("duration"));
            }

            System.out.println("---------------------------------------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


}