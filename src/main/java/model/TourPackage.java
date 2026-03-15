package model;

public class TourPackage {

    private int packageId;
    private String destination;
    private int price;
    private int duration;

    public TourPackage() {
    }

    public TourPackage(int packageId, String destination, int price, int duration) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
