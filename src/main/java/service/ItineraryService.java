package service;

import dao.ItineraryDAO;
import model.Itinerary;
import util.ColorText;
public class ItineraryService {

    ItineraryDAO dao = new ItineraryDAO();

    public Itinerary viewItinerary(int packageId) {
        Itinerary itinerary = dao.getItineraryByPackageId(packageId);

        if(itinerary == null) {
            System.out.println(ColorText.error("  No itinerary found."));
        }

        return itinerary;
    }

    public void deleteItinerary(int packageId) {
        if(packageId <= 0) {
            System.out.println(ColorText.error("  Invalid Package ID."));
            return;
        }

        dao.deleteItineraryByPackageId(packageId);
    }

    public void createItinerary(Itinerary itinerary) {

        if(itinerary == null || itinerary.getItems() == null || itinerary.getItems().isEmpty()) {
            System.out.println(ColorText.error("  Invalid itinerary data."));
            return;
        }

        int itineraryId = dao.createItinerary(itinerary);

        if(itineraryId > 0) {
            dao.addItineraryItems(itineraryId, itinerary.getItems());
        } else {
            System.out.println(ColorText.error("\n  Failed to save itinerary."));
        }
    }
}