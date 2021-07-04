import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;

public class FleetScreen extends SubScreen {
    Galaxy galaxy;
    Civ player;

    int left_slider = 0;
    int left_max;

    String filterText = "";
    String renameText = "";

    boolean typing = false;
    boolean typingR = false;
    public boolean blinker = false;
    String sortingText = "Random";
    Screen subscreen = null;

    public FleetScreen(Galaxy world, Civ player) {
        this.galaxy = world;
        this.player = player;

        left_max = player.fleets.size() - 1;

    }
    public FleetScreen(Galaxy world, Civ player, String filter) {
        this.galaxy = world;
        this.player = player;
        this.filterText = filter;

    }
    public int ls_max;
    public int rs_max = 4;


    public void right_slider(int x) {
    }
    public void left_slider(int x) {
        ls_max = player.fleets.size() - 1;
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
    public void displayBlinker(AsciiPanel terminal) {
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
    }
    public void displayOutput(AsciiPanel terminal) {
        if (subscreen != null) {
            subscreen.displayOutput(terminal);
        } else {
            ArrayList<Fleet> fleets = new ArrayList<>(player.fleets);
            terminal.write("-ESC- to close", 1, 2);
            terminal.write("This is the fleet menu.", 1, 3);

            terminal.write("-'F'- FILTER: " + filterText, 1, 4, Color.white);
            displayBlinker(terminal);
            int xf = 5;
            int xp = 25;
            int yf = 6;
            if (fleets.size() > 0) {
                Fleet selectedFleet = fleets.get(left_slider);

                for (Fleet fleet : fleets) {
                    if (fleet.automerge) {
                        terminal.write("M", xf - 3, yf, Color.pink);
                    }

                    if (fleet != selectedFleet) {
                        terminal.write(fleet.name + " " + fleet.total_power + "*", xf, yf, player.color);
                        terminal.write("Location: " + fleet.currentStar.name, xp, yf, fleet.currentStar.politicalColor());
                        if (fleet.hasDestination) {
                            terminal.write("DESTINATION: " + fleet.destinationStar.name, xp + 20, yf, Color.cyan);
                            terminal.write("Progress: " + fleet.interProgress + "/" + fleet.distanceNeeded, xp + 40, yf, Color.pink);

                        }
                        yf++;

                    } else {

                        terminal.write("<", xf - 1, yf, Color.cyan);
                        terminal.write(fleet.name + " " + fleet.total_power + "*", xf, yf, player.color);
                        terminal.write("Location: " + fleet.currentStar.name, xp, yf, fleet.currentStar.politicalColor());
                        terminal.write(">", xf + fleet.name.length() + 2 + (fleet.total_power + "").length(), yf, Color.cyan);

                        if (fleet.hasDestination) {
                            terminal.write("DESTINATION: " + fleet.destinationStar.name, xp + 20, yf, Color.cyan);
                            terminal.write("Progress: " + fleet.interProgress + "/" + fleet.distanceNeeded, xp + 40, yf, Color.pink);

                        }                        yf++;

                        if (typingR) {
                            if (ApplicationMain.count % 10 == 0) {
                                blinker = !blinker;
                            }
                            if (blinker) {
                                int xb = xf + renameText.length();
                                int yb = yf;
                                blinker(terminal, xb, yb - 1);
                            }
                            terminal.write(renameText, xf, yf - 1, Color.white);
                        }
                    }
                }
            }


        }

    }
    public ArrayList<Fleet> filter(ArrayList<Fleet> array, String filter) {
        ArrayList<Fleet> arrayCopy = new ArrayList<>(array);

        for (int x = 0; x < arrayCopy.size(); x++) {

        }

        return new ArrayList<Fleet>(arrayCopy);
    }
    public void blinker(AsciiPanel terminal, int x, int y) {

        if (blinker) {
            terminal.write("/", x, y, Color.white);
        }
    }
    public Screen respondToUserInput(KeyEvent key) {
        if (subscreen != null) {
            subscreen = subscreen.respondToUserInput(key);
        } else {
            if (typing) {
                switch (key.getKeyCode()) {
                    case (KeyEvent.VK_ESCAPE):
                        filterText = "";
                        typing = false;
                        break;
                    case (KeyEvent.VK_BACK_SPACE):
                        if (filterText.length() > 0) {
                            filterText = filterText.substring(0, filterText.length() - 1);
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
                            renameText = renameText.substring(0, renameText.length() - 1);
                        } else {
                            typing = false;
                        }
                        break;
                    case (KeyEvent.VK_ENTER):
                        if (renameText.length() > 0) {
                            player.fleets.get(left_slider).name = renameText;
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
            } else {
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
                        typingR = true;
                        break;
                    case (KeyEvent.VK_F):
                        typing = true;
                        break;
                    case (KeyEvent.VK_LEFT):

                        break;
                    case (KeyEvent.VK_RIGHT):

                        break;
                    case (KeyEvent.VK_ESCAPE):
                        return null;
                    case (KeyEvent.VK_BACK_SPACE):

                        break;
                    case (KeyEvent.VK_ENTER):
                        if (player.fleets.size() > 0) {
                            subscreen = new SelectDestinationScreen(galaxy, player, player.fleets.get(left_slider));
                        }
                        break;
                    case (KeyEvent.VK_M):
                        player.fleets.get(left_slider).automerge = !player.fleets.get(left_slider).automerge;
                        break;
                    case (KeyEvent.VK_V):

                        break;
                }
            }
        }
        return this;
    }




}
