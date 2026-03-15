package model;

public class Itinerary {

    private int itineraryId;
    private int packageId;
    private String dayPlan;

    public Itinerary() {
    }

    public Itinerary(int itineraryId, int packageId, String dayPlan) {
        this.itineraryId = itineraryId;
        this.packageId = packageId;
        this.dayPlan = dayPlan;
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

    public String getDayPlan() {
        return dayPlan;
    }

    public void setDayPlan(String dayPlan) {
        this.dayPlan = dayPlan;
    }
}