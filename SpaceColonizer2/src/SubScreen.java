import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class SubScreen implements Screen {


    public SubScreen(){

    }

    public void displayOutput(AsciiPanel terminal) {

    }
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }
}