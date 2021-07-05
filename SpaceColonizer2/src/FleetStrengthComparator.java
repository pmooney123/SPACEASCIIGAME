import java.util.Comparator;

public class FleetStrengthComparator implements Comparator<Fleet> {
    @Override
    public int compare(Fleet o1, Fleet o2) {
        return o2.total_power - o1.total_power;
    }
}
