import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Star {
    String name;
    String type;

    int x; int y;


    Galaxy galaxy;

    ArrayList<Star> connectedStars = new ArrayList<>();

    ArrayList<Planet> planets = new ArrayList<>();

    ArrayList<String> potentialNames = new ArrayList<>();

    ArrayList<Fleet> orbitingFleets = new ArrayList<>();
    Color color;
    //fleet stuff
    public void removeFleet(Fleet fleet) {
        orbitingFleets.remove(fleet);
    }

    public void addFleet(Fleet fleet) {
        orbitingFleets.add(fleet);
    }

    public ArrayList<Civ> playersHere() {
        ArrayList<Civ> playersHere = new ArrayList<>();
        for (Fleet fleet : orbitingFleets) {
            if (!playersHere.contains(fleet.ownerCiv)) {
                playersHere.add(fleet.ownerCiv);
            }
        }
        return  playersHere;
    }
    public ArrayList<Note> fleetStats() {
        ArrayList<Note> notes = new ArrayList<>();
        for (Civ player : playersHere()) {
            int total_power = 0;
            int total_pop = 0;
            Color color = player.color;
            for (Fleet fleet : orbitingFleets) {
                if (fleet.ownerCiv == player) {
                    total_power += fleet.total_power;
                    if (fleet.colonyShip) {
                        total_pop += fleet.population;
                    }
                }
            }
            if (total_power >= 1) {
                notes.add(new Note(total_power + "*", color));
            }
            if (total_pop > 0) {
                notes.add(new Note(total_pop +""+ (char)1, color));
            }


        }
        return notes;
    }
    public void update() {
        mergeFleets();
    }
    public void mergeFleets() {
        for (int x = 0; x < orbitingFleets.size(); x++) {
            for (int z = 0; z < orbitingFleets.size(); z++) {
                    Fleet fleet = orbitingFleets.get(x);
                    Fleet fleet2 = orbitingFleets.get(z);
                    if (fleet != fleet2 && fleet.automerge && fleet2.automerge) {
                        if (fleet.ownerCiv == fleet2.ownerCiv) {
                            Fleet biggerFleet = fleet.total_power > fleet2.total_power ? fleet : fleet2;
                            Fleet smallerFleet = fleet.total_power <= fleet2.total_power ? fleet : fleet2;
                            biggerFleet.total_power += smallerFleet.total_power;

                            orbitingFleets.remove(smallerFleet);
                            for (Civ civ : galaxy.civs) {
                                civ.fleets.remove(smallerFleet);
                            }
                            z--;
                        }
                    }
            }
        }
    }
    public Color politicalColor() {
        ArrayList<Color> colors = new ArrayList<>();
        for (Planet planet : planets) {
            if (planet.colonized) {
                colors.add(planet.getOwnerColor());
            }
        }
        if (colors.size() > 0) {
            return blend(colors);
        }
        return Color.gray;
    }
    public Star(int x, int y, String name, Tile tile, Galaxy galaxy) {
        this.color = tile.color();
        this.galaxy = galaxy;
        setNameArray();
        this.x = x;
        this.y = y;
        this.name = name;
    }
    public static Color blend(ArrayList<Color> c) {
        if (c == null || c.size() <= 0) {
            return null;
        }
        float ratio = 1f / ((float) c.size());

        int a = 0;
        int r = 0;
        int g = 0;
        int b = 0;

        for (int i = 0; i < c.size(); i++) {
            int rgb = c.get(i).getRGB();
            int a1 = (rgb >> 24 & 0xff);
            int r1 = ((rgb & 0xff0000) >> 16);
            int g1 = ((rgb & 0xff00) >> 8);
            int b1 = (rgb & 0xff);
            a += ((int) a1 * ratio);
            r += ((int) r1 * ratio);
            g += ((int) g1 * ratio);
            b += ((int) b1 * ratio);
        }

        return new Color(a << 24 | r << 16 | g << 8 | b);
    }
    public void addPlanet(Planet planet) {
        planets.add(planet);
    }
    public ArrayList<Star> getConnectedStars() {
        ArrayList<Star> connectedStars = new ArrayList<>();
        for (Star star : galaxy.allStars) {
            if (star.connectedStars.contains(this) || this.connectedStars.contains(star)) {
                connectedStars.add(star);
            }
        }
        return  connectedStars;
    }
    public String getName() {
        String string = potentialNames.get(0);
        potentialNames.remove(0);

        return this.name + "-" + string;

    }
    public void setNameArray() {
        String[] names = {
                "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta"
        };

        potentialNames.addAll(Arrays.asList(names));

    }


}
