import java.util.ArrayList;

public class Fleet {
    ArrayList<Ship> ships = new ArrayList<>();
    String name = "Fleet_Name";
    int total_power = 0;

    public Fleet() {

    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }
    public void removeShip(Ship ship) {
        ships.remove(ship);
    }
    public int towerPower() {
        total_power = 0;
        for (Ship ship : ships) {
            total_power += ship.power;
        }
        return total_power;
    }
}
