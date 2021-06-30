import java.awt.*;
import java.util.ArrayList;

public class Civ {
    String name;
    Color color;
    Tech[] technologies = new Tech[5];
    ArrayList<Planet> controlledPlanets = new ArrayList<Planet>();
    int tech_prog = 50;

    Tech researching = null;

    public Civ() {
        loadTechs();
    }
    public int techNumLocked() {
        int x = 0;
        for (Tech tech : technologies) {
            if (!tech.unlocked) {
                x++;
            }
        }
        return x;
    }
    public int techNumUnlocked() {
        int x = 0;
        for (Tech tech : technologies) {
            if (tech.unlocked) {
                x++;
            }
        }
        return x;
    }
    public void loadTechs() {
        technologies[0] = new Tech();
        technologies[0].automation();

        technologies[1] = new Tech();
        technologies[1].quantumcomputation();

        technologies[2] = new Tech();
        technologies[2].laserplatform();

        technologies[3] = new Tech();
        technologies[3].carbonalloy();

        technologies[4] = new Tech();
        technologies[4].nanobots();

        technologies[4].unlocked = true;
        technologies[3].unlocked = true;

    }
    public void setResearch(Tech tech) {
        researching = tech;
    }

}
