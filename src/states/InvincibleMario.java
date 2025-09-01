package states;

import core.Mario;
import ui.GameFrame;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

/**
 * Invincible Mario State - Temporary invincibility
 */
class InvincibleMario extends MarioState {
    private MarioState originalState;
    private int invincibilityTimer;
    private ScheduledExecutorService scheduler;

    public InvincibleMario(Mario mario, MarioState originalState) {
        super(mario);
        this.originalState = originalState;
        this.invincibilityTimer = 8;
        startTimer();
    }

    @Override
    public void jump() {
        GameFrame.getInstance().addLogMessage("⭐ Invincible Mario jumps with STAR POWER!", Color.BLUE);
        mario.addScore(25);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void move(int direction) {
        Point pos = mario.getPosition();
        pos.x += direction * 10; // Invincible Mario moves fastest

        // Boundary checking
        if (pos.x < 0) pos.x = 0;
        if (pos.x > GameFrame.getInstance().getGamePanel().getWidth() - 40)
            pos.x = GameFrame.getInstance().getGamePanel().getWidth() - 40;

        mario.setPosition(pos);

        GameFrame.getInstance().addLogMessage("⭐ Invincible Mario moves " + (direction > 0 ? "right" : "left") + " unstoppably", Color.BLUE);
        mario.addScore(15);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void move() {

    }

    @Override
    public void takeDamage() {
        GameFrame.getInstance().addLogMessage("⭐ Invincible Mario is IMMUNE to damage!", Color.YELLOW);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectMushroom() {
        mario.addScore(100);
        GameFrame.getInstance().addLogMessage("⭐ Invincible Mario collects mushroom (bonus points)", Color.CYAN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectFireFlower() {
        mario.addScore(200);
        GameFrame.getInstance().addLogMessage("⭐ Invincible Mario collects fire flower (bonus points)", Color.CYAN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectStar() {
        this.invincibilityTimer = 8;
        mario.addScore(300);
        GameFrame.getInstance().addLogMessage("⭐ Star collected! Invincibility timer reset!", Color.CYAN);
        GameFrame.getInstance().updateDisplay();
    }

    private void startTimer() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            invincibilityTimer--;
            SwingUtilities.invokeLater(() -> {
                GameFrame.getInstance().updateInvincibilityTimer(invincibilityTimer);
                if (invincibilityTimer <= 0) {
                    mario.setState(originalState);
                    GameFrame.getInstance().addLogMessage("⭐ → " + originalState.getStateEmoji() + " Invincibility expired!", Color.ORANGE);
                    GameFrame.getInstance().updateDisplay();
                    scheduler.shutdown();
                }
            });
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public String getStateName() {
        return "Invincible " + originalState.getStateName() + " (" + invincibilityTimer + "s)";
    }
    @Override
    public String getStateEmoji() { return "⭐"; }
    @Override
    public boolean canBreakBlocks() { return true; }
    @Override
    public boolean canShootFire() { return originalState.canShootFire(); }
    @Override
    public Color getStateColor() { return new Color(255, 255, 0); }
}