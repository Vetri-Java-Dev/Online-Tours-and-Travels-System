package comparator;

import java.util.Comparator;
import model.TourPackage;

public class PriceComparator implements Comparator<TourPackage> {

    public int compare(TourPackage p1, TourPackage p2) {
        return Double.compare(p1.getPrice(), p2.getPrice());
    }
}