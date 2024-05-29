package behavior.branch.combat;

import data.MinotaursConfig;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.frameworks.treebranch.Branch;

public class EnterCombatBranch extends Branch{

    @Override
    public boolean isValid() {
        return Inventory.contains(MinotaursConfig.getMinoConfig().FOOD_SOURCE) &&
                MinotaursConfig.getMinoConfig().SECCURITY_STRONGHOLD.contains(Players.getLocal().getTile());
    }
}
