package behavior.leaf.combat;

import data.MinotaursConfig;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.frameworks.treebranch.Leaf;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import utils.Antiban;

public class AttackMinotaurLeaf extends Leaf {

    @Override
    public boolean isValid()  {
        return MinotaursConfig.getMinoConfig().SECCURITY_STRONGHOLD.contains(Players.getLocal().getTile() )&&
                Inventory.contains(MinotaursConfig.getMinoConfig().FOOD_SOURCE) &&
                !Players.getLocal().isInCombat();
    }

    @Override
    public int onLoop() {
        Sleep.sleep(Antiban.getAntiBan().performAntiban());



        NPC monster = NPCs.closest(m-> MinotaursConfig.getMinoConfig().MINOTAUR.equalsIgnoreCase(m.getName())
                && m.canReach()
                && m.canAttack()
                && MinotaursConfig.getMinoConfig().SECCURITY_STRONGHOLD.contains(m.getTile()));

        if (monster != null) {
            monster.interact("Attack");

            Sleep.sleepUntil(()-> Players.getLocal().isInCombat(), 3000, 200);

            return 1000;
        }

        return 500;
    }
}
