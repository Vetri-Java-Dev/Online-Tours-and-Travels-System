package service;

import dao.ItineraryDAO;
import model.Itinerary;

public class ItineraryService {

    ItineraryDAO dao = new ItineraryDAO();

    public Itinerary viewItinerary(int packageId) {
        return dao.getItineraryByPackageId(packageId);
    }
}