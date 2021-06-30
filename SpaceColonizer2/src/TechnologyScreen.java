import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;

public class TechnologyScreen extends  SubScreen {
    Galaxy galaxy;
    //
    Civ civ;

    public int left_slider = 0; //how far into arraylist
    public int right_slider = 0; //how far down secondary list
    public int columns = 0;


    public TechnologyScreen(Galaxy world, Civ playerCiv) {
        this.galaxy = world;
        this.ls_max = playerCiv.techNumUnlocked() - 1;
        this.rs_max = playerCiv.techNumLocked() - 1;
        this.civ = playerCiv;
    }


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
    public void columns_slider(int x) {

        if (x > 0) {
            for (int j = 0; j < x; j++) {
                columns++;
                if (columns > 1) {
                    columns = 0;
                }
            }
        } else {
            for (int j = 0; j > x; j--) {
                columns--;
                if (columns < 0) {
                    columns = 1;
                }
            }
        }
    }

    public void displayOutput(AsciiPanel terminal) {
        terminal.write("-ESC- to close", 1, 2);
        terminal.write("This is the technology menu.", 1, 3);
        Tech techDesc = null;
        int x1 = 1;
        int x2 = 21;
        int x3 = 41;

        int y = 5;
        terminal.write("RS: " + right_slider + " LS: " + left_slider, 20, 30);
        terminal.write("RESEARCHED: ", x1, y++, Color.white);
        int index = 0;

        for (Tech tech : civ.technologies) {
            if (tech.unlocked) {
                if (index == left_slider && columns == 0) {
                    terminal.write("<", x1 - 1, y, Color.cyan);
                    techDesc = tech;
                }

                terminal.write(tech.name, x1, y++, tech.color);

                if (index == left_slider && columns == 0) {
                    terminal.write(">", x1 + tech.name.length(), y - 1, Color.cyan);
                }
                index++;
            }
        }

        y = 5;
        terminal.write("AVAILABLE: ", x2, y++, Color.white);
        index = 0;
        for (Tech tech : civ.technologies) {
            if (!tech.unlocked){

                if (index == right_slider && columns == 1) {
                    terminal.write("<", x2 - 1, y, Color.cyan);
                    techDesc = tech;
                }

                if (tech == civ.researching) {
                    terminal.write(tech.name, x2, y++, tech.color, AsciiPanel.white);
                } else {
                    terminal.write(tech.name, x2, y++, tech.color);
                }

                if (index == right_slider && columns == 1) {
                    terminal.write(">", x2 + tech.name.length(), y - 1, Color.cyan);
                }
                index++;
            }
        }

        terminal.write("DESCRIPTION:", 50, 4);
        if (techDesc != null) {

            String desc = techDesc.desc;
            int x4 = 50;
            int y4 = 5;
            for (int j = 0; j < desc.length() - 1; j++) {

                terminal.write(desc.substring(j, j + 1), x4, y4, Color.white);
                x4++;
                if (x4 > 70 && desc.startsWith(" ", j)) {
                    y4++;
                    x4 = 50;
                }
            }
            terminal.write("COST: " + techDesc.cost, 50, y4 + 1, AsciiPanel.research);

        }

        if (civ.researching != null) {
            terminal.write("RESEARCHING: ", x1, 15, Color.white);
            terminal.write(civ.researching.name, x1 + 13, 15, civ.researching.color);
            terminal.write("Progress: " + civ.tech_prog + "/" + civ.researching.cost, x1, 16);
            for (int k = 0; k < 10; k++) {
                if ((k / 10.0) < (civ.tech_prog / (civ.researching.cost + 0.0))) {
                    terminal.write(" ", x1 + k, 17, Color.cyan, Color.cyan);
                } else {
                    terminal.write(" ", x1 + k, 17, Color.white, Color.white);
                }
            }
        }

    }
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case (KeyEvent.VK_W) :
            case (KeyEvent.VK_UP) :
                if (columns == 1) {
                    right_slider(-1);
                } else {
                    left_slider(-1);
                }
                break;
            case (KeyEvent.VK_S) :
            case (KeyEvent.VK_DOWN) :
                if (columns == 1) {
                    right_slider(1);
                } else {
                    left_slider(1);
                }
                break;
            case (KeyEvent.VK_A) :
            case (KeyEvent.VK_LEFT) :
                columns_slider(-1);
                break;
            case (KeyEvent.VK_D) :
            case (KeyEvent.VK_RIGHT) :
                columns_slider(1);
                break;
            case (KeyEvent.VK_ESCAPE) :
                return null;
            case (KeyEvent.VK_ENTER) :
                Tech tech;
                int index = 0;
                for (Tech tech2 : civ.technologies) {
                    if (!tech2.unlocked){

                        if (index == right_slider && columns == 1) {
                            tech = tech2;
                            civ.setResearch(tech);
                            break;
                        }

                        index++;
                    }
                }

                break;
        }
        return this;
    }


}
