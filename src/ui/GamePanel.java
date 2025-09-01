package ui;

import core.Mario;
import interfaces.MarioComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game panel that handles all visual rendering and animations
 */
public class GamePanel extends JPanel {
    private MarioComponent mario;
    private LevelManager levelManager;
    private List<String> logMessages;
    private Timer animationTimer;
    private boolean isJumping = false;
    private int jumpOffset = 0;
    private int invincibilityTimer = 0;

    public GamePanel(MarioComponent mario, LevelManager levelManager) {
        this.mario = mario;
        this.levelManager = levelManager;
        this.logMessages = new ArrayList<>();
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.WHITE);

        // Animation timer for smooth graphics
        animationTimer = new Timer(50, e -> repaint());
        animationTimer.start();

        addLogMessage("🎮 Welcome to Super Mario Design Patterns!", Color.BLUE);
        addLogMessage("Use the control panel to interact with Mario!", Color.GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background gradient
        Level currentLevel = levelManager.getCurrentLevel();
        GradientPaint gradient = new GradientPaint(0, 0, currentLevel.getBackgroundColor().brighter(),
                0, getHeight(), currentLevel.getBackgroundColor().darker());
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw ground
        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, 450, getWidth(), 50);
        g2d.setColor(new Color(0, 100, 0));
        g2d.drawLine(0, 450, getWidth(), 450);

        // Draw level items
        for (GameObject item : currentLevel.getItems()) {
            item.draw(g2d);
        }

        // Draw blocks
        for (Block block : currentLevel.getBlocks()) {
            block.draw(g2d);
        }

        // Draw Mario
        drawMario(g2d);

        // Draw level info
        drawLevelInfo(g2d);

        // Draw log messages
        drawLogMessages(g2d);

        g2d.dispose();
    }

    private void drawMario(Graphics2D g2d) {
        Point pos = mario.getPosition();
        int drawY = pos.y - jumpOffset;

        // Get Mario's state color
        Color stateColor = Color.RED; // Default
        if (mario instanceof Mario) {
            stateColor = ((Mario) mario).getStateColor();
        }

        // Draw Mario's body
        g2d.setColor(stateColor);
        g2d.fillRoundRect(pos.x, drawY, 40, 40, 10, 10);

        // Add invincibility effect
        if (mario.getStateName().contains("Invincible")) {
            g2d.setColor(new Color(255, 255, 0, 100));
            g2d.fillRoundRect(pos.x - 5, drawY - 5, 50, 50, 15, 15);
        }

        // Draw decorators effects
        List<String> abilities = mario.getAbilities();
        for (String ability : abilities) {
            if (ability.contains("Speed Boost")) {
                g2d.setColor(new Color(255, 0, 255, 100));
                g2d.fillOval(pos.x - 10, drawY - 10, 60, 60);
            }
            if (ability.contains("Shield")) {
                g2d.setColor(new Color(0, 255, 255, 150));
                g2d.drawOval(pos.x - 8, drawY - 8, 56, 56);
                g2d.drawOval(pos.x - 6, drawY - 6, 52, 52);
            }
            if (ability.contains("Super Strength")) {
                g2d.setColor(new Color(255, 165, 0, 120));
                g2d.fillRoundRect(pos.x - 3, drawY - 3, 46, 46, 12, 12);
            }
        }

        // Draw Mario's face
        g2d.setColor(Color.BLACK);
        g2d.fillOval(pos.x + 12, drawY + 8, 4, 4); // Left eye
        g2d.fillOval(pos.x + 24, drawY + 8, 4, 4); // Right eye
        g2d.fillOval(pos.x + 18, drawY + 15, 4, 2); // Nose
        g2d.drawArc(pos.x + 15, drawY + 20, 10, 8, 0, -180); // Mouth

        // Draw Mario's hat
        g2d.setColor(Color.RED);
        g2d.fillRoundRect(pos.x + 8, drawY - 5, 24, 12, 8, 8);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(pos.x + 18, drawY - 2, 4, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawString("M", pos.x + 19, drawY + 1);

        // Draw state emoji above Mario
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.BLACK);
        g2d.drawString(mario.getStateEmoji(), pos.x + 15, drawY - 10);

        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(pos.x, drawY, 40, 40, 10, 10);
    }

    private void drawLevelInfo(Graphics2D g2d) {
        Level currentLevel = levelManager.getCurrentLevel();

        // Draw level info panel
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(10, 10, 300, 80, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString(currentLevel.getName(), 20, 30);

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(currentLevel.getDescription(), 20, 50);

        g2d.drawString("Level " + levelManager.getCurrentLevelNumber() + " of " + levelManager.getTotalLevels(), 20, 70);
    }

    private void drawLogMessages(Graphics2D g2d) {
        // Draw log panel background
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(getWidth() - 350, 10, 330, 150, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Game Log", getWidth() - 340, 30);

        // Draw recent log messages
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
        int y = 45;
        int maxMessages = 8;
        int start = Math.max(0, logMessages.size() - maxMessages);

        for (int i = start; i < logMessages.size() && y < 150; i++) {
            String message = logMessages.get(i);

            // Remove emojis for cleaner display in some cases
            String cleanMessage = message.length() > 35 ? message.substring(0, 32) + "..." : message;

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawString(cleanMessage, getWidth() - 340, y);
            y += 12;
        }
    }

    public void addLogMessage(String message, Color color) {
        logMessages.add(message);
        if (logMessages.size() > 50) {
            logMessages.remove(0);
        }
        repaint();
    }

    public void performJumpAnimation() {
        if (!isJumping) {
            isJumping = true;
            Timer jumpTimer = new Timer(50, null);
            jumpTimer.addActionListener(new ActionListener() {
                private int frame = 0;
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame++;
                    if (frame <= 10) {
                        jumpOffset = frame * 3; // Going up
                    } else if (frame <= 20) {
                        jumpOffset = (20 - frame) * 3; // Going down
                    } else {
                        jumpOffset = 0;
                        isJumping = false;
                        jumpTimer.stop();
                    }
                    repaint();
                }
            });
            jumpTimer.start();
        }
    }

    public void checkCollisions() {
        Level currentLevel = levelManager.getCurrentLevel();
        Point marioPos = mario.getPosition();

        // Check item collisions
        for (GameObject item : currentLevel.getItems()) {
            if (item.checkCollision(marioPos)) {
                item.collect();
                if (item instanceof Mushroom) {
                    mario.collectMushroom();
                } else if (item instanceof FireFlower) {
                    mario.collectFireFlower();
                } else if (item instanceof Star) {
                    mario.collectStar();
                }
            }
        }

        // Check block collisions for breaking
        for (Block block : currentLevel.getBlocks()) {
            if (block.checkCollision(marioPos) && !block.isBroken()) {
                if (mario.canBreakBlocks()) {
                    block.breakBlock();
                    addLogMessage("🧱💥 Block broken!", Color.ORANGE);
                    mario.addScore(25);
                    if (mario.hasAbility("Super Strength")) {
                        addLogMessage("💪 SUPER STRENGTH bonus!", Color.MAGENTA);
                        mario.addScore(75);
                    }
                }
            }
        }

        repaint();
    }

    public void updateInvincibilityTimer(int timer) {
        this.invincibilityTimer = timer;
        repaint();
    }
}