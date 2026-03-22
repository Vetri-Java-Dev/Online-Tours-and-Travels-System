package main;

import java.util.Comparator;
import model.TourPackage;

public class PriceComparator implements Comparator<TourPackage> {

    public int compare(TourPackage p1, TourPackage p2) {
        return p1.getPrice() - p2.getPrice(); 
    }
    public class DurationComparator implements Comparator<TourPackage> {
        public int compare(TourPackage p1, TourPackage p2) {
            return p1.getDuration() - p2.getDuration();
        }
    }
}