package service;
import java.util.*;
import dao.TourPackageDAO;
import model.TourPackage;

public class TourPackageService {


	    TourPackageDAO dao = new TourPackageDAO(); 

	    public void createPackage(int id, String dest, double price, int duration, int seats) {

	        TourPackage p = new TourPackage();

	        p.setPackageId(id);
	        p.setDestination(dest);
	        p.setPrice(price);
	        p.setDuration(duration);
	        p.setAvailableSeats(seats);
	        dao.addPackage(p); 
	    }
   public void displayPackages() {
        dao.viewPackages();
    }
    public List<TourPackage> searchByDestination(String destination) {
        return dao.searchByDestination(destination);
    }
    public List<TourPackage> getAllPackages() {
        return dao.getAllPackages();
        
    }
    public boolean updatePackage(int packageId, String newDestination, double newPrice, int newDuration) {
        return dao.updatePackage(packageId, newDestination, newPrice, newDuration);
    }
    public List<TourPackage> getAvailablePackages() {
        return dao.getAvailablePackages();
    }

    public List<TourPackage> getPackagesSortedByPrice() {
        return dao.getAllPackagesSortedByPrice();
    }
   
    public void deletePackage(int id) {
        dao.deletePackage(id);
    }
    public void updateAvailableSeats(int packageId, int seats) {
        dao.updateAvailableSeats(packageId, seats);
    }
}