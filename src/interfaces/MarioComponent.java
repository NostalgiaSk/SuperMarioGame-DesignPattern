package interfaces;

import java.awt.Point;
import java.util.List;

/**
 * Component interface for Decorator Pattern
 * Defines all Mario behaviors that can be enhanced
 */
public interface MarioComponent {
    void jump();

    void takeDamage();
    void collectMushroom();
    void collectFireFlower();
    void collectStar();
    String getStateName();
    boolean canBreakBlocks();
    boolean canShootFire();
    int getScore();
    void addScore(int points);
    boolean hasAbility(String ability);
    List<String> getAbilities();
    int getLives();

    void setLives(int lives);

    Point getPosition();
    void setPosition(Point position);
    String getStateEmoji();
    void update();
    void move(int direction);
}