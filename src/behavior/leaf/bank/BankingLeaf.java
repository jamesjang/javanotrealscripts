package behavior.leaf.bank;

import data.MinotaursConfig;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.frameworks.treebranch.Leaf;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;


public class BankingLeaf extends Leaf {

    @Override
    public boolean isValid() {
        return !Inventory.contains(MinotaursConfig.getMinoConfig().FOOD_SOURCE) &&
                MinotaursConfig.getMinoConfig().BANK_AREA.contains(Players.getLocal().getTile());

    }

    @Override
    public int onLoop() {
        if (!Bank.isOpen() && !Players.getLocal().isMoving()) {

            GameObject bankBooth = GameObjects.closest("Bank booth");
            bankBooth.interact("Bank");

            Sleep.sleepUntil(() -> Bank.isOpen(), 4000, 100);

            Bank.withdrawAll(MinotaursConfig.getMinoConfig().FOOD_SOURCE);

            Sleep.sleepUntil(() -> Bank.close(), 4000, 750);
        }

        return 100;
    }
}
