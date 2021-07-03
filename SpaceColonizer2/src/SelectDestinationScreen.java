import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;

public class SelectDestinationScreen extends SubScreen {
    Galaxy galaxy;

    //this screen will show detail planet info and allow allocation of planet resources on a turn by turn basis

    //a list on the left with every planet, then a secondary menu scrollable with all options. Left/right will change the allocation
    //display percentage of resources from the bar for each resource
    //to-do: rename planets, add moons,
    Civ player;
    String filterText = "";

    boolean typing = false;

    public boolean blinker = false;

    public Fleet fleet = null;

    public int left_slider = 0; //how far into arraylist

    public ArrayList<Planet> allPlanets = new ArrayList<>();
    public void filterSort(ArrayList<Planet> array) {

        array.sort(new FilterComparator(filterText));

    }
    public SelectDestinationScreen(Galaxy world, Civ player, Fleet fleet) {
        this.galaxy = world;
        this.allPlanets = new ArrayList<>(galaxy.allPlanets);
        this.ls_max = allPlanets.size() - 1;
        this.player = player;
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
        terminal.write("Select Destination: ", 2, 5, Color.green);


        for (Planet planet : allPlanets) {

            index++;
            if (y >= AsciiPanel.SCREEN_HEIGHT) {
                continue;
            }
            int x = 1;

            if (allPlanets.indexOf(planet) == left_slider) {
                terminal.write("<", x, y, Color.cyan);
            }

            terminal.write(planet.name, x + 1, y++, planet.habColor());

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
            filterSort(allPlanets);
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
                    fleet.setDestination(allPlanets.get(left_slider).star);
                    return null;
            }
        }
        return this;
    }

    public void printNotColonizedImage(AsciiPanel terminal) {
        AsciiImage asciiImage = null;
        asciiImage = allPlanets.get(left_slider).asciiImage;

        int xcorner = 28;
        int ycorner = 18;
        asciiImage.displayThis(terminal, xcorner, ycorner);
    }


}
