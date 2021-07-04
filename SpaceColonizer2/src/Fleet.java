import java.util.ArrayList;

public class Fleet {
    ArrayList<Ship> ships = new ArrayList<>();
    String name = "Fleet_Name";
    int total_power = 0;
    Star currentStar = null;
    Civ ownerCiv;
    boolean automerge = true; //does fleet merge with starfleets in same system?

    int speed = 10; //ly per decade
    int fuel = 100; //how many ly can it go before refueling
    int fuelMax = 100;
    int interProgress = 0;
    int distanceNeeded = 0;

    boolean hasDestination = false;
    int travel_time = 0;
    Star destinationStar;

    public Fleet(Civ ownerCiv, Star currentStar, int power, Star starTarget) {
        this.ownerCiv=  ownerCiv;
        this.currentStar = currentStar;
        total_power = power;

    }
    public void move() {
        interProgress += speed;
        if (currentStar.orbitingFleets.contains(this)) {
            currentStar.removeFleet(this);
        }
        if (interProgress >= distanceNeeded) {
            destinationStar.addFleet(this);
            currentStar = destinationStar;
            hasDestination = false;
            destinationStar = null;
        } else {

        }


    }
    public double distanceTravel(double x, double y, double x2, double y2) {
        return 5 * Math.sqrt((x2 - x)*(x2 - x) + (y2 - y) * (y2 - y));
    }
    public double distance(double x, double y, double x2, double y2) {
        return Math.sqrt((x2 - x)*(x2 - x) + (y2 - y) * (y2 - y));
    }
    public void setDestination(Star star) {
        this.destinationStar = star;
        hasDestination = true;


        this.distanceNeeded = (int) distanceTravel(currentStar.x, currentStar.y, destinationStar.x, destinationStar.y);
        this.interProgress = 0;



    }
    public void moveFleet(Star toStar) {
        currentStar.removeFleet(this);
        toStar.addFleet(this);

        hasDestination = false;
        destinationStar = null;

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
