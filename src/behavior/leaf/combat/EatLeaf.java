package behavior.leaf.combat;

import data.MinotaursConfig;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.frameworks.treebranch.Leaf;
import org.dreambot.api.utilities.Sleep;

public class EatLeaf extends Leaf {


    @Override
    public boolean isValid() {
      return Inventory.contains(MinotaursConfig.getMinoConfig().FOOD_SOURCE) &&
              Players.getLocal().getHealthPercent() <= GetEatPercentage();

    }

    @Override
    public int onLoop() {
        int initialEmptySlots = Inventory.getEmptySlots();

        if (Inventory.interact(MinotaursConfig.getMinoConfig().FOOD_SOURCE, "Eat")) {
            Sleep.sleepUntil(() -> Inventory.getEmptySlots() > initialEmptySlots ||
                    Players.getLocal().getHealthPercent() <= MinotaursConfig.getMinoConfig().CRITICAL_HEALTH_PERCENTAGE
                    , 3000);

            MinotaursConfig.getMinoConfig().SetEatPercentage(GetEatPercentage());
        }

        return 500;
    }

    public int GetEatPercentage() {
        int Variance = MinotaursConfig.getMinoConfig().EAT_PERCENTAGE_VARIANCE;
        int Base = MinotaursConfig.getMinoConfig().EAT_PERCENTAGE_BASE;

        return Calculations.random(Base - Variance,
                Base + Variance);
    }

}
