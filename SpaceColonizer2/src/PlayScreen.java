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

    public int year = random.nextInt(250) + 2000;

    public int cameraX = 0; public int cameraY = 0;

    public Civ playerCiv = new Civ();
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

    }

    public void update() {

    }

    public int getScrollX() { return Math.max(0, Math.min(cameraX - screenWidth / 2, galaxy.width - screenWidth));  }

    public int getScrollY() { return Math.max(0, Math.min(cameraY - screenHeight / 2, galaxy.height - screenHeight)); }

    public void displayOutput(AsciiPanel terminal) {
        if (subscreen != null) {
            subscreen.displayOutput(terminal);
        } else {
            if (random.nextInt(10) == 0) {

            }
            terminal.write("Enter text below: ", 1, 1);
            int x = 1;
            int y = 2;
            for (String part : input) {
                terminal.write(part, x, y);
                x += part.length();
                if (x > screenWidth) {
                    x = 1;
                    y++;
                }
            }

            blinker(terminal, x, y);
            terminal.writeCenter("Output text here:", 1);
            displayTextOutput(terminal);

            for (SubMenu menu : subMenus) {
                menu.displayOutput(terminal);
            }

            int left = getScrollX();
            int top = getScrollY();
            for (int x2 = 0; x2 < screenWidth; x2++) {
                for (int y2 = 0; y2 < screenHeight; y2++) {
                    int wx = x2 + left; //left = space to left of screen
                    int wy = y2 + top; //top = space above screen view
                    terminal.write(galaxy.glyph(wx, wy), x2, y2, galaxy.color(wx, wy));

                }
            }
            terminal.write("Year: " + year, 0, AsciiPanel.PORT_HEIGHT, Color.cyan);

            terminal.write('X', cameraX, cameraY, Color.white);



            //info panes

            if (galaxy.starHere(cameraX, cameraY)) {
                Star star = galaxy.getStar(cameraX, cameraY);
                terminal.write("STAR: " + star.name, AsciiPanel.PORT_WIDTH + 2, 5);


            }

            terminal.write("Q- Planet Manager", AsciiPanel.PORT_WIDTH + 1, 1);
            terminal.write("T- Tech Manager", AsciiPanel.PORT_WIDTH + 1, 2);



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
                case (KeyEvent.VK_ENTER) :
                    advGame();
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