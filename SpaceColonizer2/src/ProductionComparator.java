import java.util.Comparator;

public class ProductionComparator implements Comparator<Planet> {

    public int compare(Planet o1, Planet o2) {
        return o2.production - o1.production;
    }
}

