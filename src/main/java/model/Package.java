package model;

public class Package {
	
	private int packageId;
	private String title;
	private String location;
	private double price;
	private String startDate;
	private String endDate;
	
	public Package(int packageId, String title, String location, double price, int availableSeats, String startDate, String endDate) {
        this.packageId = packageId;
        this.title = title;
        this.location = location;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public int getPackageId() {
        return packageId;
    }
    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
