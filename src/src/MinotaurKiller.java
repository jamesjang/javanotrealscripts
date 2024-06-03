package src;

import behavior.branch.bank.BankingBranch;
import behavior.branch.combat.EnterCombatBranch;
import behavior.branch.pathing.WalkToBankBranch;
import behavior.branch.pathing.WalkToSecurityStrongholdBranch;
import behavior.leaf.bank.BankingLeaf;
import behavior.leaf.combat.AttackMinotaurLeaf;
import behavior.leaf.combat.EatLeaf;
import behavior.leaf.pathing.WalkToBankLeaf;
import behavior.leaf.pathing.WalktoSecurityStrongholdLeaf;
import gui.MinotaurGUI;
import org.dreambot.api.Client;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.frameworks.treebranch.TreeScript;

import data.MinotaursConfig;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;
import sun.net.www.http.HttpClient;


@ScriptManifest(category = Category.COMBAT,
        name = "Minotaur Killer",
        author = "Java Not Real",
        version = 1.1)

public class MinotaurKiller extends TreeScript {

    private JFrame GUIFrame;
    private Timer timer;
    private MinotaurGUI gui;

    private String bgurl = "https://s10.gifyu.com/images/SYHlT.jpg";
    private final Image bg = getImage(bgurl);

    private int startingCombatxp;
    private Skill trackedSkill;
    private boolean startTracking = false;

    @Override
    public void onStart() {
        timer = new Timer();

        getRandomManager().disableSolver(RandomEvent.LOGIN);
        SwingUtilities.invokeLater(new Runnable() {
                                       @Override
                                       public void run() {
                                           createGUI();
                                       }
                                   });

        //starting eat value
        MinotaursConfig.getMinoConfig().SetEatPercentage(MinotaursConfig.getMinoConfig().EAT_PERCENTAGE_BASE);

    }

    public void RunScript() {
        getRandomManager().enableSolver(RandomEvent.LOGIN);

        Combat.setCombatStyle(MinotaursConfig.getMinoConfig().AttackStyle);

        Sleep.sleepUntil(() -> Combat.getCombatStyle() == MinotaursConfig.getMinoConfig().AttackStyle, 4000, 500);

        addBranches(
                new EnterCombatBranch().addLeaves(new EatLeaf(), new AttackMinotaurLeaf()),
                new WalkToBankBranch().addLeaves(new WalkToBankLeaf()),
                new WalkToSecurityStrongholdBranch().addLeaves(new WalktoSecurityStrongholdLeaf()),
                new BankingBranch().addLeaves(new BankingLeaf())
        );

        switch (MinotaursConfig.getMinoConfig().AttackStyle) {
            case ATTACK:
                startingCombatxp = Skills.getExperience(Skill.ATTACK);
                trackedSkill = Skill.ATTACK;
                break;
            case STRENGTH:
                startingCombatxp = Skills.getExperience(Skill.STRENGTH);
                trackedSkill = Skill.STRENGTH;
                break;
            case DEFENCE:
                startingCombatxp = Skills.getExperience(Skill.DEFENCE);
                trackedSkill = Skill.DEFENCE;
                break;
        }

        startTracking = true;
    }

    @Override
    public void onPause() {
        if (ScriptManager.getScriptManager().isPaused()) {
            timer.pause();
            startTracking = false;
        } else if (!ScriptManager.getScriptManager().isPaused()) {
            timer.resume();
            startTracking = true;
        }
    }
    @Override
    public void onPaint(Graphics g) {

        if (bg != null) {
            g.drawImage(bg, 0, Client.getViewportHeight() - 160, null);
        }

        int gainedExp = Skills.getExperience(trackedSkill) - startingCombatxp;
        int xpPerhour = timer.getHourlyRate(gainedExp);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 18));

        if (startTracking) {
            if (timer != null)
                g.drawString("Timer:  " + timer.formatTime(), 210, 427);

            g.drawString(MinotaursConfig.getMinoConfig().SKILL_TRACKING_STRING + "Exp Gained: " + gainedExp, 210, 457);
            g.drawString("Exp per hour: " + xpPerhour, 210, 477);
        }
    }


    @Override
    public void onExit() {
        gui.dispose();
    }


    private void createGUI() {
        gui = new MinotaurGUI(this);

    }

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) { }
        return null;
    }


}
