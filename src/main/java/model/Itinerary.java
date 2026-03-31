package model;
import java.util.*;
public class Itinerary {
	
	private int itineraryId;
	private int packageId;
	private int userId;
	private List<ItineraryItem> items;
	
    public Itinerary(int itineraryId, int packageId, int userId, List<ItineraryItem> items) {
        this.itineraryId = itineraryId;
        this.packageId = packageId;
        this.userId = userId;
        this.items = items;
    }
    public int getItineraryId() {
        return itineraryId;
    }
    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }
    public int getPackageId() {
        return packageId;
    }
    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public List<ItineraryItem> getItems() {
        return items;
    }
    public void setItems(List<ItineraryItem> items) {
        this.items = items;
    }
}

