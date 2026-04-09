package comparator;

import model.TourPackage;
import java.util.Comparator;

public class PriceComparator implements Comparator<TourPackage> {

    private boolean ascending;

    public PriceComparator(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int compare(TourPackage a, TourPackage b) {
        if (ascending)
            return Double.compare(a.getPrice(), b.getPrice());
        else
            return Double.compare(b.getPrice(), a.getPrice());
    }
}