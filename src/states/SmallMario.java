package states;

import core.Mario;
import ui.GameFrame;

import java.awt.Color;

/**
 * Small Mario State - Default state
 */
public class SmallMario extends MarioState {

    public SmallMario(Mario mario) {
        super(mario);
    }

    @Override
    public void jump() {
        GameFrame.getInstance().addLogMessage("🔴 Small Mario performs a small jump!", Color.BLUE);
        mario.addScore(10);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void move() {
        GameFrame.getInstance().addLogMessage("🔴 Small Mario moves carefully", Color.BLUE);
        mario.addScore(5);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void takeDamage() {
        mario.setLives(mario.getLives()-1);
        GameFrame.getInstance().addLogMessage("💀 Small Mario dies! Lost a life!", Color.RED);
        if (mario.getLives() <= 0) {
            GameFrame.getInstance().showGameOver();
        }
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectMushroom() {
        mario.setState(new BigMario(mario));
        mario.addScore(100);
        GameFrame.getInstance().addLogMessage("🔴 → 🟡 Small Mario becomes Big Mario!", Color.GREEN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectFireFlower() {
        mario.setState(new FireMario(mario));
        mario.addScore(200);
        GameFrame.getInstance().addLogMessage("🔴 → 🔥 Small Mario becomes Fire Mario!", Color.GREEN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectStar() {
        mario.setState(new InvincibleMario(mario, this));
        mario.addScore(300);
        GameFrame.getInstance().addLogMessage("🔴 → ⭐ Small Mario becomes Invincible!", Color.GREEN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public String getStateName() {
        return "Small Mario";
    }

    @Override
    public String getStateEmoji() {
        return "🔴";
    }

    @Override
    public boolean canBreakBlocks() {
        return false;
    }

    @Override
    public boolean canShootFire() {
        return false;
    }

    @Override
    public Color getStateColor() {
        return new Color(255, 100, 100);
    }
}