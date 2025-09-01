package states;
import core.Mario;

import java.awt.Color;

/**
 * Abstract State class for State Pattern
 * Defines the interface for different Mario states
 */
public abstract class MarioState {
    protected Mario mario;

    public MarioState(Mario mario) {
        this.mario = mario;
    }

    public abstract void jump();
    public abstract void move(int direction);

    public abstract void move();

    public abstract void takeDamage();
    public abstract void collectMushroom();
    public abstract void collectFireFlower();
    public abstract void collectStar();
    public abstract String getStateName();
    public abstract String getStateEmoji();
    public abstract boolean canBreakBlocks();
    public abstract boolean canShootFire();
    public abstract Color getStateColor();
}