package data;

import org.dreambot.api.methods.combat.CombatStyle;
import org.dreambot.api.methods.map.Area;

public class MinotaursConfig {
    private static final MinotaursConfig minoConfig = new MinotaursConfig();
    public static MinotaursConfig getMinoConfig() {
        return minoConfig;
    }

    public String FOOD_SOURCE = "Salmon";

    public int EAT_PERCENTAGE_BASE = 50;
    public int EAT_PERCENTAGE_VARIANCE = 7;
    private int EAT_PERCENTAGE;

    public String MINOTAUR = "Minotaur";
    public Area BANK_AREA = new Area(3092, 3497, 3097, 3494);
    public Area SECCURITY_STRONGHOLD = new Area(1870, 5221, 1882, 5209);
            //new Area(3074, 3422, 3085, 3417);

    public CombatStyle AttackStyle;

    public void setEatPercentageBase(int v) { this.EAT_PERCENTAGE_BASE = v;}
    public void setEatPercentageVariance(int v) { this.EAT_PERCENTAGE_VARIANCE = v;}
    public void SetEatPercentage(int v) {
        this.EAT_PERCENTAGE = v;
    }
    public void setFoodSource(String v) { this.FOOD_SOURCE = v;}
    public void setAttackStyle(String v) {
        if (v.equalsIgnoreCase("Attack")) {
            AttackStyle = CombatStyle.ATTACK;
            return;
        }
        if (v.equalsIgnoreCase("Strength")) {
            AttackStyle = CombatStyle.STRENGTH;
            return;
        }
        if (v.equalsIgnoreCase("Defence")) {
            AttackStyle = CombatStyle.DEFENCE;
        }
    }
}
