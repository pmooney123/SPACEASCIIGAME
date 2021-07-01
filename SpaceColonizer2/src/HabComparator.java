import java.util.Comparator;

public class HabComparator implements Comparator<Planet> {
    public int compare(Planet o1, Planet o2) {
        return o2.habitability - o1.habitability;
    }
}
