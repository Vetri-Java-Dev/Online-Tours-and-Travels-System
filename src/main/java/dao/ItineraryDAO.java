/*
 * Author         : Akksheetha M
 * Description    : Database operations for creating, viewing and deleting itinerary and itinerary items.
 * Module         : Itinerary Module
 * Java Version   : 24
 */

package dao;

import model.Itinerary;
import model.ItineraryItem;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItineraryDAO {

    public Itinerary getItineraryByPackageId(int packageId) {

        Itinerary itinerary = null;

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM itinerary WHERE packageId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int itineraryId = rs.getInt("itineraryId");

                List<ItineraryItem> items = getItemsByItineraryId(itineraryId);

                itinerary = new Itinerary(itineraryId, packageId, items);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return itinerary;
    }

    private List<ItineraryItem> getItemsByItineraryId(int itineraryId) {

        List<ItineraryItem> items = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM itinerary_item WHERE itineraryId = ? ORDER BY dayNumber";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, itineraryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItineraryItem item = new ItineraryItem(
                    rs.getInt("itemId"),
                    rs.getInt("dayNumber"),
                    rs.getString("activity"),
                    rs.getString("location")
                );
                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
 // ✅ CREATE ITINERARY
    public int createItinerary(Itinerary itinerary) {

        int itineraryId = 0;

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO itinerary(packageId) VALUES(?)";
            PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setInt(1, itinerary.getPackageId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                itineraryId = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return itineraryId;
    }
    
    public void deleteItineraryByPackageId(int packageId) {

        try {
            Connection con = DBConnection.getConnection();

            // First get itineraryId
            String getQuery = "SELECT itineraryId FROM itinerary WHERE packageId=?";
            PreparedStatement ps1 = con.prepareStatement(getQuery);
            ps1.setInt(1, packageId);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                int itineraryId = rs.getInt("itineraryId");

                // Delete child records first
                String deleteItems = "DELETE FROM itinerary_item WHERE itineraryId=?";
                PreparedStatement ps2 = con.prepareStatement(deleteItems);
                ps2.setInt(1, itineraryId);
                ps2.executeUpdate();

                // Then delete itinerary
                String deleteItinerary = "DELETE FROM itinerary WHERE itineraryId=?";
                PreparedStatement ps3 = con.prepareStatement(deleteItinerary);
                ps3.setInt(1, itineraryId);
                ps3.executeUpdate();
            }
            else {
                throw new exception.PackageNotFoundException("No itinerary found for this package.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItineraryItems(int itineraryId, List<ItineraryItem> items) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO itinerary_item(itineraryId, dayNumber, activity, location) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            for (ItineraryItem item : items) {
                ps.setInt(1, itineraryId);
                ps.setInt(2, item.getDayNumber());
                ps.setString(3, item.getActivity());
                ps.setString(4, item.getLocation());
                ps.addBatch();
            }

            ps.executeBatch();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}