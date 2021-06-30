import java.awt.event.KeyEvent;
import java.security.Key;

public class HelpScreen implements Screen {


    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("THIS IS THE HELP SCREEN",1,1);
        terminal.write("-ESC- TO LEAVE",1,2);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        switch (key.getKeyCode()) {
            case (KeyEvent.VK_ESCAPE):
                return null;
        }
        return this;
    }

}
