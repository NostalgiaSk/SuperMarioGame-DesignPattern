package states;
import core.Mario;
import ui.GameUI;

class BigMario extends MarioState {

    public BigMario(Mario mario) {
        super(mario);
    }

    @Override
    public void jump() {
        GameUI.printAction("🟡 Big Mario performs a HIGH jump!");
        mario.addScore(15);
    }

    @Override
    public void move() {
        GameUI.printAction("🟡 Big Mario moves with power");
        mario.addScore(8);
    }

    @Override
    public void takeDamage() {
        mario.setState(new SmallMario(mario));
        GameUI.printDamage("🟡 → 🔴 Big Mario becomes Small Mario");
    }

    @Override
    public void collectMushroom() {
        mario.addScore(100);
        GameUI.printBonus("🟡 Big Mario collects mushroom (bonus points only)");
    }

    @Override
    public void collectFireFlower() {
        mario.setState(new FireMario(mario));
        mario.addScore(200);
        GameUI.printStateChange("🟡 → 🔥 Big Mario becomes Fire Mario!");
    }

    @Override
    public void collectStar() {
        mario.setState(new InvincibleMario(mario, this));
        mario.addScore(300);
        GameUI.printStateChange("🟡 → ⭐ Big Mario becomes Invincible!");
    }

    @Override
    public String getStateName() { return "Big Mario"; }

    @Override
    public boolean canBreakBlocks() { return true; }

    @Override
    public boolean canShootFire() { return false; }
}