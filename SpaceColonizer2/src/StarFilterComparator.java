import java.util.Comparator;
public class StarFilterComparator implements Comparator<Star> {
    String filter;
    public StarFilterComparator(String string) {
        filter = string.toLowerCase();
    }

    public int compare(Star o1, Star o2) {

        if (!o1.name.toLowerCase().contains(filter) && o2.name.toLowerCase().contains(filter)) {
            return 1;
        }
        if (o1.name.toLowerCase().contains(filter) && !o2.name.toLowerCase().contains(filter)) {

            return -1;
        }

        return 0;
    }
}
