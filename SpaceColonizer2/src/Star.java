import java.util.ArrayList;
import java.util.Arrays;

public class Star {
    String name;
    String type;

    int x; int y;

    ArrayList<Planet> planets = new ArrayList<>();

    ArrayList<String> potentialNames = new ArrayList<>();



    public Star(int x, int y, String name) {
        setNameArray();
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
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
