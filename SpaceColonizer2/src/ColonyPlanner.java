import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ColonyPlanner extends SubScreen {
    Galaxy galaxy;

    Civ player;
    String filterText = "";

    boolean typing = false;

    public boolean blinker = false;

    public Fleet fleet = null;

    public int left_slider = 0; //how far into arraylist

    public ArrayList<Planet> allPlanets = new ArrayList<>();
    public void filterSort(ArrayList<Star> array) {
        array.sort(new DistanceComparator<Star>(fleet ));
        array.sort(new StarFilterComparator(filterText));

    }
    Planet buildPlanet = null;
    Planet targetPlanet = null;

    public ColonyPlanner(Civ player, Planet targetPlanet, Planet buildPlanet, Galaxy world) {
        this.galaxy = world;
        this.targetPlanet = targetPlanet;
        this.player = player;

        ArrayList<Planet> connected = new ArrayList<>(player.controlledPlanets());
        allPlanets = connected;
        System.out.println("con size " + connected.size());
        allPlanets.sort(new ProductionComparator());
        this.ls_max = allPlanets.size() - 1;

        adjustLS();
        this.fleet = fleet;
    }

    //defense, technology, fleet, industry, comfort,
    public int ls_max;

    public void left_slider(int x) {

        ls_max = allPlanets.size() - 1;

        if (x > 0) {
            for (int j = 0; j < x; j++) {
                left_slider++;
                if (left_slider > ls_max) {
                    left_slider = 0;
                }
            }
        } else {
            for (int j = 0; j > x; j--) {
                left_slider--;
                if (left_slider < 0) {
                    left_slider = ls_max;
                }
            }
        }
    }
    public void adjustLS() {
        if (left_slider > ls_max) {
            left_slider = ls_max;
        }
        if (left_slider < 0) {
            left_slider = 0;
        }
    }
    public void printColonizable(AsciiPanel terminal) {
        int y = 7;
        int index = 0;
        terminal.write("Select Planet to Emigrate From (-3 pop): ", 2, 5, Color.green);


        for (Planet planet : allPlanets) {

            index++;
            if (y >= AsciiPanel.SCREEN_HEIGHT) {
                break;
            }
            int x = 1;

            if (allPlanets.indexOf(planet) == left_slider) {
                terminal.write("<", x, y, Color.cyan);
            }

            terminal.write(planet.name, x + 1, y++, planet.habColor());
            //System.out.println("distance" + distance);


            if (allPlanets.indexOf(planet) == left_slider) {
                terminal.write(">", x + planet.name.length() + 1, y - 1, Color.cyan);
            }
        }
    }
    public void displayOutput(AsciiPanel terminal) {

        adjustLS();

        terminal.write("-ESC- to close", 1, 2);
        terminal.write("This is the planet menu.", 1 ,3);

        terminal.write("-'F'- FILTER: " + filterText, 1, 4, Color.white);
        if (typing) {
            if (ApplicationMain.count % 10 == 0) {
                blinker = !blinker;
            }
            if (blinker) {
                int xb = 1 + ("-'F'- FILTER: " + filterText).length();
                int yb = 4;
                blinker(terminal, xb, yb);
            }
        } //filtering

        printColonizable(terminal);


    }
    public void blinker(AsciiPanel terminal, int x, int y) {

        if (blinker) {
            terminal.write("/", x, y, Color.white);
        }
    }
    public Screen respondToUserInput(KeyEvent key) {
        if (typing) {
            switch (key.getKeyCode()) {
                case (KeyEvent.VK_ESCAPE):
                    filterText = "";
                    typing = false;
                    break;
                case (KeyEvent.VK_BACK_SPACE):
                    if (filterText.length() > 0) {
                        filterText = filterText.substring(0, filterText.length()-1);
                    } else {
                        typing = false;
                    }
                    break;
                case (KeyEvent.VK_ENTER):
                    typing = false;
                    System.out.println("here");

                    break;

                case (KeyEvent.VK_SPACE):
                    filterText = filterText.concat(" ");
                    break;
                default:
                    if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ -+=-';:\"1234567890".contains((key.getKeyChar() + "").toUpperCase())) {
                        filterText = filterText.concat(key.getKeyChar() + "");
                    }
            }
        }
        else {
            switch (key.getKeyCode()) {
                case (KeyEvent.VK_S):
                    left_slider(1);
                    break;
                case (KeyEvent.VK_W):
                    left_slider(-1);
                    break;
                case (KeyEvent.VK_F):
                    typing = true;
                    break;
                case (KeyEvent.VK_ESCAPE):
                    return null;
                case (KeyEvent.VK_ENTER):
                    buildPlanet = allPlanets.get(left_slider);
                    if (buildPlanet.population > 3) {
                        player.newFleet(buildPlanet.star, 0, targetPlanet.star, true, 3, targetPlanet);
                        buildPlanet.population -= 3;
                    }
                    return null;
            }
        }
        return this;
    }

    public void printNotColonizedImage(AsciiPanel terminal) {
        AsciiImage asciiImage = null;
        //asciiImage = allPlanets.get(left_slider).asciiImage;

        int xcorner = 28;
        int ycorner = 18;
        //asciiImage.displayThis(terminal, xcorner, ycorner);
    }


}
