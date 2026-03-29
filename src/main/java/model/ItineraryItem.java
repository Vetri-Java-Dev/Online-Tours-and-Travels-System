package model;

public class ItineraryItem {
	
	private int itemId;
    private int dayNumber;
    private String activity;
    private String location;
    public ItineraryItem(int itemId, int dayNumber, String activity, String location) {
        this.itemId = itemId;
        this.dayNumber = dayNumber;
        this.activity = activity;
        this.location = location;
    }
    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public int getDayNumber() {
        return dayNumber;
    }
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
    public String getActivity() {
        return activity;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
