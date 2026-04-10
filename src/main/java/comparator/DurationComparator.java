package comparator;

import model.TourPackage;
import java.util.Comparator;

public class DurationComparator implements Comparator<TourPackage> {

    private boolean ascending;

    public DurationComparator(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int compare(TourPackage a, TourPackage b) {
        if (ascending)
            return Integer.compare(a.getDuration(), b.getDuration());
        else
            return Integer.compare(b.getDuration(), a.getDuration());
    }
}