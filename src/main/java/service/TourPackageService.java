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
}