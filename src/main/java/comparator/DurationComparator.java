package comparator;

import java.util.Comparator;
import model.TourPackage;

public class DurationComparator implements Comparator<TourPackage> {

    @Override
    public int compare(TourPackage p1, TourPackage p2) {
        return p1.getDuration() - p2.getDuration();
    }
}