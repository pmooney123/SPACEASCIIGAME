import java.awt.*;

public class Tech {
    String name;
    String desc;
    String desc_adv;
    boolean unlocked = false;
    int cost;
    Color color;
    public Tech() {

    }


    public void automation() {
        this.name = "Automation";
        this.desc = "Factories require less manpower to operate.";
        this.cost = 100;
        this.color = AsciiPanel.industry;
    }
    public void quantumcomputation() {
        this.name = "Quantum Computers";
        this.desc = "Hyper-efficient computers increase research power.";
        this.cost = 120;
        this.color = AsciiPanel.research;

    }
    public void laserplatform() {
        this.name = "Laser Platforms";
        this.desc = "Upgraded weaponry increases planetary defense.";
        this.cost = 110;
        this.color = AsciiPanel.defense;
    }
    public void carbonalloy() {
        this.name = "Carbon Alloy";
        this.desc = "Light-weight, hyper-durable material.";
        this.cost = 130;
        this.color = AsciiPanel.shipbuilding;
    }
    public void nanobots() {
        this.name = "Nano Bots";
        this.desc = "Self-replicating nanobots carry out terraforming.";
        this.cost = 150;
        this.color = AsciiPanel.terraforming;
    }
}
