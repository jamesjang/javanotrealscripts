package behavior.leaf.pathing;

import data.MinotaursConfig;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.frameworks.treebranch.Leaf;
import org.dreambot.api.utilities.Sleep;
import utils.Antiban;

public class WalkToBankLeaf extends Leaf {

    @Override
    public boolean isValid() {
        return !Inventory.contains(MinotaursConfig.getMinoConfig().FOOD_SOURCE) &&
                !MinotaursConfig.getMinoConfig().BANK_AREA.contains(Players.getLocal().getTile());
    }

    @Override
    public int onLoop() {
        if (Walking.shouldWalk(6)) {
            Sleep.sleep(Antiban.getAntiBan().performAntiban());

            Walking.walk(MinotaursConfig.getMinoConfig().BANK_AREA.getRandomTile());

            Sleep.sleepUntil(() -> MinotaursConfig.getMinoConfig().BANK_AREA.contains(
                    Players.getLocal().getTile()
            ), 500, 100);
        }
        return 144;
    }
}
