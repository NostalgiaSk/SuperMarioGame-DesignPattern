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
 * Super Strength Decorator - Enhanced block breaking ability
 */
public class SuperStrengthDecorator extends MarioDecorator {
    private int duration;
    private ScheduledExecutorService timer;

    public SuperStrengthDecorator(MarioComponent mario) {
        super(mario);
        this.duration = 6;
        startTimer();
    }

    @Override
    public boolean canBreakBlocks() {
        return true; // Can always break blocks with super strength
    }

    @Override
    public String getStateName() {
        return decoratedMario.getStateName() + " [💪Strength:" + duration + "s]";
    }

    @Override
    public boolean hasAbility(String ability) {
        return ability.equals("Super Strength") || decoratedMario.hasAbility(ability);
    }

    @Override
    public List<String> getAbilities() {
        List<String> abilities = new ArrayList<>(decoratedMario.getAbilities());
        abilities.add("Super Strength (" + duration + "s)");
        return abilities;
    }

    public void breakSpecialBlock() {
        GameFrame.getInstance().addLogMessage("💪 SUPER STRENGTH: Breaking reinforced block!", Color.MAGENTA);
        decoratedMario.addScore(100);
        GameFrame.getInstance().updateDisplay();
    }

    private void startTimer() {
        timer = Executors.newScheduledThreadPool(1);
        timer.scheduleAtFixedRate(() -> {
            duration--;
            SwingUtilities.invokeLater(() -> GameFrame.getInstance().updateDisplay());
            if (duration <= 0) {
                SwingUtilities.invokeLater(() -> {
                    GameFrame.getInstance().addLogMessage("💪 Super Strength expired!", Color.GRAY);
                    GameFrame.getInstance().removeDecorator("Super Strength");
                });
                timer.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean isActive() {
        return duration > 0;
    }
}