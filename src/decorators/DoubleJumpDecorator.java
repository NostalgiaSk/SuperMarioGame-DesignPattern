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
 * Double Jump Decorator - Allows second jump in mid-air
 */
public class DoubleJumpDecorator extends MarioDecorator {
    private boolean hasUsedSecondJump;
    private int duration;
    private ScheduledExecutorService timer;

    public DoubleJumpDecorator(MarioComponent mario) {
        super(mario);
        this.hasUsedSecondJump = false;
        this.duration = 10;
        startTimer();
    }

    @Override
    public void jump() {
        decoratedMario.jump();
        if (!hasUsedSecondJump) {
            GameFrame.getInstance().addLogMessage("⬆️ DOUBLE JUMP: Second jump activated!", Color.MAGENTA);
            hasUsedSecondJump = true;
            decoratedMario.addScore(20);

            ScheduledExecutorService resetTimer = Executors.newScheduledThreadPool(1);
            resetTimer.schedule(() -> {
                hasUsedSecondJump = false;
                SwingUtilities.invokeLater(() -> GameFrame.getInstance().addLogMessage("⬇️ Mario landed - double jump reset", Color.GRAY));
                resetTimer.shutdown();
            }, 2, TimeUnit.SECONDS);
        }
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void move(int direction) {
        decoratedMario.move(direction);
        hasUsedSecondJump = false;
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

    @Override
    public void setLives(int lives) {

    }

    private void startTimer() {
        timer = Executors.newScheduledThreadPool(1);
        timer.scheduleAtFixedRate(() -> {
            duration--;
            SwingUtilities.invokeLater(() -> GameFrame.getInstance().updateDisplay());
            if (duration <= 0) {
                SwingUtilities.invokeLater(() -> {
                    GameFrame.getInstance().addLogMessage("⬆️ Double Jump expired!", Color.GRAY);
                    GameFrame.getInstance().removeDecorator("Double Jump");
                });
                timer.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}