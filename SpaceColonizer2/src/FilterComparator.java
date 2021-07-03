import java.util.Comparator;
public class FilterComparator implements Comparator<Planet> {
    String filter;
    public FilterComparator(String string) {
        filter = string.toLowerCase();
    }

    public int compare(Planet o1, Planet o2) {

        if (!o1.name.toLowerCase().contains(filter) && o2.name.toLowerCase().contains(filter)) {
            return 1;
        }
        if (o1.name.toLowerCase().contains(filter) && !o2.name.toLowerCase().contains(filter)) {

            return -1;
        }

        return 0;
    }
}
