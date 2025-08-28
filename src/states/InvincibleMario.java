package states;

import core.Mario;
import ui.GameUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Invincible Mario State - Temporary invincibility
 */
public class InvincibleMario extends MarioState {
    private MarioState originalState;
    private int invincibilityTimer;
    private ScheduledExecutorService scheduler;

    public InvincibleMario(Mario mario, MarioState originalState) {
        super(mario);
        this.originalState = originalState;
        this.invincibilityTimer = 8; // 8 seconds of invincibility
        startTimer();
    }

    @Override
    public void jump() {
        GameUI.printAction("⭐ Invincible Mario jumps with STAR POWER!");
        mario.addScore(25);
    }

    @Override
    public void move() {
        GameUI.printAction("⭐ Invincible Mario moves unstoppably");
        mario.addScore(15);
    }

    @Override
    public void takeDamage() {
        GameUI.printAction("⭐ Invincible Mario is IMMUNE to damage!");
    }

    @Override
    public void collectMushroom() {
        mario.addScore(100);
        GameUI.printBonus("⭐ Invincible Mario collects mushroom (bonus points)");
    }

    @Override
    public void collectFireFlower() {
        mario.addScore(200);
        GameUI.printBonus("⭐ Invincible Mario collects fire flower (bonus points)");
    }

    @Override
    public void collectStar() {
        this.invincibilityTimer = 8; // Reset timer
        mario.addScore(300);
        GameUI.printBonus("⭐ Star collected! Invincibility timer reset!");
    }

    private void startTimer() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            invincibilityTimer--;
            GameUI.printTimer("⭐ Invincibility: " + invincibilityTimer + " seconds left");

            if (invincibilityTimer <= 0) {
                mario.setState(originalState);
                GameUI.printStateChange("⭐ → " + getStateEmoji(originalState) + " Invincibility expired!");
                scheduler.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private String getStateEmoji(MarioState state) {
        if (state instanceof SmallMario) return "🔴";
        if (state instanceof BigMario) return "🟡";
        if (state instanceof FireMario) return "🔥";
        return "❓";
    }

    @Override
    public String getStateName() {
        return "Invincible " + originalState.getStateName() + " (" + invincibilityTimer + "s)";
    }

    @Override
    public boolean canBreakBlocks() {
        return true;
    }

    @Override
    public boolean canShootFire() {
        return originalState.canShootFire();
    }
}