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

            String query = "INSERT INTO tour_package (packageId, destination, price, duration, availableSeats) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, tourPackage.getPackageId());
            ps.setString(2, tourPackage.getDestination());
            ps.setDouble(3, tourPackage.getPrice());
            ps.setInt(4, tourPackage.getDuration());
            ps.setInt(5, tourPackage.getAvailableSeats()); 
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
                    rs.getDouble("price"),
                    rs.getInt("duration"),
                    rs.getInt("availableSeats")
                );
                tourPackage.setAvailableSeats(rs.getInt("availableSeats"));
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

            System.out.println("-----------------------------------------------------------------------------------");
            System.out.printf("| %-10s | %-35s | %-10s | %-15s | %-10s |\n", 
                    "PackageID", "Destination", "Price", "Duration", "Seats");System.out.println("-----------------------------------------------------------------------------------");

            while(rs.next()) {
            	System.out.printf("| %-10d | %-35s | %-10.2f | %-15d | %-10d |\n",
            	        rs.getInt("packageId"),
            	        rs.getString("destination"),
            	        rs.getDouble("price"),
            	        rs.getInt("duration"),
            	        rs.getInt("availableSeats"));
            }

            System.out.println("-----------------------------------------------------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<TourPackage> searchByDestination(String destination) {

        List<TourPackage> list = new ArrayList<>();

        String sql = "SELECT * FROM tour_package WHERE LOWER(destination) LIKE LOWER(?)";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + destination + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                TourPackage tp = new TourPackage(
                    rs.getInt("packageId"),
                    rs.getString("destination"),
                    rs.getDouble("price"),
                    rs.getInt("duration"),
                    rs.getInt("availableSeats")
                );
                tp.setAvailableSeats(rs.getInt("availableSeats")); 
                list.add(tp);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<TourPackage> getAvailablePackages() {
        List<TourPackage> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM tour_package";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TourPackage tp = new TourPackage();
                tp.setPackageId(rs.getInt("packageId"));
                tp.setDestination(rs.getString("destination"));
                tp.setPrice(rs.getDouble("price"));
                tp.setDuration(rs.getInt("duration"));
                tp.setAvailableSeats(rs.getInt("availableSeats"));
                list.add(tp);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<TourPackage> getAllPackagesSortedByPrice() {
        List<TourPackage> list = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM tour_package ORDER BY price ASC";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TourPackage tp = new TourPackage();
                tp.setPackageId(rs.getInt("packageId"));
                tp.setDestination(rs.getString("destination"));
                tp.setPrice(rs.getDouble("price"));
                tp.setDuration(rs.getInt("duration"));
                list.add(tp);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    public List<TourPackage> getAllPackages() {

        List<TourPackage> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM tour_package";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                TourPackage tp = new TourPackage();

                tp.setPackageId(rs.getInt("packageId"));
                tp.setDestination(rs.getString("destination"));
                tp.setPrice(rs.getDouble("price"));
                tp.setDuration(rs.getInt("duration"));
                tp.setAvailableSeats(rs.getInt("availableSeats"));
                list.add(tp);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
            return list;
        }
    
        public void updateAvailableSeats(int packageId, int seats) {

            try {
                Connection con = DBConnection.getConnection();

                String query = "UPDATE tour_package SET availableSeats=? WHERE packageId=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, seats);
                ps.setInt(2, packageId);

                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

                
        public boolean updatePackage(int packageId, String destination, double price, int duration) {
            try {
                Connection con = DBConnection.getConnection();
                String query = "UPDATE tour_package SET destination=?, price=?, duration=? WHERE packageId=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, destination);
                ps.setDouble(2, price);  
                ps.setInt(3, duration);
                ps.setInt(4, packageId);

                int rows = ps.executeUpdate();

                return rows > 0;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    public boolean deletePackage(int packageId) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "DELETE FROM tour_package WHERE packageId=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, packageId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Package deleted successfully!");
                return true;
            } else {
                System.out.println("Package ID not found!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}

