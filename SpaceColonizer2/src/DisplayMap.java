import java.awt.event.KeyEvent;

public class DisplayMap extends SubMenu {

    public Galaxy galaxy;
    public DisplayMap(Galaxy galaxy) {
        this.galaxy = galaxy;
        this.ID = "dis";
    }


    @Override
    public void displayOutput(AsciiPanel terminal) {
        for (int x2 = 0; x2 < galaxy.width; x2++) {
            for (int y2 = 0; y2 < galaxy.height; y2++) {
                terminal.write(galaxy.tileMap[x2][y2].glyph(), x + x2, y + y2);
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }


}
