import java.awt.*;

public class AsciiImage {
    public Tile[][] pixels;
    public int width;
    public int height;
    Color color1 = new Color(194, 34, 34);
    Color color2 = new Color(33, 32, 32);


    public AsciiImage(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x][y] = Math.random() < 0.5 ? Tile.PLANET : Tile.PLANET2;
            }
        }

        int centerX = width/2;
        int centerY = height/2;
        int radius = width/3;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (distance(x, centerX, y, centerY) >= radius) {
                    pixels[x][y] = Math.random() < 0.8 ? Tile.IMAGESPACE : Math.random() < 0.5 ? Tile.BGSTAR : Tile.BGSTAR2;
                }
            }
        }
    }

    public double distance(int x, int x2, int y, int y2) {
        return Math.sqrt((x2 - x)*(x2 - x) + (y2 - y) *(y2 - y));
    }


}
