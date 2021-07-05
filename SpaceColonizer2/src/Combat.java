import java.awt.*;
import java.util.ArrayList;

public class Combat {
    ArrayList<Fleet> attackers = new ArrayList<>();
    ArrayList<Fleet> defenders = new ArrayList<>();

    public Combat() {


    }
    public void addSide1(Fleet... side1) {
        for (int x = 0; x < side1.length; x++) {
            attackers.add(side1[x]);
        }
    }
    public CombatResults resolve() {
        CombatResults results = new CombatResults();


        return results;
    }


}
