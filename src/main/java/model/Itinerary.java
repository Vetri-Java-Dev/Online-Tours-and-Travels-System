package model;
import java.util.List;

public class Itinerary {

    private int itineraryId;
    private int packageId;
    private List<ItineraryItem> items;

    public Itinerary(int itineraryId, int packageId, List<ItineraryItem> items) {
        this.itineraryId = itineraryId;
        this.packageId = packageId;
        this.items = items;
    }

    public int getItineraryId() {
        return itineraryId;
    }

    public int getPackageId() {
        return packageId;
    }

    public List<ItineraryItem> getItems() {
        return items;
    }
}