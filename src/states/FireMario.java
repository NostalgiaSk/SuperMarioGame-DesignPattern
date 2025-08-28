package states;

import core.Mario;
import ui.GameUI;

/**
 * Fire Mario State - Fire power state
 */
public class FireMario extends MarioState {

    public FireMario(Mario mario) {
        super(mario);
    }

    @Override
    public void jump() {
        GameUI.printAction("🔥 Fire Mario jumps with burning flames!");
        mario.addScore(20);
    }

    @Override
    public void move() {
        GameUI.printAction("🔥 Fire Mario moves with fiery power");
        mario.addScore(12);
    }

    @Override
    public void takeDamage() {
        mario.setState(new BigMario(mario));
        GameUI.printDamage("🔥 → 🟡 Fire Mario becomes Big Mario");
    }

    @Override
    public void collectMushroom() {
        mario.addScore(100);
        GameUI.printBonus("🔥 Fire Mario collects mushroom (bonus points only)");
    }

    @Override
    public void collectFireFlower() {
        mario.addScore(200);
        GameUI.printBonus("🔥 Fire Mario collects fire flower (bonus points only)");
    }

    @Override
    public void collectStar() {
        mario.setState(new InvincibleMario(mario, this));
        mario.addScore(300);
        GameUI.printStateChange("🔥 → ⭐ Fire Mario becomes Invincible!");
    }

    public void shootFire() {
        GameUI.printAction("🔥💥 Fire Mario shoots a fireball!");
        mario.addScore(50);
    }

    @Override
    public String getStateName() {
        return "Fire Mario";
    }

    @Override
    public boolean canBreakBlocks() {
        return true;
    }

    @Override
    public boolean canShootFire() {
        return true;
    }
}