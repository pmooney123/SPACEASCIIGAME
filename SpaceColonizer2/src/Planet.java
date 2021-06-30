import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Planet  {

    Random random = new Random();
    String name;
    Color color;
    AsciiImage asciiImage = new AsciiImage(22, 21, random.nextInt(4), random.nextInt(4) + 4, random.nextInt(7) - 1);

    int size;
    int resources;

    int wetness = 0;
    int temperature = 0;
    int breathability = 0;
    int radiation = 0;
    int soilrichness = 0;

    String[] hab_terms = {"Moisture","Temperature","Atmosphere","Radiation","SoilRichness"};
    int habitability = 0;
    boolean colonized = false;
    Civ owner = null;

    double population = 1;
    double factories = 0;

    int production = (int) (Math.random() * 100);
    int happiness = 50;
    double lastPopGrowth = 0;

    int defenseSpending = 20;
    int industrySpending = 40;
    int shipSpending = 20;
    int researchSpending = 20;
    int terraSpending = 0;
    public int totalSpending = 0;


    public int CarryingCapacity = 0;
    public int GrowthRatePC = 1;

    public void setPolarity() {

    }

    public void setHappiness() {
        happiness = habitability;
    }
    public void setHabitability() {
        habitability = wetness + breathability + (int) (2 * (Math.abs(10.0 - temperature))) + (20 - radiation) + soilrichness;
    }

    public void setOwner(Civ newOwner) {
        owner = newOwner;
        colonized = true;
    }
    public void abandon() {
        owner = null;
        colonized = false;
    }

    public String getPopString() {
        if (population > 1000) {
            double value = population / 1000;
            return String.valueOf(Math.round(value * 10) / 10.0) + "b";
        } else {

            return String.valueOf(Math.round(population * 10) / 10.0) + "m";
        }
    }
    public double getMaxPop() {
        return 7000 * ((habitability + 100) / 200.0);
    }
    public void setProduction() {
        double P = Math.log(population * 1000000) / Math.log(1.06);
        P -= 220;
        if (P < 10) {
            P = 10;
        }
        production = (int) Math.round(P);

        //log(population, 1.08) - 100

                //log2 N = log10 N / log10 2
    }
    public void advPopulation() {
        double P = population;
        double K  = getMaxPop();
        double R = 0.25 * happiness / 100.0;
        double popgrowth = R * P * (1 - (P / K));
        /*
        double popgrowth = (population / 4.0) * happiness / 100.0;

         */
        population += popgrowth;
        lastPopGrowth = popgrowth;
    }

    public void calculateTotal() {
        totalSpending = defenseSpending + industrySpending + shipSpending + researchSpending + terraSpending;
    }
    public void defenseSpending(int x) {
        defenseSpending += x;
        if (defenseSpending > 100) {
            defenseSpending = 100;
        }
        if (defenseSpending < 0) {
            defenseSpending = 0;
        }
    }
    public double defenseSpendingActual() {
        calculateTotal();
        double x = (defenseSpending + 0.0) / totalSpending;

        return x * production;
    }

    public void researchSpending(int x) {
        researchSpending += x;
        if (researchSpending > 100) {
            researchSpending = 100;
        }
        if (researchSpending < 0) {
            researchSpending = 0;
        }
    }
    public double researchSpendingActual() {
        calculateTotal();
        double x = (researchSpending + 0.0) / totalSpending;

        return x * production;
    }
    public void industrySpending(int x) {
        industrySpending += x;
        if (industrySpending > 100) {
            industrySpending = 100;
        }
        if (industrySpending < 0) {
            industrySpending = 0;
        }
    }
    public double industrySpendingActual() {
        calculateTotal();
        double x = (industrySpending + 0.0) / totalSpending;

        return x * production;
    }

    public void terraSpending(int x) {
        terraSpending += x;
        if (terraSpending > 100) {
            terraSpending = 100;
        }
        if (terraSpending < 0) {
            terraSpending = 0;
        }
    }
    public double terraSpendingActual() {
        calculateTotal();
        double x = (terraSpending + 0.0) / totalSpending;

        return x * production;
    }

    public void shipSpending(int x) {
        shipSpending += x;
        if (shipSpending > 100) {
            shipSpending = 100;
        }
        if (shipSpending < 0) {
            shipSpending = 0;
        }
    }
    public double shipSpendingActual() {
        calculateTotal();
        double x = (shipSpending + 0.0) / totalSpending;

        return x * production;
    }

    int max = 100;

    public Planet(String name) {
        this.name = "Default_planet";
        color = Color.red;
        resources = 10;
        size = 100;
        this.name = name;


    }



}
