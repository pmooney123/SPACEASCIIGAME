import javax.naming.CompositeName;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;

public class PlayScreen implements Screen {
    public Factory factory;
    public static Random random = new Random();
    private int centerX;
    private int centerY;
    private final int screenWidth;
    private final int screenHeight;
    public ArrayList<String> input = new ArrayList<String>();
    public static boolean blinker = false;
    public ArrayList<String> output = new ArrayList<>();
    private boolean paused = false;
    private int describing = 0;
    public Galaxy galaxy;
    private Screen subscreen;
    public boolean namesShown = false;
    public boolean powerShown = false;
    public boolean linesShown = false;
    public int year = random.nextInt(250) + 2000;

    public int cameraX = 0; public int cameraY = 0;

    public ArrayList<Civ> civs = new ArrayList<Civ>();
    public Civ playerCiv = new Civ(Color.green);
    public Civ enemyCiv = new Civ(Color.orange);
    public ArrayList<SubMenu> subMenus = new ArrayList<>();

    public static boolean HELP = true;

    //debugging thing
    public boolean reset = false;

    public PlayScreen(){
        screenWidth = AsciiPanel.PORT_WIDTH;
        screenHeight = AsciiPanel.PORT_HEIGHT;
        this.galaxy = new Galaxy(screenWidth, screenHeight, playerCiv);
        cameraX = galaxy.width /2;
        cameraY = galaxy.height /2;
        advGame();
        //this.factory = new Factory();
        civs.add(playerCiv);
        civs.add(enemyCiv);
        for (Star star : galaxy.allStars) {

        }

    }

    public void update() {

    }

    public int getScrollX() { return Math.max(0, Math.min(cameraX - screenWidth / 2, galaxy.width - screenWidth));  }

    public int getScrollY() { return Math.max(0, Math.min(cameraY - screenHeight / 2, galaxy.height - screenHeight)); }
    public double distance(int x, int y, int x2, int y2) {
        return Math.sqrt((x2 - x)*(x2 - x) + (y2 - y) * (y2 - y));
    }

    public void displayOutput(AsciiPanel terminal) {
        if (subscreen != null) {
            subscreen.displayOutput(terminal);
        } else {



            for (SubMenu menu : subMenus) {
                menu.displayOutput(terminal);
            } //display seb menus

            int left = getScrollX();
            int top = getScrollY();
            for (int x2 = 0; x2 < screenWidth; x2++) {
                for (int y2 = 0; y2 < screenHeight; y2++) {
                    int wx = x2 + left; //left = space to left of screen
                    int wy = y2 + top; //top = space above screen view
                    if (galaxy.starHere(x2, y2)) {
                        Star star = galaxy.getStar(x2, y2);
                        terminal.write(galaxy.glyph(wx, wy), x2, y2, star.politicalColor(), new Color(0,0,0,0));
                    } else {
                        terminal.write(galaxy.glyph(wx, wy), x2, y2, galaxy.color(wx, wy), Color.black);
                    }
                }
            }//display map
            if (namesShown) {
                for (Star star : galaxy.allStars) {
                    int sx = star.x;
                    int sy = star.y;
                    while (sx + star.name.length() >= galaxy.width) {
                        sx--;
                    }
                    if (sy >= AsciiPanel.PORT_HEIGHT) {
                        sy--;
                    }
                    for (int x2 = 0; x2 < star.name.length(); x2++) {
                        if (galaxy.starHere(sx + x2 + 1, sy + 1)) {
                            if (galaxy.getStar(sx + x2 + 1, sy + 1) != star) {
                                sy--;
                            }
                        }
                    }


                    for (int x2 = 0; x2 < star.name.length(); x2++) {
                        if (!galaxy.starHere(sx + x2 + 1, sy + 1)) {

                            terminal.write(star.name.substring(x2, x2 + 1), sx + x2 + 1, sy + 1, star.politicalColor(), new Color(0, 0, 0, 0));
                        }
                    }


                } //star names

            }

            if (powerShown) {
                for (Star star : galaxy.allStars) {
                    int sx = star.x;
                    int sy = star.y;
                    if (sy - 1 < 0) {
                        sy ++;
                    }
                    for (Note note : star.fleetStats()) {



                        for (int x2 = 0; x2 < note.string.length(); x2++) {
                            if (!galaxy.starHere(sx + x2 + 1, sy - 1)) {

                                terminal.write(note.string.substring(x2, x2 + 1), sx + x2 + 1, sy - 1, note.color, new Color(0, 0, 0, 0));
                            }
                        }
                        sx += note.string.length();

                    }


                } //star names

            }
            terminal.write("Year: " + year, 0, AsciiPanel.PORT_HEIGHT, Color.cyan);
            terminal.write('X', cameraX, cameraY, Color.white);
            if (linesShown) {
            for (Star star : galaxy.allStars) {
                for (Star star2 : star.connectedStars) {

                    terminal.paintLine(star.x * terminal.getCharWidth() + terminal.getCharWidth() / 2,
                            star.y * terminal.getCharHeight() + terminal.getCharHeight() / 2,
                            star2.x * terminal.getCharWidth() + terminal.getCharWidth() / 2,
                            star2.y * terminal.getCharHeight() + terminal.getCharHeight() / 2);

                }
            }
            } //draw lines
            int y = 15;

            int x = 80;
            if (galaxy.starHere(cameraX, cameraY)) {
                Star star = galaxy.getStar(cameraX, cameraY);
                x = AsciiPanel.PORT_WIDTH + 2;
                y = 5;
                terminal.write("STAR: " + star.name, x, y++);
                for (Fleet fleet : star.orbitingFleets) {
                    terminal.write("Fleet: " + fleet.name + " " + fleet.total_power + "*", x, y++);
                }
            }

            terminal.write("Q- Planet Manager", AsciiPanel.PORT_WIDTH + 1, 1);
            terminal.write("T- Tech Manager", AsciiPanel.PORT_WIDTH + 1, 2);

            int xgap1 = 0;
            int xgap2 = 0;
            int xgap3 = 0;


            for (Planet planet: galaxy.colonizedBy(playerCiv)) {
                String string = planet.name + ": ";
                if (string.length() > xgap1) {
                    xgap1 = string.length();
                }

                string = planet.getPopString() + " ";
                if (string.length() > xgap2) {
                    xgap2 = string.length();
                }

                string = planet.production + "* ";
                if (string.length() > xgap3) {
                    xgap3 = string.length();
                }
            }
            for (Planet planet: galaxy.colonizedBy(playerCiv)) {
                x = 80;
                y = 15;
                String string = planet.name + ": ";
                terminal.write( string, x, y, Color.white);
                x += xgap1;
                string = planet.getPopString() + " ";

                terminal.write( string + " ", x, y, Color.green);
                x += xgap2;
                string = planet.production + "* ";

                terminal.write( string + " ", x, y, Color.orange);
                x += xgap3;
                string = planet.happiness + "% ";

                terminal.write( string + " ", x, y++, planet.habColor());
            }

        }
    }
    public void advGame() {
        year += 10;
        for (Planet planet : galaxy.allPlanets) {
            planet.advPopulation();
            planet.setProduction();
        }
    }
    public static void blinker(AsciiPanel terminal, int x, int y) {
        if (ApplicationMain.count % 30 == 0) {
            blinker = !blinker;
        }
        if (blinker) {
            terminal.write("/", x, y, Color.white);

        }
    }
    public void displayTextOutput(AsciiPanel terminal) {
        int y = 2;
        if (output.size() > 0) {
            terminal.writeCenter(output.get(output.size() - 1), y++, Color.white, Color.blue);
            for (int j = output.size() - 2; j >= 0; j--) {
                String string = output.get(j);
                terminal.writeCenter(string, y++);
            }
        }

    }

    public void toggleMenu(SubMenu menu) {
        boolean alreadyHas = false;
        SubMenu menuRemove = null;
        for (SubMenu subMenu : subMenus) {
            if (subMenu.ID.equals(menu.ID)) {
                alreadyHas = true;
                menuRemove = subMenu;
            }
        }
        if (!alreadyHas) {
            subMenus.add(menu);
        } else {
            subMenus.remove(menuRemove);
        }
    }
    public Screen respondToInput() {
        output.clear();
        output.add("");


        input.clear();
        return null;
    }
    public Screen respondToUserInput(KeyEvent key) {

        if (subscreen != null) {
            subscreen = subscreen.respondToUserInput(key);
        } else {
            switch (key.getKeyCode()) {
                case (KeyEvent.VK_ESCAPE):
                    input.clear();
                    return this;
                case (KeyEvent.VK_N) :
                    namesShown = !namesShown;
                    break;
                case (KeyEvent.VK_L) :
                    linesShown = !linesShown;
                    break;
                case (KeyEvent.VK_Q):
                    subscreen = new PlanetScreen(galaxy, playerCiv);
                    return this;
                case (KeyEvent.VK_T):
                    subscreen = new TechnologyScreen(galaxy, playerCiv);
                    return this;
                case (KeyEvent.VK_1):
                    toggleMenu(new DisplayMap(galaxy));
                    return this;
                case (KeyEvent.VK_BACK_SPACE):
                    if (input.size() > 0) {
                        input.remove(input.size() - 1);
                    }
                    return this;
                case (KeyEvent.VK_W) :
                    moveCamera(0, -1);
                    break;
                case (KeyEvent.VK_A) :
                    moveCamera(-1, 0);
                    break;
                case (KeyEvent.VK_S) :
                    moveCamera(0, 1);
                    break;
                case (KeyEvent.VK_D) :
                    moveCamera(1, 0);
                    break;
                case (KeyEvent.VK_V):
                    if (galaxy.starHere(cameraX, cameraY)) {
                        String string = galaxy.getStar(cameraX, cameraY).name;

                        subscreen = new PlanetScreen(galaxy, playerCiv, string);
                    }
                    break;
                case (KeyEvent.VK_F):
                    subscreen = new FleetScreen(galaxy, playerCiv);
                    break;
                case (KeyEvent.VK_P):
                    powerShown = !powerShown;
                    break;
                case (KeyEvent.VK_ENTER) :
                    advGame();
                    break;
                case (KeyEvent.VK_3) :
                    if (galaxy.starHere(cameraX,cameraY)) {
                        System.out.println("here");
                        enemyCiv.newFleet(galaxy.getStar(cameraX,cameraY), 10,  galaxy.allStars.get(random.nextInt(galaxy.allStars.size())));
                    }
                    break;
                case (KeyEvent.VK_2) :
                    if (galaxy.starHere(cameraX,cameraY)) {
                        System.out.println("here");
                        playerCiv.newFleet(galaxy.getStar(cameraX,cameraY), 10, galaxy.allStars.get(random.nextInt(galaxy.allStars.size())));
                    }
                    break;
                default:
                    if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ ".contains((key.getKeyChar() + "").toUpperCase())) {
                        try {
                            char Character = key.getKeyChar();
                            String string = Character + "";
                            string = string.toUpperCase();
                            input.add(string);
                            //  System.out.println("key.toString().toUpperCase() " + key.toString().toUpperCase());
                        } catch (Exception e) {
                            System.out.println("UNKNWON INPUT" + key.getKeyCode());
                        }
                    }
            }
        }
        return this;
    }

    public void moveCamera(int x, int y) {
        if (galaxy.tile(x + cameraX, y + cameraY) != Tile.BOUNDS) {
            //System.out.println("here");
            cameraX += x;
            cameraY += y;
        }
    }
}