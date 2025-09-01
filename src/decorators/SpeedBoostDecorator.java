package decorators;

import interfaces.MarioComponent;
import ui.GameFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

/**
 * Speed Boost Decorator - Increases movement speed temporarily
 */
public class SpeedBoostDecorator extends MarioDecorator {
    private int duration;
    private ScheduledExecutorService timer;

    public SpeedBoostDecorator(MarioComponent mario) {
        super(mario);
        this.duration = 8;
        startTimer();
    }

    @Override
    public void move(int direction) {
        // Apply speed boost (move further)
        Point pos = decoratedMario.getPosition();
        pos.x += direction * 12; // Even faster movement

        // Boundary checking
        if (pos.x < 0) pos.x = 0;
        if (pos.x > GameFrame.getInstance().getGamePanel().getWidth() - 40)
            pos.x = GameFrame.getInstance().getGamePanel().getWidth() - 40;

        decoratedMario.setPosition(pos);

        GameFrame.getInstance().addLogMessage("🚀 SPEED BOOST: Moving at 2x speed!", Color.MAGENTA);
        decoratedMario.addScore(5);
        GameFrame.getInstance().updateDisplay();
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
                    GameFrame.getInstance().addLogMessage("🚀 Speed Boost expired!", Color.GRAY);
                    GameFrame.getInstance().removeDecorator("Speed Boost");
                });
                timer.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
