package decorators;

import interfaces.MarioComponent;
import ui.GameUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

/**
 * Speed Boost Decorator - Increases movement speed temporarily
 */
public class SpeedBoostDecorator extends MarioDecorator {
    private double speedMultiplier;
    private int duration;
    private ScheduledExecutorService timer;

    public SpeedBoostDecorator(MarioComponent mario) {
        super(mario);
        this.speedMultiplier = 2.0;
        this.duration = 8; // 8 seconds
        startTimer();
    }

    @Override
    public void move() {
        decoratedMario.move();
        GameUI.printDecorator("🚀 SPEED BOOST: Moving at " + speedMultiplier + "x speed!");
        decoratedMario.addScore(5); // Bonus points for enhanced movement
    }

    @Override
    public String getStateName() {
        return decoratedMario.getStateName() + " [🚀Speed:" + duration + "s]";
    }

    @Override
    public boolean hasAbility(String ability) {
        return ability.equals("Speed Boost") || decoratedMario.hasAbility(ability);
    }

    @Override
    public List<String> getAbilities() {
        List<String> abilities = new ArrayList<>(decoratedMario.getAbilities());
        abilities.add("Speed Boost (" + duration + "s)");
        return abilities;
    }

    private void startTimer() {
        timer = Executors.newScheduledThreadPool(1);
        timer.scheduleAtFixedRate(() -> {
            duration--;
            if (duration <= 0) {
                GameUI.printDecorator("🚀 Speed Boost expired!");
                timer.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean isActive() {
        return duration > 0;
    }
}