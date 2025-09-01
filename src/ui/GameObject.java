package ui;

import java.awt.*;

/**
 * Base class for all game objects
 */
public abstract class GameObject {
    protected Point position;
    protected int width, height;
    protected Color color;
    protected boolean collected;

    public GameObject(Point position, int width, int height, Color color) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.color = color;
        this.collected = false;
    }

    public boolean checkCollision(Point marioPos) {
        Rectangle marioRect = new Rectangle(marioPos.x, marioPos.y, 40, 40);
        Rectangle itemRect = new Rectangle(position.x, position.y, width, height);
        return marioRect.intersects(itemRect) && !collected;
    }

    public abstract void draw(Graphics2D g2d);

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public Point getPosition() {
        return position;
    }
}

/**
 * Mushroom power-up
 */
class Mushroom extends GameObject {
    public Mushroom(Point position) {
        super(position, 30, 30, new Color(255, 100, 100));
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!collected) {
            // Draw mushroom
            g2d.setColor(new Color(255, 200, 200));
            g2d.fillOval(position.x, position.y, width, height);
            g2d.setColor(Color.RED);
            g2d.fillOval(position.x + 5, position.y + 5, width - 10, height - 15);
            g2d.setColor(Color.WHITE);
            g2d.fillOval(position.x + 8, position.y + 8, 6, 6);
            g2d.fillOval(position.x + 16, position.y + 8, 6, 6);

            // Draw label
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("🍄", position.x + 8, position.y - 5);
        }
    }
}

/**
 * Fire Flower power-up
 */
class FireFlower extends GameObject {
    public FireFlower(Point position) {
        super(position, 30, 30, new Color(255, 150, 50));
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!collected) {
            // Draw fire flower
            g2d.setColor(Color.ORANGE);
            g2d.fillOval(position.x + 5, position.y, 20, 20);
            g2d.setColor(Color.RED);
            g2d.fillOval(position.x + 8, position.y + 3, 14, 14);
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(position.x + 11, position.y + 6, 8, 8);

            // Draw stem
            g2d.setColor(Color.GREEN);
            g2d.fillRect(position.x + 14, position.y + 15, 2, 10);

            // Draw label
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("🌸", position.x + 8, position.y - 5);
        }
    }
}

/**
 * Star power-up
 */
class Star extends GameObject {
    private int animationFrame = 0;

    public Star(Point position) {
        super(position, 30, 30, Color.YELLOW);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!collected) {
            // Animated star
            animationFrame++;
            int pulse = (int)(Math.sin(animationFrame * 0.3) * 5);

            g2d.setColor(Color.YELLOW);
            int[] xPoints = {position.x + 15, position.x + 18, position.x + 25, position.x + 20, position.x + 22,
                    position.x + 15, position.x + 8, position.x + 10, position.x + 5, position.x + 12};
            int[] yPoints = {position.y + pulse, position.y + 8 + pulse, position.y + 8 + pulse, position.y + 15 + pulse,
                    position.y + 25 + pulse, position.y + 20 + pulse, position.y + 25 + pulse, position.y + 15 + pulse,
                    position.y + 8 + pulse, position.y + 8 + pulse};
            g2d.fillPolygon(xPoints, yPoints, 10);

            g2d.setColor(Color.ORANGE);
            g2d.drawPolygon(xPoints, yPoints, 10);

            // Draw label
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("⭐", position.x + 8, position.y - 5);
        }
    }
}

/**
 * Breakable block
 */
class Block extends GameObject {
    boolean broken = false;

    public Block(Point position) {
        super(position, 40, 40, new Color(139, 69, 19));
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (!broken) {
            // Draw brick texture
            g2d.setColor(new Color(139, 69, 19));
            g2d.fillRect(position.x, position.y, width, height);

            // Add brick lines
            g2d.setColor(new Color(101, 67, 33));
            for (int i = 0; i < height; i += 10) {
                g2d.drawLine(position.x, position.y + i, position.x + width, position.y + i);
            }
            for (int i = 0; i < width; i += 20) {
                g2d.drawLine(position.x + i, position.y, position.x + i, position.y + height);
            }

            g2d.setColor(Color.BLACK);
            g2d.drawRect(position.x, position.y, width, height);

            // Draw label
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("🧱", position.x + 12, position.y - 5);
        }
    }

    public void breakBlock() {
        broken = true;
    }

    public boolean isBroken() {
        return broken;
    }
}