import java.awt.*;

public class AsciiImage {
    public Tile[][] pixels;
    public int width;
    public int height;

    public AsciiImage(int width, int height, int type, int radius, int polarity) {
        this.width = width;
        this.height = height;
        pixels = new Tile[width][height];
        switch (type) {
            case (0): //earthlike
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        pixels[x][y] = Math.random() < 0.5 ? Tile.PLANETGREEN : Tile.PLANETBLUE;
                    }
                }
                break;
            case (1): //marslike
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        pixels[x][y] = Math.random() < 0.5 ? Tile.PLANETRED : Tile.PLANETPINK;
                    }
                }
                break;
            case (2):
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        pixels[x][y] = Math.random() < 0.5 ? Tile.PLANETBROWN : Tile.PLANETLIGHTBROWN;
                    }
                }
                break;
            case (3):
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        pixels[x][y] = Math.random() < 0.5 ? Tile.PLANETGREY : Tile.PLANETDARKGREY;
                    }
                }
                break;
        }



        int centerX = width/2;
        int centerY = height/2;
        int polarRadius = radius - polarity;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y > centerY + polarRadius || y < centerY - polarRadius) {
                    pixels[x][y] = Tile.PLANETPOLAR;
                }
                if (y == centerY + polarRadius || y == centerY - polarRadius) {
                    if (Math.random() > 0.5) {
                        pixels[x][y] = Tile.PLANETPOLAR;
                    }
                }
            }
        }


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
