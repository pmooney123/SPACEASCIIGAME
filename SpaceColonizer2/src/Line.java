import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Line implements Iterator<Point> {
    private ArrayList<Point> points;

    public ArrayList<Point> getPoints() {
        return points;
    }

    public Line(int x0, int y0, int x1, int y1) {
        points = new ArrayList<Point>();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            points.add(new Point(x0, y0));

            if (x0 == x1 && y0 == y1) {
                break;
            }

            int e2 = err * 2;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
    public Iterator<Point> iterator() {
        return points.iterator();
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Point next() {
        return null;
    }
}
