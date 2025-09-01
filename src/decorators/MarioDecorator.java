package decorators;

import interfaces.MarioComponent;

import java.awt.Point;
import java.util.List;

/**
 * Base Decorator class for Decorator Pattern
 */
public abstract class MarioDecorator implements MarioComponent {
    protected MarioComponent decoratedMario;

    public MarioDecorator(MarioComponent mario) {
        this.decoratedMario = mario;
    }

    // Default delegation to wrapped mario
    @Override
    public void jump() {
        decoratedMario.jump();
    }

    @Override
    public void move() {
        decoratedMario.move();
    }

    @Override
    public void takeDamage() {
        decoratedMario.takeDamage();
    }

    @Override
    public void collectMushroom() {
        decoratedMario.collectMushroom();
    }

    @Override
    public void collectFireFlower() {
        decoratedMario.collectFireFlower();
    }

    @Override
    public void collectStar() {
        decoratedMario.collectStar();
    }

    @Override
    public String getStateName() {
        return decoratedMario.getStateName();
    }

    @Override
    public String getStateEmoji() {
        return decoratedMario.getStateEmoji();
    }

    @Override
    public boolean canBreakBlocks() {
        return decoratedMario.canBreakBlocks();
    }

    @Override
    public boolean canShootFire() {
        return decoratedMario.canShootFire();
    }

    @Override
    public int getScore() {
        return decoratedMario.getScore();
    }

    @Override
    public void addScore(int points) {
        decoratedMario.addScore(points);
    }

    @Override
    public boolean hasAbility(String ability) {
        return decoratedMario.hasAbility(ability);
    }

    @Override
    public List<String> getAbilities() {
        return decoratedMario.getAbilities();
    }

    @Override
    public int getLives() {
        return decoratedMario.getLives();
    }

    @Override
    public Point getPosition() {
        return decoratedMario.getPosition();
    }

    @Override
    public void setPosition(Point position) {
        decoratedMario.setPosition(position);
    }
}