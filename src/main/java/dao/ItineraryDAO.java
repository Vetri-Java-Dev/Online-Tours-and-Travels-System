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
                int userId      = rs.getInt("userId");

                List<ItineraryItem> items = getItemsByItineraryId(itineraryId);

                itinerary = new Itinerary(itineraryId, packageId, userId, items);
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
}