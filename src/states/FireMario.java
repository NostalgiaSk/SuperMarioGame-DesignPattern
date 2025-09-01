package states;

import core.Mario;
import ui.GameFrame;

import java.awt.Color;

/**
 * Fire Mario State - Fire power state
 */
public class FireMario extends MarioState {

    public FireMario(Mario mario) {
        super(mario);
    }

    @Override
    public void jump() {
        GameFrame.getInstance().addLogMessage("🔥 Fire Mario jumps with burning flames!", Color.BLUE);
        mario.addScore(20);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void move() {
        GameFrame.getInstance().addLogMessage("🔥 Fire Mario moves with fiery power", Color.BLUE);
        mario.addScore(12);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void takeDamage() {
        mario.setState(new BigMario(mario));
        GameFrame.getInstance().addLogMessage("🔥 → 🟡 Fire Mario becomes Big Mario", Color.ORANGE);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectMushroom() {
        mario.addScore(100);
        GameFrame.getInstance().addLogMessage("🔥 Fire Mario collects mushroom (bonus points only)", Color.CYAN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectFireFlower() {
        mario.addScore(200);
        GameFrame.getInstance().addLogMessage("🔥 Fire Mario collects fire flower (bonus points only)", Color.CYAN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectStar() {
        mario.setState(new InvincibleMario(mario, this));
        mario.addScore(300);
        GameFrame.getInstance().addLogMessage("🔥 → ⭐ Fire Mario becomes Invincible!", Color.GREEN);
        GameFrame.getInstance().updateDisplay();
    }

    public void shootFire() {
        GameFrame.getInstance().addLogMessage("🔥💥 Fire Mario shoots a fireball!", Color.MAGENTA);
        mario.addScore(50);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public String getStateName() {
        return "Fire Mario";
    }

    @Override
    public String getStateEmoji() {
        return "🔥";
    }

    @Override
    public boolean canBreakBlocks() {
        return true;
    }

    @Override
    public boolean canShootFire() {
        return true;
    }

    @Override
    public Color getStateColor() {
        return new Color(255, 150, 50);
    }
}