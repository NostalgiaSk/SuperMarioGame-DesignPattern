package states;

import core.Mario;
import ui.GameFrame;

import java.awt.*;

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
    public void move(int direction) {
        Point pos = mario.getPosition();
        pos.x += direction * 5; // Move 5 pixels in direction

        // Boundary checking
        if (pos.x < 0) pos.x = 0;
        if (pos.x > GameFrame.getInstance().getGamePanel().getWidth() - 40)
            pos.x = GameFrame.getInstance().getGamePanel().getWidth() - 40;

        mario.setPosition(pos);

        GameFrame.getInstance().addLogMessage("🔴 Small Mario moves " + (direction > 0 ? "right" : "left"), Color.BLUE);
        mario.addScore(5);
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void move() {

    }

    @Override
    public void takeDamage() {
        mario.setLives(mario.getLives() - 1);
        GameFrame.getInstance().addLogMessage("💀 Small Mario dies! Lost a life!", Color.RED);
        if (mario.getLives() <= 0) {
            GameFrame.getInstance().showGameOver();
        } else {
            // Reset position after damage
            mario.setPosition(new Point(100, 400));
        }
        GameFrame.getInstance().updateDisplay();
    }

    @Override
    public void collectMushroom() {

    }
//
//    @Override
//    public void collectMushroom() {
//        mario.setState(new BigMario(mario));
//        mario.addScore(100);
//        GameFrame.getInstance().addLogMessage("🔴 → 🟡 Small Mario becomes Big Mario!", Color.GREEN);
//        GameFrame.getInstance().updateDisplay();
//    }

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
    public String getStateName() { return "Small Mario"; }
    @Override
    public String getStateEmoji() { return "🔴"; }
    @Override
    public boolean canBreakBlocks() { return false; }
    @Override
    public boolean canShootFire() { return false; }
    @Override
    public Color getStateColor() { return new Color(255, 100, 100); }
}