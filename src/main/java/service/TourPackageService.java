package service;

import model.TourPackage;
import dao.TourPackageDAO;

public class TourPackageService {

    TourPackageDAO dao = new TourPackageDAO();

    public void createPackage(int id, String dest, int price, int duration) {

        TourPackage p = new TourPackage(id, dest, price, duration);

        dao.addPackage(p);
    }

    public void displayPackages() {
        dao.viewPackages();
    }
}