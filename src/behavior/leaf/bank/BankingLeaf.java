package behavior.leaf.bank;

import data.MinotaursConfig;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.frameworks.treebranch.Leaf;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;


public class BankingLeaf extends Leaf {

    private String foodSource;

    public BankingLeaf()  {
        foodSource = MinotaursConfig.getMinoConfig().FOOD_SOURCE;

    }
    @Override
    public boolean isValid() {
        return !Inventory.contains(MinotaursConfig.getMinoConfig().FOOD_SOURCE) &&
                MinotaursConfig.getMinoConfig().BANK_AREA.contains(Players.getLocal().getTile());

    }

    @Override
    public int onLoop() {
        if (Bank.open()) {
            if (Bank.contains(MinotaursConfig.getMinoConfig().FOOD_SOURCE)) {
                Bank.withdrawAll(MinotaursConfig.getMinoConfig().FOOD_SOURCE);
            } else {
                ScriptManager.getScriptManager().getCurrentScript().stop();
            }
        }

        return 100;
    }
}
