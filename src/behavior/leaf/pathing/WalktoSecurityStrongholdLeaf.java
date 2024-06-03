package behavior.leaf.pathing;

import data.MinotaursConfig;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.frameworks.treebranch.Leaf;
import org.dreambot.api.utilities.Sleep;
import utils.Antiban;

public class WalktoSecurityStrongholdLeaf extends Leaf {

    @Override
    public boolean isValid() {
        int Count = Inventory.count(MinotaursConfig.getMinoConfig().FOOD_SOURCE);
        return Inventory.contains(MinotaursConfig.getMinoConfig().FOOD_SOURCE) &&
                !MinotaursConfig.getMinoConfig().SECCURITY_STRONGHOLD.contains(Players.getLocal().getTile()) &&
                Count >= 25;
    }

    @Override
    public int onLoop() {
        if (Walking.shouldWalk(6)) {
            Sleep.sleep(Antiban.getAntiBan().performAntiban());

            Walking.walk(MinotaursConfig.getMinoConfig().SECCURITY_STRONGHOLD.getRandomTile());

            Sleep.sleepUntil(() -> MinotaursConfig.getMinoConfig().SECCURITY_STRONGHOLD.contains(
                    Players.getLocal().getTile()
            ), 500, 100);

        }

        return 144;
    }
}
