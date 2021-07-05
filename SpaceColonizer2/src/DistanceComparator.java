import java.util.Comparator;

public class DistanceComparator<S> implements Comparator<Star> {
    Fleet fleet;


    public DistanceComparator(Fleet fleet) {
        this.fleet = fleet;

    }
    public double distance(double x, double y, double x2, double y2) {
        return Math.sqrt((x2 - x)*(x2 - x) + (y2 - y) * (y2 - y));
    }

    public int compare(Star o1, Star o2) {
        int d1 = (int) (5 * distance(fleet.currentStar.x, fleet.currentStar.y, o1.x, o1.y));
        int d2 = (int) (5 * distance(fleet.currentStar.x, fleet.currentStar.y, o2.x, o2.y));


        return d1 - d2;
    }
}

