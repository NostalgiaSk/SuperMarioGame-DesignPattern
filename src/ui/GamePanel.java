package ui;

import core.Mario;
import interfaces.MarioComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private List<FireEffect> fireEffects;
    private List<SmokeEffect> smokeEffects;
    private List<FireballEffect> fireballEffects;
    private Random random;
    private boolean showStrengthEffect = false;
    private int strengthEffectTimer = 0;

    public GamePanel(MarioComponent mario, LevelManager levelManager) {
        this.mario = mario;
        this.levelManager = levelManager;
        this.logMessages = new ArrayList<>();
        this.fireEffects = new ArrayList<>();
        this.smokeEffects = new ArrayList<>();
        this.fireballEffects = new ArrayList<>();
        this.random = new Random();
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.WHITE);

        // Animation timer for smooth graphics
        animationTimer = new Timer(16, e -> { // ~60 FPS
            updateEffects();
            repaint();
        });
        animationTimer.start();

        // Game loop timer for continuous updates
        Timer gameLoopTimer = new Timer(100, e -> {
            mario.update();
        });
        gameLoopTimer.start();

        addLogMessage("🎮 Welcome to Super Mario Design Patterns!", Color.BLUE);
        addLogMessage("Use the control panel to interact with Mario!", Color.GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw background gradient
        Level currentLevel = levelManager.getCurrentLevel();
        GradientPaint gradient = new GradientPaint(0, 0, currentLevel.getBackgroundColor().brighter(),
                0, getHeight(), currentLevel.getBackgroundColor().darker());
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw ground
        drawGround(g2d);

        // Draw level items
        for (GameObject item : currentLevel.getItems()) {
            item.draw(g2d);
        }

        // Draw blocks
        for (Block block : currentLevel.getBlocks()) {
            block.draw(g2d);
        }

        // Draw effects (fire, smoke, etc.)
        drawEffects(g2d);

        // Draw Mario
        drawMario(g2d);

        // Draw level info
        drawLevelInfo(g2d);

        // Draw log messages
        drawLogMessages(g2d);

        g2d.dispose();
    }

    private void drawGround(Graphics2D g2d) {
        // Draw ground with texture
        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, 450, getWidth(), 50);

        // Draw ground details
        g2d.setColor(new Color(0, 100, 0));
        for (int i = 0; i < getWidth(); i += 20) {
            g2d.drawLine(i, 450, i + 10, 450);
        }

        // Draw ground shadow
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillRect(0, 450, getWidth(), 3);
    }

    private void drawMario(Graphics2D g2d) {
        Point pos = mario.getPosition();
        int drawY = pos.y - jumpOffset;

        // Get Mario's state color
        Color stateColor = Color.RED; // Default
        if (mario instanceof Mario) {
            stateColor = ((Mario) mario).getStateColor();
        }

        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(pos.x + 5, 445, 30, 10);

        // Draw Mario's body with gradient for 3D effect
        GradientPaint bodyGradient = new GradientPaint(
                pos.x, drawY, stateColor.brighter(),
                pos.x, drawY + 40, stateColor.darker()
        );
        g2d.setPaint(bodyGradient);
        g2d.fillRoundRect(pos.x, drawY, 40, 40, 10, 10);

        // Add invincibility effect with pulsing animation
        if (mario.getStateName().contains("Invincible")) {
            int alpha = (int)(100 + 55 * Math.sin(System.currentTimeMillis() / 100.0));
            g2d.setColor(new Color(255, 255, 0, alpha));
            g2d.fillRoundRect(pos.x - 5, drawY - 5, 50, 50, 15, 15);
        }

        // Draw decorators effects
        List<String> abilities = mario.getAbilities();
        for (String ability : abilities) {
            if (ability.contains("Speed Boost")) {
                int alpha = (int)(100 + 55 * Math.sin(System.currentTimeMillis() / 150.0));
                g2d.setColor(new Color(255, 0, 255, alpha));
                g2d.fillOval(pos.x - 10, drawY - 10, 60, 60);
            }
            if (ability.contains("Shield")) {
                int alpha = (int)(150 + 55 * Math.cos(System.currentTimeMillis() / 200.0));
                g2d.setColor(new Color(0, 255, 255, alpha));
                g2d.drawOval(pos.x - 8, drawY - 8, 56, 56);
                g2d.drawOval(pos.x - 6, drawY - 6, 52, 52);
            }
            if (ability.contains("Super Strength")) {
                int alpha = (int)(120 + 35 * Math.sin(System.currentTimeMillis() / 180.0));
                g2d.setColor(new Color(255, 165, 0, alpha));
                g2d.fillRoundRect(pos.x - 3, drawY - 3, 46, 46, 12, 12);
            }
        }

        // Strength effect overlay
        if (showStrengthEffect) {
            g2d.setColor(new Color(255, 215, 0, 100));
            g2d.fillRoundRect(pos.x - 8, drawY - 8, 56, 56, 15, 15);
        }

        // Draw Mario's face - direction aware
        g2d.setColor(Color.BLACK);
        int eyeOffset = ((Mario) mario).getFacingDirection() > 0 ? 0 : 8;
        g2d.fillOval(pos.x + 12 + eyeOffset, drawY + 8, 4, 4); // Left eye
        g2d.fillOval(pos.x + 24 - eyeOffset, drawY + 8, 4, 4); // Right eye
        g2d.fillOval(pos.x + 18, drawY + 15, 4, 2); // Nose
        g2d.drawArc(pos.x + 15, drawY + 20, 10, 8, 0, -180); // Mouth

        // Draw Mario's hat with gradient
        GradientPaint hatGradient = new GradientPaint(
                pos.x + 8, drawY - 5, new Color(200, 0, 0),
                pos.x + 32, drawY + 7, new Color(150, 0, 0)
        );
        g2d.setPaint(hatGradient);
        g2d.fillRoundRect(pos.x + 8, drawY - 5, 24, 12, 8, 8);

        g2d.setColor(Color.WHITE);
        g2d.fillOval(pos.x + 18, drawY - 2, 4, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawString("M", pos.x + 19, drawY + 1);

        // Draw state emoji above Mario with pulsing effect
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        int emojiYOffset = (int)(-10 + 2 * Math.sin(System.currentTimeMillis() / 200.0));
        g2d.setColor(Color.BLACK);
        g2d.drawString(mario.getStateEmoji(), pos.x + 15, drawY + emojiYOffset);

        // Draw border with highlight
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(pos.x, drawY, 40, 40, 10, 10);
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.drawRoundRect(pos.x + 1, drawY + 1, 38, 38, 8, 8);
    }

    private void drawEffects(Graphics2D g2d) {
        // Draw fire effects
        for (FireEffect effect : fireEffects) {
            effect.draw(g2d);
        }

        // Draw smoke effects
        for (SmokeEffect effect : smokeEffects) {
            effect.draw(g2d);
        }

        // Draw fireball effects
        for (FireballEffect effect : fireballEffects) {
            effect.draw(g2d);
        }
    }

    private void drawLevelInfo(Graphics2D g2d) {
        Level currentLevel = levelManager.getCurrentLevel();

        // Draw level info panel with gradient
        GradientPaint panelGradient = new GradientPaint(
                10, 10, new Color(0, 0, 0, 200),
                10, 90, new Color(0, 0, 0, 150)
        );
        g2d.setPaint(panelGradient);
        g2d.fillRoundRect(10, 10, 300, 80, 15, 15);

        // Draw border
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.drawRoundRect(10, 10, 300, 80, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString(currentLevel.getName(), 20, 30);

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(currentLevel.getDescription(), 20, 50);

        g2d.drawString("Level " + levelManager.getCurrentLevelNumber() + " of " + levelManager.getTotalLevels(), 20, 70);
    }

    private void drawLogMessages(Graphics2D g2d) {
        // Draw log panel background with gradient
        GradientPaint logGradient = new GradientPaint(
                getWidth() - 350, 10, new Color(0, 0, 0, 200),
                getWidth() - 350, 160, new Color(0, 0, 0, 150)
        );
        g2d.setPaint(logGradient);
        g2d.fillRoundRect(getWidth() - 350, 10, 330, 150, 15, 15);

        // Draw border
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.drawRoundRect(getWidth() - 350, 10, 330, 150, 15, 15);

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
            String cleanMessage = message.length() > 35 ? message.substring(0, 32) + "..." : message;

            // Add subtle pulsing effect to newest message
            if (i == logMessages.size() - 1) {
                int alpha = (int)(255 * (0.7 + 0.3 * Math.sin(System.currentTimeMillis() / 300.0)));
                g2d.setColor(new Color(255, 255, 255, alpha));
            } else {
                g2d.setColor(Color.LIGHT_GRAY);
            }

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
                        createFireEffect(marioPos.x + 20, marioPos.y, 8);
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

    // Fire effect methods
    public void createFireEffect(int x, int y, int size) {
        fireEffects.add(new FireEffect(x, y, size));
    }

    public void createSmokeEffect(int x, int y, int size) {
        smokeEffects.add(new SmokeEffect(x, y, size));
    }

    public void createFireballEffect(int x, int y) {
        fireballEffects.add(new FireballEffect(x, y));
    }

    public void showStrengthEffect() {
        showStrengthEffect = true;
        strengthEffectTimer = 20; // Show for 20 frames
    }

    public void playFireTransformationSound() {
        // Sound implementation would go here
        System.out.println("Playing fire transformation sound");
    }

    public void playPowerUpSound() {
        // Sound implementation would go here
        System.out.println("Playing power-up sound");
    }

    public void playPowerDownSound() {
        // Sound implementation would go here
        System.out.println("Playing power-down sound");
    }

    private void updateEffects() {
        // Update fire effects
        fireEffects.removeIf(FireEffect::update);

        // Update smoke effects
        smokeEffects.removeIf(SmokeEffect::update);

        // Update fireball effects
        fireballEffects.removeIf(FireballEffect::update);

        // Update strength effect
        if (showStrengthEffect) {
            strengthEffectTimer--;
            if (strengthEffectTimer <= 0) {
                showStrengthEffect = false;
            }
        }
    }

    // Inner classes for visual effects
    private class FireEffect {
        int x, y;
        int size;
        int life;
        int maxLife;

        FireEffect(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.maxLife = 30 + random.nextInt(20);
            this.life = maxLife;
        }

        boolean update() {
            life--;
            y -= 2;
            x += random.nextInt(5) - 2;
            return life <= 0;
        }

        void draw(Graphics2D g2d) {
            float alpha = (float) life / maxLife;
            int currentSize = (int)(size * (0.5 + 0.5 * alpha));

            // Draw multiple layers for fire effect
            g2d.setColor(new Color(255, 255, 0, (int)(100 * alpha)));
            g2d.fillOval(x - currentSize/2, y - currentSize/2, currentSize, currentSize);

            g2d.setColor(new Color(255, 165, 0, (int)(150 * alpha)));
            g2d.fillOval(x - currentSize/3, y - currentSize/3, currentSize*2/3, currentSize*2/3);

            g2d.setColor(new Color(255, 69, 0, (int)(200 * alpha)));
            g2d.fillOval(x - currentSize/4, y - currentSize/4, currentSize/2, currentSize/2);
        }
    }

    private class SmokeEffect {
        int x, y;
        int size;
        int life;
        int maxLife;

        SmokeEffect(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.maxLife = 40 + random.nextInt(20);
            this.life = maxLife;
        }

        boolean update() {
            life--;
            y -= 1;
            x += random.nextInt(3) - 1;
            return life <= 0;
        }

        void draw(Graphics2D g2d) {
            float alpha = (float) life / maxLife;
            int currentSize = (int)(size * alpha);

            g2d.setColor(new Color(100, 100, 100, (int)(100 * alpha)));
            g2d.fillOval(x - currentSize/2, y - currentSize/2, currentSize, currentSize);
        }
    }

    private class FireballEffect {
        int x, y;
        int life;

        FireballEffect(int x, int y) {
            this.x = x;
            this.y = y;
            this.life = 30;
        }

        boolean update() {
            life--;
            x += 8;
            return life <= 0 || x > getWidth();
        }

        void draw(Graphics2D g2d) {
            float alpha = (float) life / 30;
            int size = 12;

            g2d.setColor(new Color(255, 255, 0, (int)(255 * alpha)));
            g2d.fillOval(x - size/2, y - size/2, size, size);

            g2d.setColor(new Color(255, 69, 0, (int)(200 * alpha)));
            g2d.fillOval(x - size/3, y - size/3, size*2/3, size*2/3);

            // Draw trail
            for (int i = 1; i <= 3; i++) {
                int trailAlpha = (int)(100 * alpha / i);
                int trailSize = size - i * 2;
                g2d.setColor(new Color(255, 165, 0, trailAlpha));
                g2d.fillOval(x - i * 4 - trailSize/2, y - trailSize/2, trailSize, trailSize);
            }
        }
    }
}