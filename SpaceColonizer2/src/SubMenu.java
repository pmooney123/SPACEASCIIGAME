import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class SubMenu implements Screen {
    public String ID = "Displayer";
    public int x;
    public int y;

    public SubMenu(){

    }

    public void displayOutput(AsciiPanel terminal) {

    }
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }
}