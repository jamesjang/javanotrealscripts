package utils;

import data.MinotaursConfig;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.skills.Skill;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Entity;

import java.awt.*;

public class Antiban {

    private static final Antiban antiBan = new Antiban();
    public static Antiban getAntiBan() {
        return antiBan;
    }


    //no action
    private int MIN_WAIT_VALUE = 100;
    private int MAX_WAIT_VALUE = 300;

    //between eventsw
    private int MIN_WAIT_BETWEEN_EVENTS = 4000; // In seconds

    private long LAST_ANTIBAN_EVENT = 0L;
    private long START_TIME = 0L;

    private final Point STATS_WIDGET = new Point(577, 186); // Stats menu
    private Skill skillCheck;

    public final Point[] STAT_WIDGET = {
            new Point(550, 210), // Attack
            new Point(550, 270), // Defence
            new Point(550, 240), // Strength
            new Point(612, 210), // Hits
            new Point(550, 304), // Ranged
            new Point(550, 336), // Prayer
            new Point(350, 370), // Magic
            new Point(367, 304), // Cooking
            new Point(676, 368), // Woodcut
            new Point(613, 369), // Fletching
            new Point(677, 273), // Fishing
            new Point(676, 336), // Firemaking
            new Point(614, 337), // Crafting
            new Point(677, 240), // Smithing
            new Point(677, 209), // Mining
            new Point(613, 271), // Herblore
            new Point(614, 240), // Agility
            new Point(614, 304), // Thieving
            new Point(614, 401), // Slayer
            new Point(676, 400), // Farming
            new Point(550, 400), // Runecrafting
            new Point(613, 432), // Hunter
            new Point(550, 432), // Construction
    };

    public Antiban() {
        START_TIME = System.currentTimeMillis();
    }

    public int performAntiban() {
        if (System.currentTimeMillis() - LAST_ANTIBAN_EVENT <= randomWait(MIN_WAIT_BETWEEN_EVENTS, MIN_WAIT_BETWEEN_EVENTS * 2))
            return randomWait(50,100);

        int rp = randomWait(0, 100);

        int Event = randomWait(1, 4);
        switch (Event) {
            case 1:
                if (rp < 30) {
                    if (openStats()) {
                        if (checkStat()) {
                            LAST_ANTIBAN_EVENT = System.currentTimeMillis();
                            Logger.log("performed anti ban check stat");
                            return randomWait(2000, 3500);
                        }
                    }
                }
            case 2:
            {
                if (rp < 30) {
                    Area a = new Area(Players.getLocal().getX() - 10, Players.getLocal().getY() - 10, Players.getLocal().getX() + 10, Players.getLocal().getY() + 10);
                    Camera.rotateToTile(a.getRandomTile());
                    Logger.log("performed anti ban camera move");
                    LAST_ANTIBAN_EVENT = System.currentTimeMillis();

                    return randomWait(50, 100);
                }
            }
            case 3:
            {
                if (rp < 20) {
                    int r = randomWait(1, 3);

                    Entity e = GameObjects.closest(o -> o != null && !o.getName().equals("null") && randomWait(1, 2) != 1);

                    if (e == null || r == 2) {
                        e = NPCs.closest(n -> n != null && !n.getName().equals("null"));

                        if (e == null || r == 3) {
                            e = GroundItems.closest(i -> i != null && !i.getName().equals("null"));
                            if (e == null) {
                                return randomWait(150, 200);
                            }

                        }
                    }

                    Mouse.move(e);
                    Sleep.sleep(randomWait(0, 50));

                    Mouse.click(true);

                    Logger.log("performed anti ban click random");
                    LAST_ANTIBAN_EVENT = System.currentTimeMillis();
                    return randomWait(500, 3000);
                }
            }
            case 4:
                if (!Inventory.isOpen()){
                    Inventory.open();

                    LAST_ANTIBAN_EVENT = System.currentTimeMillis();

                    return randomWait(1000, 2000);
                }
        }
        return randomWait(50, 100);
    }

    // Returns a random number with the human lag element added to the minimum wait time
    public int randomWait(int x, int y) {
        return Calculations.random(x, y+1);
    }

    // This method opens the stats menu
    public boolean openStats() {
        if (Tabs.getOpen() != Tab.SKILLS) {

            int x = (int) STATS_WIDGET.getX() + randomWait(0, 10);
            int y = (int) STATS_WIDGET.getY() + randomWait(0, 10);
            Mouse.move(new Point(x, y));
            Sleep.sleep(0, 50);
            Mouse.click();

            Sleep.sleep(50, 250);
        }

        return Tabs.getOpen() == Tab.SKILLS;
    }

    public boolean checkStat() {
        Point p = STAT_WIDGET[2];

        p.setLocation(p.getX(), p.getY());

        Mouse.move(p);

        return true;
    }




}
