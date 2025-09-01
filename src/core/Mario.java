package core;

import interfaces.MarioComponent;
import states.FireMario;
import states.MarioState;
import states.SmallMario;
import ui.GameFrame;

import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Mario class - Context for State Pattern, Component for Decorator Pattern
 */
public class Mario implements MarioComponent {
    private MarioState currentState;
    private Point position;
    private int score;
    protected int lives;
    private List<String> abilities;
    private int facingDirection = 1; // 1 for right, -1 for left

    public Mario() {
        this.currentState = new SmallMario(this);
        this.position = new Point(100, 400);
        this.score = 0;
        this.lives = 3;
        this.abilities = new ArrayList<>();
    }

    public void setState(MarioState state) {
        this.currentState = state;
    }

    public MarioState getState() {
        return currentState;
    }

    @Override
    public void jump() {
        currentState.jump();
        GameFrame.getInstance().getGamePanel().performJumpAnimation();
    }

    @Override
    public void move(int direction) {
        facingDirection = direction;
        currentState.move(direction);
    }

    @Override
    public void takeDamage() { currentState.takeDamage(); }
    @Override
    public void collectMushroom() { currentState.collectMushroom(); }
    @Override
    public void collectFireFlower() { currentState.collectFireFlower(); }
    @Override
    public void collectStar() { currentState.collectStar(); }
    @Override
    public String getStateName() { return currentState.getStateName(); }
    @Override
    public String getStateEmoji() { return currentState.getStateEmoji(); }
    @Override
    public boolean canBreakBlocks() { return currentState.canBreakBlocks(); }
    @Override
    public boolean canShootFire() { return currentState.canShootFire(); }

    @Override
    public int getScore() { return score; }
    @Override
    public void addScore(int points) { this.score += points; }
    @Override
    public boolean hasAbility(String ability) { return abilities.contains(ability); }
    @Override
    public List<String> getAbilities() { return new ArrayList<>(abilities); }
    @Override
    public int getLives() { return lives; }
    @Override
    public void setLives(int lives) {  this.lives = lives; }
    @Override
    public Point getPosition() { return new Point(position); }
    @Override
    public void setPosition(Point position) { this.position = position; }

    public void shootFire() {
        if (currentState instanceof FireMario) {
            ((FireMario) currentState).shootFire();
        } else {
            GameFrame.getInstance().addLogMessage("❌ Cannot shoot fire in current state!", Color.RED);
        }
    }

    public Color getStateColor() {
        return currentState.getStateColor();
    }

    public int getFacingDirection() {
        return facingDirection;
    }

    @Override
    public void update() {
        // Check collisions continuously
        GameFrame.getInstance().getGamePanel().checkCollisions();
    }
}
