package decorators;

import interfaces.MarioComponent;
import ui.GameUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

/**
 * Super Strength Decorator - Enhanced block breaking ability
 */
public class SuperStrengthDecorator extends MarioDecorator {
    private double strengthMultiplier;
    private int duration;
    private ScheduledExecutorService timer;

    public SuperStrengthDecorator(MarioComponent mario) {
        super(mario);
        this.strengthMultiplier = 3.0;
        this.duration = 6; // 6 seconds
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
        GameUI.printDecorator("💪 SUPER STRENGTH: Breaking reinforced block!");
        decoratedMario.addScore(100);
    }

    private void startTimer() {
        timer = Executors.newScheduledThreadPool(1);
        timer.scheduleAtFixedRate(() -> {
            duration--;
            if (duration <= 0) {
                GameUI.printDecorator("💪 Super Strength expired!");
                timer.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean isActive() {
        return duration > 0;
    }
}