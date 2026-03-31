package service;
import java.util.*;
import dao.TourPackageDAO;
import model.TourPackage;

public class TourPackageService {

    TourPackageDAO dao = new TourPackageDAO();
    

    public void createPackage(int id, String dest, int price, int duration) {

        TourPackage p = new TourPackage(id, dest, price, duration);

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
    public List<TourPackage> getAvailablePackages() {
        return dao.getAvailablePackages();
    }

    public List<TourPackage> getPackagesSortedByPrice() {
        return dao.getAllPackagesSortedByPrice();
    }
    public void updatePackage(int id, String dest, int price, int duration) {
        dao.updatePackage(id, dest, price, duration);
    }

    public void deletePackage(int id) {
        dao.deletePackage(id);
    }
    public void updateAvailableSeats(int packageId, int seats) {
        dao.updateAvailableSeats(packageId, seats);
    }
}