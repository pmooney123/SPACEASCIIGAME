import java.awt.*;
import java.util.ArrayList;

public class Civ {
    String name;
    Color color;
    Tech[] technologies = new Tech[5];
    ArrayList<Planet> controlledPlanets = new ArrayList<Planet>();
    ArrayList<Fleet> fleets = new ArrayList<>();
    Galaxy world;

    int tech_prog = 50;

    Tech researching = null;
    public ArrayList<Planet> controlledPlanets() {
        ArrayList<Planet> conPlanets = new ArrayList<>();
        conPlanets = world.colonizedBy(this);
        return conPlanets;
    }
    public void moveFleets() {
        for (Fleet fleet : fleets) {
            if (fleet.hasDestination) {
                fleet.move();
            }
        }
    }
    public void newFleet(Star star, int power, Star target) {
        Fleet fleet = new Fleet(this,star,power,target);

        fleets.add(fleet);
        star.orbitingFleets.add(fleet);
    }
    public void newFleet(Star star, int power, Star target, boolean colony, int pop, Planet targetPlanet) {
        Fleet fleet = new Fleet(this,star,power,target);

        fleets.add(fleet);
        fleet.colonyShip = true;
        fleet.population = pop;
        fleet.setDestination(targetPlanet.star);
        fleet.name = "Colony Ship";
        fleet.destinationPlanet = targetPlanet;
        fleet.automerge = false;

        star.orbitingFleets.add(fleet);

    }

    public Civ(Color color, Galaxy world, String name) {
        this.color = color;
        this.world = world;
        this.name = name;
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
