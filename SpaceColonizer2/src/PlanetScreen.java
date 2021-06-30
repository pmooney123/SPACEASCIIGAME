import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.Collections;

public class PlanetScreen extends SubScreen {
    Galaxy galaxy;

    //this screen will show detail planet info and allow allocation of planet resources on a turn by turn basis

    //a list on the left with every planet, then a secondary menu scrollable with all options. Left/right will change the allocation
    //display percentage of resources from the bar for each resource
    //to-do: rename planets, add moons,
    Civ player;

    String sortingText = "Random";
    boolean show_colonized = true;

    public int left_slider = 0; //how far into arraylist
    public int right_slider = 0; //how far down secondary list
    public String[] terms = {"INDUSTRY","RESEARCH","DEFENSES","SHIPBUILDING","TERRAFORMING"};

    public int sortBy = 0;
    public int sortByMax = 3;

    public PlanetScreen(Galaxy world, Civ player) {
        this.galaxy = world;
        this.ls_max = galaxy.colonizedBy(player).size() - 1;
        this.player = player;
    }

    //defense, technology, fleet, industry, comfort,
    public int ls_max;
    public int rs_max = 4;


    public void right_slider(int x) {

        if (x > 0) {
            for (int j = 0; j < x; j++) {
                right_slider++;
                if (right_slider > rs_max) {
                    right_slider = 0;
                }
            }
        } else {
            for (int j = 0; j > x; j--) {
                right_slider--;
                if (right_slider < 0) {
                    right_slider = rs_max;
                }
            }
        }
    }
    public void left_slider(int x) {
        if (show_colonized) {
            ls_max = galaxy.colonizedBy(player).size() - 1;
        } else {
            ls_max = galaxy.notColonizedBy(player).size() - 1;
        }
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
        if (show_colonized) {
            ls_max = galaxy.colonizedBy(player).size() - 1;
        } else {
            ls_max = galaxy.notColonizedBy(player).size() - 1;
        }
        if (left_slider > ls_max) {
            left_slider = ls_max;
        }
        if (left_slider < 0) {
            left_slider = 0;
        }
    }

    public void sortColonies() {
        sortBy++;
        if(sortBy == 3) {
            sortBy = 0;
        }
        switch (sortBy) {
            case (0) :
                sortingText = "Production";
                galaxy.allPlanets.sort(new ProductionComparator());
                break;
            case (1) :
                galaxy.allPlanets.sort(new AlphabeticalComparator());
                sortingText ="Alphabetical";
                break;
            case (2) :

                break;


        }
    }

    public void changePlanetValue(int x) {
        for (Planet planet : galaxy.colonizedBy(player)) {
            if (galaxy.colonizedBy(player).indexOf(planet) == left_slider) {
                switch (right_slider) {
                    case (0) :
                        planet.industrySpending(x);
                        break;
                    case (1) :
                        planet.researchSpending(x);
                        break;
                    case (2) :
                        planet.defenseSpending(x);
                        break;
                    case (3) :
                        planet.shipSpending(x);
                        break;
                    case (4) :
                        planet.terraSpending(x);
                        break;
                }

            }
        }
    }

    public void printColonized(AsciiPanel terminal) {
        int y = 7;
        int index = 0;

        terminal.write("COLONIZED: (-SPACEBAR-)", 2, 5, Color.white);
        terminal.write("Sorting: " + sortingText + " -V-", 2, 6, Color.white);

        for (Planet planet : galaxy.colonizedBy(player)) {

            if (planet.colonized) {

                index++;
                if (y >= AsciiPanel.SCREEN_HEIGHT) {
                    break;
                }
                int x = 1;
                if (galaxy.colonizedBy(player).indexOf(planet) == left_slider) {
                    terminal.write("<", x, y, Color.cyan);
                }

                terminal.write(planet.name, x + 1, y++, planet.color);

                if (galaxy.colonizedBy(player).indexOf(planet) == left_slider) {
                    terminal.write(">", x + planet.name.length() + 1, y - 1, Color.cyan);
                }
            }
        }
        int x = 25;
        y = 6;
        for (String string : terms) {
            if (y - 6 == right_slider) {
                terminal.write("<", x - 1, y, Color.cyan);
            }
            terminal.write(string, x, y++, Color.white);

            if (y - 7 == right_slider) {
                terminal.write(">", x + string.length(), y - 1, Color.cyan);
            }
        }
        y = 6;
        if (galaxy.colonizedBy(player).size() > 0) {
            Planet planet = galaxy.colonizedBy(player).get(left_slider);

            terminal.write(planet.industrySpending + "%", 45, y, Color.cyan);
            terminal.write(Math.round(planet.industrySpendingActual()) + "*", 61, y, Color.orange);
            terminal.write(planet.researchSpending + "%", 45, y + 1, Color.cyan);
            terminal.write(Math.round(planet.researchSpendingActual()) + "*", 61, y + 1, Color.orange);
            terminal.write(planet.defenseSpending + "%", 45, y + 2, Color.cyan);
            terminal.write(Math.round(planet.defenseSpendingActual()) + "*", 61, y + 2, Color.orange);
            terminal.write(planet.shipSpending + "%", 45, y + 3, Color.cyan);
            terminal.write(Math.round(planet.shipSpendingActual()) + "*", 61, y + 3, Color.orange);
            terminal.write(planet.terraSpending + "%", 45, y + 4, Color.cyan);
            terminal.write(Math.round(planet.terraSpendingActual()) + "*", 61, y + 4, Color.orange);
            x = 50;
            for (int z = 0; z < planet.industrySpending / 10.0; z++) {
                terminal.write(" ", x + z, y, Color.cyan, AsciiPanel.industry);
            }
            for (int z = 0; z < planet.researchSpending / 10.0; z++) {
                terminal.write(" ", x + z, y + 1, Color.cyan, AsciiPanel.research);
            }
            for (int z = 0; z < planet.defenseSpending / 10.0; z++) {
                terminal.write(" ", x + z, y + 2, Color.cyan, AsciiPanel.defense);
            }
            for (int z = 0; z < planet.shipSpending / 10.0; z++) {
                terminal.write(" ", x + z, y + 3, Color.cyan, AsciiPanel.shipbuilding);
            }
            for (int z = 0; z < planet.terraSpending / 10.0; z++) {
                terminal.write(" ", x + z, y + 4, Color.cyan, AsciiPanel.terraforming);
            }

            terminal.write("PRODUCTION", 25, y + 6, Color.orange);
            terminal.write(planet.production + "*", 45, y + 6, Color.orange);

            terminal.write("POPULATION", 25, y + 8, Color.green);
            terminal.write(planet.getPopString(), 45, y + 8, Color.green);
            terminal.write("+" + Math.round(planet.lastPopGrowth * 100.0) / 100.0 + "m", 45, y + 9, Color.green);

            terminal.write("HAPPINESS", 25, y + 10, Color.cyan);
            terminal.write(planet.happiness + "%", 45, y + 10, Color.cyan);
            String string = switch (right_slider) {
                case (0) -> "Construct factories to increase production.";
                case (1) -> "Research new technologies.";
                case (2) -> "Build planetary defenses.";
                case (3) -> "Build space ships.";
                case (4) -> "Make your planet more habitable.";
                default -> "";
            };
            terminal.write(string,65, 6 + right_slider);
        }
    }
    public void printColonizable(AsciiPanel terminal) {
        int y = 7;
        int index = 0;
        terminal.write("COLONIZABLE: (-SPACEBAR-)", 2, 5, Color.white);
        terminal.write("Sorting: " + sortingText + " -V-", 2, 6, Color.white);


        for (Planet planet : galaxy.notColonizedBy(player)) {

            if (!planet.colonized) {

                index++;
                if (y >= AsciiPanel.SCREEN_HEIGHT) {
                    break;
                }
                int x = 1;
                if (galaxy.notColonizedBy(player).indexOf(planet) == left_slider) {
                    terminal.write("<", x, y, Color.cyan);
                }

                terminal.write(planet.name, x + 1, y++, planet.color);

                if (galaxy.notColonizedBy(player).indexOf(planet) == left_slider) {
                    terminal.write(">", x + planet.name.length() + 1, y - 1, Color.cyan);
                }
            }
        }
    }
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("-ESC- to close", 1, 2);
        terminal.write("This is the planet menu.", 1 ,3);

        terminal.write("Scroll (W-S)            Scroll (UP - DOWN)", 1, 4);

        if (show_colonized) {
            printColonized(terminal);
        } else {
            printColonizable(terminal);
        }

        if (galaxy.colonizedBy(player).size() > 0 && show_colonized) {
            printColonizedImage(terminal);
        } else if (!show_colonized && galaxy.notColonizedBy(player).size() > 0){
            printNotColonizedImage(terminal);
        }



    }
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case (KeyEvent.VK_UP) :
                right_slider(-1);
                break;
            case (KeyEvent.VK_S) :
                left_slider(1);
                break;
            case (KeyEvent.VK_W) :
                left_slider(-1);
                break;
            case (KeyEvent.VK_DOWN) :
                right_slider(1);
                break;
            case (KeyEvent.VK_LEFT) :
                changePlanetValue(-10);
                break;
            case (KeyEvent.VK_RIGHT) :
                changePlanetValue(10);
                break;
            case (KeyEvent.VK_ESCAPE) :
                return null;
            case (KeyEvent.VK_BACK_SPACE) :
                if (show_colonized) {
                    for (Planet planet : galaxy.colonizedBy(player)) {
                        if (galaxy.colonizedBy(player).indexOf(planet) == left_slider) {
                            planet.abandon();
                            adjustLS();
                            break;
                        }
                    }
                }
                break;
            case (KeyEvent.VK_ENTER) :
                if (!show_colonized) {
                    for (Planet planet : galaxy.notColonizedBy(player)) {
                        if (galaxy.notColonizedBy(player).indexOf(planet) == left_slider) {
                            planet.setOwner(player);
                            adjustLS();
                            break;
                        }
                    }
                }
                break;
            case (KeyEvent.VK_SPACE) :
                show_colonized = !show_colonized;
                adjustLS();
                break;
            case (KeyEvent.VK_V) :
                sortColonies();
                break;
        }
        return this;
    }

    public void printColonizedImage(AsciiPanel terminal) {
        AsciiImage asciiImage = null;
        asciiImage = galaxy.colonizedBy(player).get(left_slider).asciiImage;

        int xcorner = 75;
        int ycorner = 12;
        if (asciiImage != null) {
            for (int x = 0; x < asciiImage.width; x++) {
                for (int y = 0; y < asciiImage.height; y++) {
                    terminal.write(asciiImage.pixels[x][y].glyph(), x + xcorner, y + ycorner, asciiImage.pixels[x][y].color());
                }
            }
        }
    }
    public void printNotColonizedImage(AsciiPanel terminal) {
        AsciiImage asciiImage = null;
        asciiImage = galaxy.notColonizedBy(player).get(left_slider).asciiImage;

        int xcorner = 75;
        int ycorner = 12;
        if (asciiImage != null) {
            for (int x = 0; x < asciiImage.width; x++) {
                for (int y = 0; y < asciiImage.height; y++) {
                    terminal.write(asciiImage.pixels[x][y].glyph(), x + xcorner, y + ycorner, asciiImage.pixels[x][y].color());
                }
            }
        }
    }


}
