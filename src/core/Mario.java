package core;

import interfaces.MarioComponent;
import states.*;
import ui.GameUI;
import java.util.List;
import java.util.ArrayList;

/**
 * Main Mario class - Context for State Pattern, Component for Decorator Pattern
 */
public class Mario implements MarioComponent {
    private MarioState currentState;
    private int x, y;
    private int score;
    private int lives;
    private List<String> abilities;

    public Mario() {
        this.currentState = new SmallMario(this);
        this.x = 0;
        this.y = 0;
        this.score = 0;
        this.lives = 3;
        this.abilities = new ArrayList<>();
    }

    // State management
    public void setState(MarioState state) {
        this.currentState = state;
    }

    public MarioState getState() {
        return currentState;
    }

    // Public method for states to reduce lives
    public void loseLife() {
        this.lives--;
    }

    // Delegate to current state (State Pattern)
    @Override
    public void jump() {
        currentState.jump();
    }

    @Override
    public void move() {
        currentState.move();
    }

    @Override
    public void takeDamage() {
        currentState.takeDamage();
    }

    @Override
    public void collectMushroom() {
        currentState.collectMushroom();
    }

    @Override
    public void collectFireFlower() {
        currentState.collectFireFlower();
    }

    @Override
    public void collectStar() {
        currentState.collectStar();
    }

    @Override
    public String getStateName() {
        return currentState.getStateName();
    }

    @Override
    public boolean canBreakBlocks() {
        return currentState.canBreakBlocks();
    }

    @Override
    public boolean canShootFire() {
        return currentState.canShootFire();
    }

    // Mario-specific methods
    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void addScore(int points) {
        this.score += points;
    }

    @Override
    public boolean hasAbility(String ability) {
        return abilities.contains(ability);
    }

    @Override
    public List<String> getAbilities() {
        return new ArrayList<>(abilities);
    }

    @Override
    public int getLives() {
        return lives;
    }

    // Special method for Fire Mario
    public void shootFire() {
        if (currentState instanceof FireMario) {
            ((FireMario) currentState).shootFire();
        } else {
            GameUI.printError("❌ Cannot shoot fire in current state!");
        }
    }
}