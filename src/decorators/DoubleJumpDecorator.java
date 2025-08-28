package decorators;

import interfaces.MarioComponent;
import ui.GameUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

/**
 * Double Jump Decorator - Allows second jump in mid-air
 */
public class DoubleJumpDecorator extends MarioDecorator {
    private boolean hasUsedSecondJump;
    private int duration;
    private ScheduledExecutorService timer;

    public DoubleJumpDecorator(MarioComponent mario) {
        super(mario);
        this.hasUsedSecondJump = false;
        this.duration = 10; // 10 seconds
        startTimer();
    }

    @Override
    public void jump() {
        decoratedMario.jump();
        if (!hasUsedSecondJump) {
            GameUI.printDecorator("⬆️ DOUBLE JUMP: Second jump activated!");
            hasUsedSecondJump = true;
            decoratedMario.addScore(20); // Bonus for double jump

            // Reset after 2 seconds (simulating landing)
            ScheduledExecutorService resetTimer = Executors.newScheduledThreadPool(1);
            resetTimer.schedule(() -> {
                hasUsedSecondJump = false;
                GameUI.printInfo("⬇️ Mario landed - double jump reset");
                resetTimer.shutdown();
            }, 2, TimeUnit.SECONDS);
        }
    }

    @Override
    public void move() {
        decoratedMario.move();
        hasUsedSecondJump = false; // Reset when moving on ground
    }

    @Override
    public String getStateName() {
        return decoratedMario.getStateName() + " [⬆️DoubleJump:" + duration + "s]";
    }

    @Override
    public boolean hasAbility(String ability) {
        return ability.equals("Double Jump") || decoratedMario.hasAbility(ability);
    }

    @Override
    public List<String> getAbilities() {
        List<String> abilities = new ArrayList<>(decoratedMario.getAbilities());
        abilities.add("Double Jump (" + duration + "s)");
        return abilities;
    }

    private void startTimer() {
        timer = Executors.newScheduledThreadPool(1);
        timer.scheduleAtFixedRate(() -> {
            duration--;
            if (duration <= 0) {
                GameUI.printDecorator("⬆️ Double Jump expired!");
                timer.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean isActive() {
        return duration > 0;
    }
}