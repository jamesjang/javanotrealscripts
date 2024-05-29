import behavior.branch.bank.BankingBranch;
import behavior.branch.combat.EnterCombatBranch;
import behavior.branch.pathing.WalkToBankBranch;
import behavior.branch.pathing.WalkToSecurityStrongholdBranch;
import behavior.leaf.bank.BankingLeaf;
import behavior.leaf.combat.AttackMinotaurLeaf;
import behavior.leaf.combat.EatLeaf;
import behavior.leaf.pathing.WalkToBankLeaf;
import behavior.leaf.pathing.WalktoSecurityStrongholdLeaf;
import org.dreambot.api.Client;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.randoms.LoginSolver;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.randoms.RandomManager;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.frameworks.treebranch.TreeScript;

import data.MinotaursConfig;
import org.dreambot.api.utilities.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Timer;

@ScriptManifest(category = Category.COMBAT,
        name = "Minotaur Killer",
        author = "Java Not Real",
        version = 1.0)

public class MinotaurKiller extends TreeScript {

    private JFrame GUIFrame;
    private Timer timer;

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
    }

    @Override
    public void onPaint(Graphics g) {

        g.setColor(Color.white);
        g.fillRect(0, 360, 560, 240);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 14));

        if (timer != null)
            g.drawString("Timer:  " + timer.formatTime(), 20, 400);

    }

    @Override
    public void onExit() {
        GUIFrame.dispose();
    }


    private void createGUI() {
        JFrame frame = new JFrame();
        GUIFrame = frame;
        frame.setTitle("Minotaur Killer");

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(Client.getCanvas());
        frame.setPreferredSize(new Dimension(350, 220));
        frame.getContentPane().setLayout(new BorderLayout());

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ScriptManager.getScriptManager().stop();
            }
        });

        JPanel settingPanel = new JPanel();
        settingPanel.setLayout(new GridLayout(0, 2));

        JLabel foodNameLabel = new JLabel();
        foodNameLabel.setText("Food Name");
        settingPanel.add(foodNameLabel);

        JTextField foodNameTextField = new JTextField();
        foodNameTextField.setPreferredSize( new Dimension( 200, 24 ) );
        settingPanel.add(foodNameTextField);

        JLabel EatPercentageBase = new JLabel();
        EatPercentageBase.setText("Base Eat Percntage");
        settingPanel.add(EatPercentageBase);

        JTextField EatPercentageBaseTextField = new JTextField();
        EatPercentageBaseTextField.setPreferredSize( new Dimension( 200, 24 ) );
        EatPercentageBaseTextField.setText( String.valueOf(MinotaursConfig.getMinoConfig().EAT_PERCENTAGE_BASE));
        settingPanel.add(EatPercentageBaseTextField);

        JLabel EatPercentageVariance = new JLabel();
        EatPercentageVariance.setText("Eat Percntage Variance");
        settingPanel.add(EatPercentageVariance);

        JTextField EatPercentageVarianceTextField = new JTextField();
        EatPercentageVarianceTextField.setPreferredSize( new Dimension( 200, 24 ) );
        EatPercentageVarianceTextField.setText( String.valueOf(MinotaursConfig.getMinoConfig().EAT_PERCENTAGE_VARIANCE));
        settingPanel.add(EatPercentageVarianceTextField);

        JLabel AttackStyleLabel = new JLabel();
        AttackStyleLabel.setText("Select Attack Style");
        settingPanel.add(AttackStyleLabel);

        JComboBox<String> AttackStyle = new JComboBox<>(new String[] {
           "Attack", "Strength", "Defence"
        });

        AttackStyle.setSelectedIndex(1);
        settingPanel.add(AttackStyle);

        frame.add(settingPanel);

        JPanel buttonPanel = new JPanel();
        settingPanel.setLayout(new FlowLayout());

        JButton button = new JButton();
        button.setText("Start Script");


        button.addActionListener(e -> {
            MinotaursConfig.getMinoConfig().setFoodSource(foodNameTextField.getText());
            MinotaursConfig.getMinoConfig().setEatPercentageBase(Integer.parseInt(EatPercentageBaseTextField.getText()));
            MinotaursConfig.getMinoConfig().setEatPercentageVariance(Integer.parseInt(EatPercentageVarianceTextField.getText()));
            MinotaursConfig.getMinoConfig().setAttackStyle(AttackStyle.getSelectedItem().toString());
            frame.dispose();

            RunScript();


        });

        buttonPanel.add(button);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }
}
