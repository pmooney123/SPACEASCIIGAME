import java.awt.*;
import java.awt.event.KeyEvent;

public class StartScreen implements Screen {

    public StartScreen() {

    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Welcome to Space Game", 1, 1, Color.white);
        terminal.write("Press -enter- to start", (AsciiPanel.SCREEN_WIDTH / 2) - 8, AsciiPanel.PORT_HEIGHT - 10);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case (KeyEvent.VK_ENTER):
                return new PlayScreen();
        }
        return this;
    }


}
