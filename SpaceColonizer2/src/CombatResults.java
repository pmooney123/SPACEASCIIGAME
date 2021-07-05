import java.awt.*;
import java.util.ArrayList;

public class CombatResults {
    boolean attackersWin = false;
    ArrayList<Note> events = new ArrayList<Note>();


    public CombatResults() {

    }

    public void add(String string, Color color) {
        events.add(new Note(string, color));
    }


}
