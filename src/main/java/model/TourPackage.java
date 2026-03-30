package model;

public class TourPackage {

    private int packageId;
    private String destination;
    private double price;
    private int duration;

    public TourPackage() {
    }

    public TourPackage(int packageId, String destination, double price, int duration) {
        this.packageId = packageId;
        this.destination = destination;
        this.price = price;
        this.duration = duration;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public void setPrice(double d) {
        this.price = d;
    }
    public double getPrice() {
    	return price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    private int availableSeats;

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}
