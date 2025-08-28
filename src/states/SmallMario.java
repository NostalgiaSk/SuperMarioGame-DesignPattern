package states;

import core.Mario;
import ui.GameUI;

/**
 * Small Mario State - Default state
 */
public class SmallMario extends MarioState {

    public SmallMario(Mario mario) {
        super(mario);
    }

    @Override
    public void jump() {
        GameUI.printAction("🔴 Small Mario performs a small jump!");
        mario.addScore(10);
    }

    @Override
    public void move() {
        GameUI.printAction("🔴 Small Mario moves carefully");
        mario.addScore(5);
    }

    @Override
    public void takeDamage() {
        mario.loseLife();
        GameUI.printDamage("💀 Small Mario dies! Lost a life!");
        if (mario.getLives() <= 0) {
            GameUI.printGameOver("🎮 GAME OVER!");
        }
    }

    @Override
    public void collectMushroom() {
        mario.setState(new BigMario(mario));
        mario.addScore(100);
        GameUI.printStateChange("🔴 → 🟡 Small Mario becomes Big Mario!");
    }

    @Override
    public void collectFireFlower() {
        mario.setState(new FireMario(mario));
        mario.addScore(200);
        GameUI.printStateChange("🔴 → 🔥 Small Mario becomes Fire Mario!");
    }

    @Override
    public void collectStar() {
        mario.setState(new InvincibleMario(mario, this));
        mario.addScore(300);
        GameUI.printStateChange("🔴 → ⭐ Small Mario becomes Invincible!");
    }

    @Override
    public String getStateName() {
        return "Small Mario";
    }

    @Override
    public boolean canBreakBlocks() {
        return false;
    }

    @Override
    public boolean canShootFire() {
        return false;
    }
}