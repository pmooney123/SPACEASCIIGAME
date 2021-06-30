import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Galaxy {
    public Random random = new Random();
    public Tile[][] tileMap;
    public int width;
    public int height;
    public ArrayList<Planet> allPlanets = new ArrayList<>();
    public ArrayList<Star> allStars = new ArrayList<>();
    ArrayList<String> potentialNames = new ArrayList<>();

    public Galaxy(int width, int height, Civ player) {
        this.width = width;
        this.height = height;
        setNameArray();
        this.tileMap = makeWorld();
        populateStars();
        allPlanets.get(0).setOwner(player);


    }
    public String getName() {
        String string = potentialNames.get(random.nextInt(potentialNames.size() - 1));
        potentialNames.remove(string);

        return string;

    }
    public void setNameArray() {
        String[] names = {
                "Trappist", "Leo", "Ares", "Rigel", "Capricorn", "Aquarius", "Virgo", "Pisces", "Sagittarius",
                "Sirius", "Vega", "Deneb", "Pollux", "Antares", "Ursae", "Gemina", "Pegasi", "Bellatrix",
                "Scorpio", "Capella", "Castor", "Centauri", "Satyr",
                "Glob", "Rhea", "Patrick", "Hades", "Hercules", "Megara", "Apollo", "Persephone", "Cerberus",
                "Tau Ceti", "Theta-9", "Wolf", "Draconis", "Polaris", "Cassiopeia", "Procyon", "Hubble", "Heaven",
        };

        potentialNames.addAll(Arrays.asList(names));

    }

    public void addStar(Star star) {
        allStars.add(star);
    }

    public void populateStars() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tile(x, y).glyph() != Tile.SPACE.glyph() && tile(x, y).glyph() != Tile.BGSTAR2.glyph() && tile(x, y).glyph() != Tile.BGSTAR.glyph()){
                    System.out.println("added");
                    Star star = new Star(x, y, "Star" + x + ":" + y);

                    star.name = getName();
                    for (int z = 0; z < 3; z++) {
                        Planet planet = new Planet(star.name);
                        planet.name = star.getName();
                        star.addPlanet(planet);
                        allPlanets.add(planet);
                    }

                    addStar(star);

                }
            }
        }
    }
    public Star getStar(int x,int y) {
        for (Star star : allStars) {
            if (star.x == x && star.y == y) {
                return star;
            }
        }
        return null;
    }
    public boolean starHere(int x, int y) {
        for (Star star : allStars) {
            if (star.x == x && star.y == y) {
                return true;
            }
        }
        return false;
    }
    public ArrayList<Planet> colonizedBy(Civ civ) {
        ArrayList<Planet> planets = new ArrayList<>();
        for (Planet planet : allPlanets) {
            if (planet.owner == civ) {
                planets.add(planet);
            }
        }

        return planets;

    }
    public ArrayList<Planet> notColonizedBy(Civ civ) {
        ArrayList<Planet> planets = new ArrayList<>();
        for (Planet planet : allPlanets) {
            if (planet.owner != civ) {
                planets.add(planet);
            }
        }
        return planets;
    }


    public Tile[][] makeWorld() {
        Tile[][] tiles = new Tile[width][height];
        int maxStars = 15;
        int stars = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                if (NoStarsNear(tiles, x, y)) {
                    tiles[x][y] = Math.random() < 0.96 ? //chance for a star
                            Tile.SPACE : Math.random() < 0.1 ?
                            Tile.NEUTRONSTAR : Math.random() < 0.1 ?
                            Tile.REDGIANT : Math.random() < 0.5 ?
                            Tile.YELLOWSTAR : Tile.WHITESTAR;
                    ;
                } else {
                    tiles[x][y] = tiles[x][y] = Math.random() < 0.99 ? //chance for a star near to another star
                            Tile.SPACE : Math.random() < 0.1 ?
                            Tile.NEUTRONSTAR : Math.random() < 0.1 ?
                            Tile.REDGIANT : Math.random() < 0.5 ?
                            Tile.YELLOWSTAR : Tile.WHITESTAR;
                    ;;
                }
                if (tiles[x][y] == Tile.SPACE) {
                    if (Math.random() < 0.05) {
                        tiles[x][y] = Math.random() < 0.5 ? Tile.BGSTAR : Tile.BGSTAR2;
                    }
                } else {
                    stars++;
                }
            }
        }


        return tiles;
    }
    public boolean NoStarsNear(Tile[][] tiles, int x, int y) {
        int distance = 4;
        for (int z = x - distance; z < x + distance; z++ ) {
            for (int j = y - distance; j < y + distance; j++) {
                if (z > 0 && z < tiles.length && j > 0 && j < tiles[0].length) {
                if (tiles[z][j] != Tile.SPACE && tiles[z][j] != null && tiles[z][j] != Tile.BOUNDS) {
                    return false;
                }
                }
            }
        }
        return true;
    }


    public Tile tile(int x, int y){
        if (x < 0 || x >= width || y < 0 || y >= height)
            return Tile.BOUNDS;
        else
            return tileMap[x][y];
    } //tile getter

    public char glyph(int x, int y){
        return tile(x, y).glyph();
    } //get tile char

    public Color color(int x, int y){
        return tile(x, y).color();
    } //get tile color


}
