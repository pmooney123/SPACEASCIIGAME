import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Planet {

    Random random = new Random();
    String name;
    Color colorDeep;
    Color colorString;
    Color colorShallow;
    AsciiImage asciiImage;

    int size;
    int resources;

    public Planet(String name) {
        this.name = "Default_planet";
        colorString = Color.red;

        resources = 10;
        size = random.nextInt(4) + 4;
        this.name = name;
        this.soilrichness = random.nextInt(11);
        this.wetness = random.nextInt(21);
        this.radiation = random.nextInt(21);
        this.temperature = random.nextInt(21);
        this.breathability = random.nextInt(21);
        this.polarity = Math.max(10 - temperature, 0);

        colorDeep = new Color((20 - wetness), 30, wetness * 5 + 15);
        colorShallow = new Color(random.nextInt(100) + 50, random.nextInt(100) + 50, random.nextInt(100) + 50);

        asciiImage = new AsciiImage(22, 17, random.nextInt(4), size, polarity + 1, colorShallow, colorDeep);

        setHabitability();
    }

    int polarity = 0;


    int wetness = 0;
    int temperature = 0;
    int breathability = 0;
    int radiation = 0;
    int soilrichness = 0;

    String[] hab_terms = {"Moisture", "Temperature", "Atmosphere", "Radiation", "SoilRichness"};
    int habitability = 0;
    boolean colonized = false;
    Civ owner = null;

    double population = 0.5;
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
        habitability = 2 * Math.min(wetness, 10) + breathability + (int) (20 - (2 * (Math.abs(10.0 - temperature)))) + (20 - radiation) + soilrichness;
        setHappiness();
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
        return size * 1000 * habitability / 100.0;
    }

    public void setProduction() {
        //        double P = Math.log(population * 1000000) / Math.log(1.05);
        double Pexp = Math.log10(population * 1000000);
        double P = Math.pow(1.9195, Pexp);
        P -= 20;
        if (P < 10) {
            P = 10;
        }

        production = (int) Math.round(P);

        //log(population, 1.08) - 100

        //log2 N = log10 N / log10 2
    }

    public void advPopulation() {
        double P = population;
        double K = getMaxPop();
        double R = 0.5 * happiness / 100.0;
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


    public Color tempColor() {
        if (temperature >= 0 && temperature <= 5) {
           return new Color(197, 255, 245);
        } else if (temperature > 5 && temperature < 9) {
            return new Color(97, 159, 172, 255);
        } else         if (temperature >= 9 && temperature <= 11) {
            return new Color(117, 231, 109);
        } else         if (temperature > 11 && temperature < 16) {
            return new Color(196, 96, 50);
        } else         if (temperature >= 16 && temperature <= 20) {
            return new Color(255, 72, 0);
        }
        return Color.black;
    }

    public String tempString() {
        if (temperature >= 0 && temperature <= 5) {
            return "Very Cold";
        } else if (temperature > 5 && temperature < 9) {
            return "Cold";
        } else         if (temperature >= 9 && temperature <= 11) {
            return "Mild";
        } else         if (temperature > 11 && temperature < 16) {
            return "Hot";
        } else         if (temperature >= 16 && temperature <= 20) {
            return "Very Hot";
        }
        return "Error";
    }
    public Color airColor() {
        if (breathability >= 0 && breathability <= 5) {
            return new Color(255, 0, 0);
        } else if (breathability > 5 && breathability <= 10) {
            return new Color(255, 199, 0, 255);
        } else         if (breathability > 10 && breathability <= 15) {
            return new Color(201, 255, 0);
        } else         if (breathability > 15 && breathability < 20) {
            return new Color(80, 208, 187);
        } else          if (breathability >= 18 && breathability <= 20) {
            return new Color(0, 255, 226);
        }
        return Color.black;
    }
    public String airString() {
        if (breathability >= 0 && breathability <= 5) {
            return "Extremely Toxic";
        } else if (breathability > 5 && breathability <= 10) {
            return "Very Toxic";
        } else         if (breathability > 10 && breathability <= 15) {
            return "Toxic";
        } else         if (breathability > 15 && breathability < 18) {
            return "Slightly Toxic";
        } else         if (breathability >= 18 && breathability <= 20) {
            return "Breathable";
        }
        return "Error";
    }
    public Color soilColor() {
        if (soilrichness >= 0 && soilrichness <= 5) {
            return new Color(255, 30, 0);
        } else if (soilrichness > 5 && soilrichness < 9) {
            return new Color(255, 185, 0, 255);
        } else         if (soilrichness >= 9 && soilrichness <= 11) {
            return new Color(146, 153, 6);
        } else         if (soilrichness > 11 && soilrichness < 16) {
            return new Color(54, 88, 1);
        } else         if (soilrichness >= 16 && soilrichness <= 20) {
            return new Color(20, 226, 0);
        }
        return Color.black;
    }
    public String soilString() {
        int value = soilrichness;
        if (value >= 0 && value <= 5) {
            return "Toxic Soil";
        } else if (value > 5 && value <= 10) {
            return "Unhealthy Soil";
        } else         if (value > 10 && value <= 15) {
            return "Poor Soil";
        } else         if (value > 15 && value < 18) {
            return "Healthy Soil";
        } else         if (value >= 18 && value <= 20) {
            return "Enriched Soil";
        }
        return  "Error";
    }
    public Color radColor() {
        int value = radiation;
        if (value >= 0 && value <= 5) {
            return new Color(0, 255, 235);
        } else if (value > 5 && value <= 10) {
            return new Color(73, 128, 80);
        } else         if (value > 10 && value <= 15) {
            return new Color(255, 167, 0);
        } else         if (value > 15 && value < 18) {
            return new Color(255, 143, 0);
        } else if (value >= 18 && value <= 20) {
            return new Color(255, 72, 0);
        }
        return Color.black;
    }
    public String radString() {
        int value = radiation;
        if (value >= 0 && value <= 5) {
            return "Safe";
        } else if (value > 5 && value <= 10) {
            return "Slightly Radioactive";
        } else         if (value > 10 && value <= 15) {
            return "Radioactive";
        } else         if (value > 15 && value < 18) {
            return "Very Radioactive";
        } else         if (value >= 18 && value <= 20) {
            return "Extremely Radioactive";
        }
        return  "Error";
    }
    public Color waterColor() {
        int value = wetness;
        if (value >= 0 && value <= 5) {
            return new Color(255, 200, 0);
        } else if (value > 5 && value <= 10) {
            return new Color(255, 140, 0);
        } else         if (value > 10 && value <= 15) {
            return new Color(15, 118, 80, 107);
        } else         if (value > 15 && value < 18) {
            return new Color(3, 106, 154);
        } else         if (value >= 18 && value <= 20) {
            return new Color(0, 55, 255);
        }
        return Color.black;
    }
    public String waterString() {
        int value = wetness;
        if (value >= 0 && value <= 5) {
            return "None";
        } else if (value > 5 && value <= 10) {
            return "Some Moisture";
        } else         if (value > 10 && value <= 15) {
            return "Lakes";
        } else         if (value > 15 && value < 18) {
            return "Oceans";
        } else         if (value >= 18 && value <= 20) {
            return "Superoceanic";
        }
        return  "Error";
    }

    public Color iceCapsColor() {
        int value = polarity;
        if (value >= 1 && value < 4) {
            return Color.white;
        } else if (value >= 4 && value <= 100) {
            return Color.white;
        }
        return  Color.black;
    }
    public String iceCapsString() {
        int value = polarity;
        if (value >= 1 && value < 4) {
            return "Ice";
        } else if (value >= 4 && value <= 100) {
            return "Ice";
        }
        return  "Error";
    }

    public Color habColor() {
        int value = habitability;
        if (value >= 0 && value < 20) {
            return new Color(90, 0, 0);
        } else if (value >= 20 && value < 40) {
            return new Color(255, 29, 0, 255);
        } else         if (value >= 40 && value < 60) {
            return new Color(255, 132, 0);
        } else         if (value >= 60 && value < 80) {
            return new Color(233, 255, 0);
        } else         if (value >= 80 && value <= 1000) {
            return new Color(24, 255, 0);
        }
        return Color.black;
    }



}
