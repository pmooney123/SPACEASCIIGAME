import java.util.ArrayList;

public class Fleet {
    ArrayList<Ship> ships = new ArrayList<>();
    String name = "Fleet_Name";
    int total_power = 0;
    Star currentStar = null;
    Civ ownerCiv;

    boolean hasDestination = false;
    int travel_time = 0;
    Star destinationStar;

    public Fleet(Civ ownerCiv, Star currentStar, int power, Star starTarget) {
        this.ownerCiv=  ownerCiv;
        this.currentStar = currentStar;
        total_power = power;
        setDestination(starTarget);
    }
    public void setDestination(Star star) {
        this.destinationStar = star;
        hasDestination = true;
    }
    public void moveFleet(Star toStar) {
        toStar.orbitingFleets.add(this);
        currentStar.removeFleet(this);
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
