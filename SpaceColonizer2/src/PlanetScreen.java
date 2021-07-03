import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;

public class PlanetScreen extends SubScreen {
    Galaxy galaxy;

    //this screen will show detail planet info and allow allocation of planet resources on a turn by turn basis

    //a list on the left with every planet, then a secondary menu scrollable with all options. Left/right will change the allocation
    //display percentage of resources from the bar for each resource
    //to-do: rename planets, add moons,
    Civ player;
    String filterText = "";
    String renameText = "";
    boolean typing = false;
    boolean typingR = false;
    public boolean blinker = false;
    String sortingText = "Random";
    boolean show_colonized = true;

    public int left_slider = 0; //how far into arraylist
    public int right_slider = 0; //how far down secondary list
    public String[] terms = {"INDUSTRY","RESEARCH","DEFENSES","SHIPBUILDING","TERRAFORMING"};

    public ArrayList<Planet> allPlanets = new ArrayList<>();
    public ArrayList<Planet> colonizedPlanets = new ArrayList<>();
    public ArrayList<Planet> notColPlanets = new ArrayList<>();


    public int sortBy = 0;
    public int sortByMax = 3;

    public PlanetScreen(Galaxy world, Civ player) {
        this.galaxy = world;

        this.colonizedPlanets = new ArrayList<>(galaxy.colonizedBy(player));
        this.notColPlanets = new ArrayList<>(galaxy.notColonizedBy(player));
        this.allPlanets = new ArrayList<>(galaxy.allPlanets);

        this.ls_max = colonizedPlanets.size() - 1;
        this.player = player;
        adjustLS();

    }
    public PlanetScreen(Galaxy world, Civ player, String filter) {
        this.galaxy = world;
        show_colonized = false  ;
        this.colonizedPlanets = new ArrayList<>(galaxy.colonizedBy(player));
        this.notColPlanets = new ArrayList<>(galaxy.notColonizedBy(player));
        this.allPlanets = new ArrayList<>(galaxy.allPlanets);

        this.ls_max = colonizedPlanets.size() - 1;
        this.player = player;
        this.filterText = filter;
        adjustLS();

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
            ls_max = colonizedPlanets.size() - 1;
        } else {
            ls_max = notColPlanets.size() - 1;
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
        this.colonizedPlanets = new ArrayList<>(galaxy.colonizedBy(player));
        this.notColPlanets = new ArrayList<>(galaxy.notColonizedBy(player));
        this.allPlanets = new ArrayList<>(galaxy.allPlanets);

        this.colonizedPlanets = new ArrayList<>(galaxy.colonizedBy(player));
        colonizedPlanets = filter(colonizedPlanets, filterText);
        this.notColPlanets = new ArrayList<>(galaxy.notColonizedBy(player));
        notColPlanets = filter(notColPlanets, filterText);
        this.allPlanets = new ArrayList<>(galaxy.allPlanets);
        this.ls_max = colonizedPlanets.size() - 1;

        if (show_colonized) {
            ls_max = colonizedPlanets.size() - 1;
        } else {
            ls_max = notColPlanets.size() - 1;
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
                galaxy.allPlanets.sort(new HabComparator());
                sortingText ="Habitability";
                break;


        }
    }

    public void changePlanetValue(int x) {
        for (Planet planet : colonizedPlanets) {
            if (colonizedPlanets.indexOf(planet) == left_slider) {
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

        terminal.write("COLONIZED: (-SPACEBAR-)", 2, 5, Color.cyan);
        terminal.write("Sorting: " + sortingText + " -V-", 2, 6, Color.white);

        for (Planet planet : colonizedPlanets) {

            if (planet.colonized) {

                index++;
                if (y >= AsciiPanel.SCREEN_HEIGHT) {
                    break;
                }
                int x = 1;
                if (colonizedPlanets.indexOf(planet) == left_slider) {
                    terminal.write("<", x, y, Color.cyan);
                }

                terminal.write(planet.name, x + 1, y++, planet.habColor());
                if (typingR && colonizedPlanets.indexOf(planet) == left_slider) {
                    if (ApplicationMain.count % 10 == 0) {
                        blinker = !blinker;
                    }
                    if (blinker) {
                            int xb = x + 1 + renameText.length();
                            int yb = y - 1;
                            blinker(terminal, xb, yb);
                    }
                    y--;
                    terminal.write(renameText, x + 1, y++, Color.white);
                }


                //renaming
                if (colonizedPlanets.indexOf(planet) == left_slider) {
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
        if (colonizedPlanets.size() > 0) {
            Planet planet = colonizedPlanets.get(left_slider);

            terminal.write(planet.industrySpending + "%", 45, y, Color.cyan);
            terminal.write(Math.round(planet.industrySpendingActual()* 10)/10.0 + "*", 61, y, Color.orange);
            terminal.write(planet.researchSpending + "%", 45, y + 1, Color.cyan);
            terminal.write(Math.round(planet.researchSpendingActual()* 10)/10.0 + "*", 61, y + 1, Color.orange);
            terminal.write(planet.defenseSpending + "%", 45, y + 2, Color.cyan);
            terminal.write(Math.round(planet.defenseSpendingActual() * 10)/10.0 + "*", 61, y + 2, Color.orange);
            terminal.write(planet.shipSpending + "%", 45, y + 3, Color.cyan);
            terminal.write(Math.round(planet.shipSpendingActual()* 10)/10.0 + "*", 61, y + 3, Color.orange);
            terminal.write(planet.terraSpending + "%", 45, y + 4, Color.cyan);
            terminal.write(Math.round(planet.terraSpendingActual()* 10)/10.0 + "*", 61, y + 4, Color.orange);
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
            y += 6;
            terminal.write("PRODUCTION: ", 25, y, Color.orange);
            terminal.write(planet.production + "*", 45, y++, Color.orange);

            terminal.write("POPULATION: ", 25, y, Color.green);
            terminal.write(planet.getPopString(), 45, y++, Color.green);
            terminal.write("   GROWTH: ", 25, y, Color.green);
            terminal.write("+" + Math.round(planet.lastPopGrowth * 100.0) / 100.0 + "m", 45, y++, Color.green);

            terminal.write("HAPPINESS: ", 25, y, Color.cyan);
            terminal.write(planet.happiness + "%", 45, y++, Color.cyan);

            terminal.write("SIZE: ", 25, y, Color.gray);
            terminal.write(planet.size + "", 45, y++, Color.gray);

            String string = switch (right_slider) {
                case (0) -> "Construct factories to increase production.";
                case (1) -> "Research new technologies.";
                case (2) -> "Build planetary defenses.";
                case (3) -> "Build space ships.";
                case (4) -> "Make your planet more habitable.";
                default -> "";
            };
            terminal.write(string,68, 6 + right_slider);
            y++;
            /*
            terminal.write("HABITABILITY: ", 25, y, AsciiPanel.terraforming);
            planet.setHabitability();

            terminal.write(planet.habitability + "%", 45, y++, AsciiPanel.terraforming);


            terminal.write(" WETNESS: ", 25, y);
          //  terminal.write("Is there enough water?", 55, y);
            terminal.write(2 * Math.min(planet.wetness,10) * 5 + "%", 45, y++);

            terminal.write(" TEMPERATURE: ", 25, y);
          //  terminal.write("Is the climate mild?", 55, y);
            terminal.write((int) (20 - (2 * (Math.abs(10.0 - planet.temperature)))) * 5 + "%", 45, y++);

            terminal.write(" ATMOSPHERE: ", 25, y);
          //  terminal.write("Is the atmosphere breathable?", 55, y);
            terminal.write(planet.breathability * 5 + "%", 45, y++);

            terminal.write(" RADIATION: ", 25, y);
          //  terminal.write("Is there a protective magnetic field?", 55, y);
            terminal.write((20 - planet.radiation) * 5 + "%", 45, y++);

            terminal.write(" SOIL: ", 25, y);
           // terminal.write("Is there nutritious and diverse geology?", 55, y);
            terminal.write(planet.soilrichness * 5 + "%", 45, y++);
            */
            int y2 = 18;
            int x2 = 38;
            x = 25;
            terminal.write("Climate: ", x, y2);
            terminal.write(planet.tempString(), x2, y2++, planet.tempColor());
            terminal.write("Atmosphere: ", x, y2);
            terminal.write(planet.airString(), x2, y2++, planet.airColor());

            terminal.write("Liquid Water: ", x, y2);
            terminal.write(planet.iceCapsString(), x2 + planet.waterString().length() + 1, y2 ,planet.iceCapsColor());
            terminal.write(planet.waterString(), x2, y2++, planet.waterColor());



            terminal.write("Radiation: ", x, y2);
            terminal.write(planet.radString(), x2, y2++, planet.radColor());
            terminal.write("Soil: ", x, y2);
            terminal.write(planet.soilString(), x2, y2++, planet.soilColor());
        }



    }
    public void colorFromValue(int x) {

    }
    public void printColonizable(AsciiPanel terminal) {
        int y = 7;
        int index = 0;
        terminal.write("COLONIZABLE: (-SPACEBAR-)", 2, 5, Color.green);
        terminal.write("Sorting: " + sortingText + " -V-", 2, 6, Color.white);


        for (Planet planet : notColPlanets) {

            if (!planet.colonized) {
                if (notColPlanets.indexOf(planet) == left_slider) {
                    int y2 = 12;
                    int x2 = 42;
                    int x = 28;
                    terminal.write("Climate: ", x, y2);
                    terminal.write(planet.tempString(), x2, y2++, planet.tempColor());
                    terminal.write("Atmosphere: ", x, y2);
                    terminal.write(planet.airString(), x2, y2++, planet.airColor());

                    terminal.write("Liquid Water: ", x, y2);
                    terminal.write(planet.iceCapsString(), x2 + planet.waterString().length() + 1, y2 ,planet.iceCapsColor());
                    terminal.write(planet.waterString(), x2, y2++, planet.waterColor());



                    terminal.write("Radiation: ", x, y2);
                    terminal.write(planet.radString(), x2, y2++, planet.radColor());
                    terminal.write("Soil: ", x, y2);
                    terminal.write(planet.soilString(), x2, y2++, planet.soilColor());

                    terminal.write("SIZE: ", x, y2, Color.gray);
                    terminal.write(planet.size + "", x2, y2++, Color.gray);
                }

                index++;
                if (y >= AsciiPanel.SCREEN_HEIGHT) {
                    continue;
                }
                int x = 1;
                if (notColPlanets.indexOf(planet) == left_slider) {
                    terminal.write("<", x, y, Color.cyan);
                }

                terminal.write(planet.name, x + 1, y++, planet.habColor());

                if (notColPlanets.indexOf(planet) == left_slider) {
                    terminal.write(">", x + planet.name.length() + 1, y - 1, Color.cyan);
                }
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


        if (show_colonized) {
            printColonized(terminal);
        } else {
            printColonizable(terminal);
        }

        if (colonizedPlanets.size() > 0 && show_colonized) {
            printColonizedImage(terminal);
        } else if (!show_colonized && notColPlanets.size() > 0){
            printNotColonizedImage(terminal);
        }



    }
    public ArrayList<Planet> filter(ArrayList<Planet> array, String filter) {
        ArrayList<Planet> arrayCopy = new ArrayList<>(array);

        for (int x = 0; x < arrayCopy.size(); x++) {
            Planet planet = arrayCopy.get(x);
            if (!planet.name.toLowerCase().contains(filter.toLowerCase())) {
                arrayCopy.remove(planet);
                x--;
            }
        }

        return new ArrayList<>(arrayCopy);
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
                    break;

                case (KeyEvent.VK_SPACE):
                    filterText = filterText.concat(" ");
                    break;
                default:
                    if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ -+=-';:\"1234567890".contains((key.getKeyChar() + "").toUpperCase())) {
                        filterText = filterText.concat(key.getKeyChar() + "");
                    }
            }
        } else if (typingR) {
            switch (key.getKeyCode()) {
                case (KeyEvent.VK_ESCAPE):
                    renameText = "";
                    typingR = false;
                    break;
                case (KeyEvent.VK_BACK_SPACE):
                    if (renameText.length() > 0) {
                        renameText = renameText.substring(0, renameText.length()-1);
                    } else {
                        typing = false;
                    }
                    break;
                case (KeyEvent.VK_ENTER):
                    if (renameText.length() > 0) {
                        colonizedPlanets.get(left_slider).name = renameText;
                    }
                    renameText = "";
                    typingR = false;
                    break;

                case (KeyEvent.VK_SPACE):
                    renameText = renameText.concat(" ");
                    break;
                default:
                    if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ -+=-';:\"1234567890".contains((key.getKeyChar() + "").toUpperCase())) {
                        renameText = renameText.concat(key.getKeyChar() + "");
                    }
                    break;

            }
        } else
            switch (key.getKeyCode()) {
                case (KeyEvent.VK_UP):
                    right_slider(-1);
                    break;
                case (KeyEvent.VK_S):
                    left_slider(1);
                    break;
                case (KeyEvent.VK_W):
                    left_slider(-1);
                    break;
                case (KeyEvent.VK_DOWN):
                    right_slider(1);
                    break;
                case (KeyEvent.VK_R):
                    if (show_colonized) {
                        typingR = true;
                    }
                    break;
                case (KeyEvent.VK_F):
                    typing = true;
                    break;
                case (KeyEvent.VK_LEFT):
                    changePlanetValue(-10);
                    break;
                case (KeyEvent.VK_RIGHT):
                    changePlanetValue(10);
                    break;
                case (KeyEvent.VK_ESCAPE):
                    return null;
                case (KeyEvent.VK_BACK_SPACE):
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
                case (KeyEvent.VK_ENTER):
                    if (!show_colonized) {
                        for (Planet planet : notColPlanets) {
                            if (notColPlanets.indexOf(planet) == left_slider) {
                                planet.setOwner(player);
                                adjustLS();
                                break;
                            }
                        }
                    }
                    break;
                case (KeyEvent.VK_SPACE):
                    show_colonized = !show_colonized;
                    adjustLS();
                    break;
                case (KeyEvent.VK_V):
                    sortColonies();
                    break;
            }
        return this;
    }


    public void printColonizedImage(AsciiPanel terminal) {
        AsciiImage asciiImage = null;
        asciiImage = colonizedPlanets.get(left_slider).asciiImage;

        int xcorner = 55;
        int ycorner = 12;
        asciiImage.displayThis(terminal, xcorner, ycorner);
    }
    public void printNotColonizedImage(AsciiPanel terminal) {
        AsciiImage asciiImage = null;
        asciiImage = notColPlanets.get(left_slider).asciiImage;

        int xcorner = 28;
        int ycorner = 18;
        asciiImage.displayThis(terminal, xcorner, ycorner);
    }


}
