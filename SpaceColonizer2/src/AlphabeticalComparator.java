import java.util.Comparator;

public class AlphabeticalComparator implements Comparator<Planet> {
    public int compare(Planet o1, Planet o2) {
        return o1.name.compareTo(o2.name);
    }
}
