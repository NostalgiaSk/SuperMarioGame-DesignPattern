package states;
import core.Mario;
import ui.GameFrame;

import java.awt.Color;

/**
 * Big Mario State - Powered up state
 */
public class BigMario extends MarioState {

    public BigMario(Mario mario) {
        super(mario);
    }

    @Override
    public void jump() {
        GameFrame.getInstance().addLogMessage("🟡 Big Mario performs a HIGH jump!", Color.BLUE);
        mario.addScore(15);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void move() {
        GameFrame.getInstance().addLogMessage("🟡 Big Mario moves with power", Color.BLUE);
        mario.addScore(8);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void takeDamage() {
        mario.setState(new SmallMario(mario));
        GameFrame.getInstance().addLogMessage("🟡 → 🔴 Big Mario becomes Small Mario", Color.ORANGE);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectMushroom() {
        mario.addScore(100);
        GameFrame.getInstance().addLogMessage("🟡 Big Mario collects mushroom (bonus points only)", Color.CYAN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectFireFlower() {
        mario.setState(new FireMario(mario));
        mario.addScore(200);
        GameFrame.getInstance().addLogMessage("🟡 → 🔥 Big Mario becomes Fire Mario!", Color.GREEN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectStar() {
        mario.setState(new InvincibleMario(mario, this));
        mario.addScore(300);
        GameFrame.getInstance().addLogMessage("🟡 → ⭐ Big Mario becomes Invincible!", Color.GREEN);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public String getStateName() {
        return "Big Mario";
    }

    @Override
    public String getStateEmoji() {
        return "🟡";
    }

    @Override
    public boolean canBreakBlocks() {
        return true;
    }

    @Override
    public boolean canShootFire() {
        return false;
    }

    @Override
    public Color getStateColor() {
        return new Color(255, 255, 100);
    }
}