package service;

import dao.ItineraryDAO;
import model.Itinerary;
import util.ColorText;

public class ItineraryService {

    ItineraryDAO dao = new ItineraryDAO();

    public Itinerary viewItinerary(int packageId) {
        return dao.getItineraryByPackageId(packageId);
    }

    public void deleteItinerary(int packageId) {
        if(packageId <= 0) {
            System.out.println(ColorText.error("  Invalid Package ID."));
            return;
        }
        dao.deleteItineraryByPackageId(packageId);
    }

    public void createItinerary(Itinerary itinerary) {
        int itineraryId = dao.createItinerary(itinerary);
        if(itineraryId > 0) {
            dao.addItineraryItems(itineraryId, itinerary.getItems());
        } else {
            System.out.println(ColorText.error("\n  Failed to save itinerary."));
        }
    }
}