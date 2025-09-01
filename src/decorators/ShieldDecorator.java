package decorators;

import interfaces.MarioComponent;
import ui.GameFrame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

/**
 * Shield Decorator - Absorbs damage
 */
public class ShieldDecorator extends MarioDecorator {
    private int shieldStrength;
    private int duration;
    private ScheduledExecutorService timer;

    public ShieldDecorator(MarioComponent mario) {
        super(mario);
        this.shieldStrength = 2;
        this.duration = 12;
        startTimer();
    }

    @Override
    public void takeDamage() {
        if (shieldStrength > 0) {
            shieldStrength--;
            GameFrame.getInstance().addLogMessage("🛡️ SHIELD: Damage blocked! Strength: " + shieldStrength, Color.CYAN);
            if (shieldStrength <= 0) {
                GameFrame.getInstance().addLogMessage("🛡️ Shield broken!", Color.ORANGE);
                GameFrame.getInstance().removeDecorator("Shield");
            }
        } else {
            decoratedMario.takeDamage();
        }
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public String getStateName() {
        return decoratedMario.getStateName() + " [🛡️Shield:" + shieldStrength + "/" + duration + "s]";
    }

    @Override
    public boolean hasAbility(String ability) {
        return ability.equals("Shield") || decoratedMario.hasAbility(ability);
    }

    @Override
    public List<String> getAbilities() {
        List<String> abilities = new ArrayList<>(decoratedMario.getAbilities());
        abilities.add("Shield (" + shieldStrength + " hits, " + duration + "s)");
        return abilities;
    }

    private void startTimer() {
        timer = Executors.newScheduledThreadPool(1);
        timer.scheduleAtFixedRate(() -> {
            duration--;
            SwingUtilities.invokeLater(() -> GameFrame.getInstance().updateDisplay());
            if (duration <= 0 || shieldStrength <= 0) {
                SwingUtilities.invokeLater(() -> {
                    GameFrame.getInstance().addLogMessage("🛡️ Shield expired!", Color.GRAY);
                    GameFrame.getInstance().removeDecorator("Shield");
                });
                timer.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean isActive() {
        return duration > 0 && shieldStrength > 0;
    }
}