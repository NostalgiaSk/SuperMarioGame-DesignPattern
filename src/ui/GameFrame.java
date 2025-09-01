package ui;

import core.Mario;
import decorators.DoubleJumpDecorator;
import decorators.ShieldDecorator;
import decorators.SpeedBoostDecorator;
import decorators.SuperStrengthDecorator;
import interfaces.MarioComponent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main game window that contains all UI components
 */
public class GameFrame extends JFrame {
    private static GameFrame instance;
    private MarioComponent mario;
    private LevelManager levelManager;
    private GamePanel gamePanel;
    private JPanel controlPanel;
    private JPanel statusPanel;
    private JLabel scoreLabel, livesLabel, stateLabel, abilitiesLabel;
    private JProgressBar invincibilityBar;

    private GameFrame() {
        mario = new Mario();
        levelManager = new LevelManager();
        initializeGUI();
    }

    public static GameFrame getInstance() {
        if (instance == null) {
            instance = new GameFrame();
        }
        return instance;
    }

    private void initializeGUI() {
        setTitle("🍄 Super Mario Design Patterns - State & Decorator Demo 🍄");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create game panel
        gamePanel = new GamePanel(mario, levelManager);
        add(gamePanel, BorderLayout.CENTER);

        // Create control panel
        createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        // Create status panel
        createStatusPanel();
        add(statusPanel, BorderLayout.NORTH);

        // Set window properties
        pack();
        setLocationRelativeTo(null);
        setResizable(true);

        // Add keyboard controls
        addKeyBindings();

        updateDisplay();
    }

    private void createControlPanel() {
        controlPanel = new JPanel(new GridLayout(3, 5, 5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Game Controls"));
        controlPanel.setBackground(new Color(240, 240, 240));

        // Action buttons
        addButton("🦘 Jump", e -> { mario.jump(); gamePanel.checkCollisions(); });
        addButton("⬅️ Move Left", e -> { mario.move(-1); gamePanel.checkCollisions(); });
        addButton("➡️ Move Right", e -> { mario.move(1); gamePanel.checkCollisions(); });
        addButton("🔥 Fire", e -> shootFire());
        addButton("🧱 Break", e -> breakBlock());

        addButton("🍄 Mushroom", e -> { mario.collectMushroom(); gamePanel.checkCollisions(); });
        addButton("🌸 Fire Flower", e -> { mario.collectFireFlower(); gamePanel.checkCollisions(); });
        addButton("⭐ Star", e -> { mario.collectStar(); gamePanel.checkCollisions(); });
        addButton("💥 Damage", e -> mario.takeDamage());
        addButton("🚀 Speed", e -> addSpeedBoost());

        addButton("⬆️ DblJump", e -> addDoubleJump());
        addButton("🛡️ Shield", e -> addShield());
        addButton("💪 Strength", e -> addSuperStrength());
        addButton("🎭 Demo", e -> demonstratePatterns());
        addButton("◀️ Prev Level", e -> previousLevel());
        addButton("▶️ Next Level", e -> nextLevel());
    }

    private void createStatusPanel() {
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder("Mario Status"));
        statusPanel.setBackground(new Color(250, 250, 250));

        // Left side - Score and Lives
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scoreLabel = new JLabel("Score: 0");
        livesLabel = new JLabel("Lives: 3");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        livesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(scoreLabel);
        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(livesLabel);

        // Center - State and Abilities
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        stateLabel = new JLabel("State: Small Mario");
        abilitiesLabel = new JLabel("Abilities: None");
        stateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        abilitiesLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        centerPanel.add(stateLabel);
        centerPanel.add(Box.createHorizontalStrut(10));
        centerPanel.add(abilitiesLabel);

        // Right side - Invincibility timer
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        invincibilityBar = new JProgressBar(0, 8);
        invincibilityBar.setStringPainted(true);
        invincibilityBar.setString("Invincibility");
        invincibilityBar.setVisible(false);
        rightPanel.add(invincibilityBar);

        statusPanel.add(leftPanel, BorderLayout.WEST);
        statusPanel.add(centerPanel, BorderLayout.CENTER);
        statusPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void addButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setFont(new Font("Arial", Font.PLAIN, 10));
        button.setFocusable(false);
        controlPanel.add(button);
    }

    private void addKeyBindings() {
        // Add keyboard shortcuts
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        // WASD controls
        inputMap.put(KeyStroke.getKeyStroke('w'), "jump");
        inputMap.put(KeyStroke.getKeyStroke('a'), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke('d'), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke('s'), "fire");
        inputMap.put(KeyStroke.getKeyStroke('b'), "break");

        // Number keys for items
        inputMap.put(KeyStroke.getKeyStroke('1'), "mushroom");
        inputMap.put(KeyStroke.getKeyStroke('2'), "flower");
        inputMap.put(KeyStroke.getKeyStroke('3'), "star");
        inputMap.put(KeyStroke.getKeyStroke('4'), "damage");

        // Arrow keys for movement
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");

        // Space for jump
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "jump");

        actionMap.put("jump", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mario.jump();
                gamePanel.checkCollisions();
            }
        });

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mario.move(-1);
                gamePanel.checkCollisions();
            }
        });

        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mario.move(1);
                gamePanel.checkCollisions();
            }
        });

        actionMap.put("fire", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shootFire();
            }
        });

        actionMap.put("break", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                breakBlock();
            }
        });

        actionMap.put("mushroom", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mario.collectMushroom();
                gamePanel.checkCollisions();
            }
        });

        actionMap.put("flower", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mario.collectFireFlower();
                gamePanel.checkCollisions();
            }
        });

        actionMap.put("star", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mario.collectStar();
                gamePanel.checkCollisions();
            }
        });

        actionMap.put("damage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mario.takeDamage();
            }
        });
    }

    // Action methods
    private void shootFire() {
        if (mario.canShootFire()) {
            if (mario instanceof Mario) {
                ((Mario) mario).shootFire();
            } else {
                addLogMessage("🔥💥 Shooting fireball through decorators!", Color.MAGENTA);
                mario.addScore(50);
                updateDisplay();
            }
        } else {
            addLogMessage("❌ Cannot shoot fire in current state!", Color.RED);
        }
    }

    private void breakBlock() {
        if (mario.canBreakBlocks()) {
            addLogMessage("🧱💥 Breaking block!", Color.ORANGE);
            mario.addScore(25);
            if (mario.hasAbility("Super Strength")) {
                addLogMessage("💪 SUPER STRENGTH: Breaking extra blocks!", Color.MAGENTA);
                mario.addScore(75);
            }
            updateDisplay();
        } else {
            addLogMessage("❌ Cannot break blocks in current state!", Color.RED);
        }
    }

    private void addSpeedBoost() {
        if (!mario.hasAbility("Speed Boost")) {
            mario = new SpeedBoostDecorator(mario);
            addLogMessage("🚀 Speed Boost activated!", Color.MAGENTA);
            updateDisplay();
        } else {
            addLogMessage("❌ Speed Boost already active!", Color.RED);
        }
    }

    private void addDoubleJump() {
        if (!mario.hasAbility("Double Jump")) {
            mario = new DoubleJumpDecorator(mario);
            addLogMessage("⬆️ Double Jump activated!", Color.MAGENTA);
            updateDisplay();
        } else {
            addLogMessage("❌ Double Jump already active!", Color.RED);
        }
    }

    private void addShield() {
        if (!mario.hasAbility("Shield")) {
            mario = new ShieldDecorator(mario);
            addLogMessage("🛡️ Shield activated!", Color.CYAN);
            updateDisplay();
        } else {
            addLogMessage("❌ Shield already active!", Color.RED);
        }
    }

    private void addSuperStrength() {
        if (!mario.hasAbility("Super Strength")) {
            mario = new SuperStrengthDecorator(mario);
            addLogMessage("💪 Super Strength activated!", Color.ORANGE);
            updateDisplay();
        } else {
            addLogMessage("❌ Super Strength already active!", Color.RED);
        }
    }

    private void nextLevel() {
        if (!levelManager.isLastLevel()) {
            levelManager.nextLevel();
            addLogMessage("▶️ Advanced to " + levelManager.getCurrentLevel().getName(), Color.GREEN);
            updateDisplay();
        } else {
            addLogMessage("🎉 You've completed all levels!", Color.ORANGE);
        }
    }

    private void previousLevel() {
        if (levelManager.getCurrentLevelNumber() > 1) {
            levelManager.previousLevel();
            addLogMessage("◀️ Returned to " + levelManager.getCurrentLevel().getName(), Color.BLUE);
            updateDisplay();
        } else {
            addLogMessage("❌ Already at first level!", Color.RED);
        }
    }

    private void demonstratePatterns() {
        addLogMessage("🎭 Starting Design Patterns Demo!", Color.CYAN);

        Timer demoTimer = new Timer(2000, null);
        final int[] step = {0};

        demoTimer.addActionListener(e -> {
            switch (step[0]) {
                case 0:
                    addLogMessage("📖 State Pattern: Collecting mushroom...", Color.BLUE);
                    mario.collectMushroom();
                    break;
                case 1:
                    addLogMessage("📖 State Pattern: Collecting fire flower...", Color.BLUE);
                    mario.collectFireFlower();
                    break;
                case 2:
                    addLogMessage("📖 Decorator Pattern: Adding Speed Boost...", Color.pink);
                    if (!mario.hasAbility("Speed Boost")) {
                        mario = new SpeedBoostDecorator(mario);
                    }
                    break;
                case 3:
                    addLogMessage("📖 Decorator Pattern: Adding Shield...", Color.pink);
                    if (!mario.hasAbility("Shield")) {
                        mario = new ShieldDecorator(mario);
                    }
                    break;
                case 4:
                    addLogMessage("📖 Integration: Testing enhanced abilities...", Color.GREEN);
                    mario.jump();
                    gamePanel.performJumpAnimation();
                    break;
                case 5:
                    addLogMessage("📖 Integration: Testing fire shooting...", Color.GREEN);
                    shootFire();
                    break;
                case 6:
                    addLogMessage("🎉 Demo Complete! All patterns working together!", Color.ORANGE);
                    demoTimer.stop();
                    break;
            }
            step[0]++;
            updateDisplay();
        });

        demoTimer.start();
    }

    public void addLogMessage(String message, Color color) {
        gamePanel.addLogMessage(message, color);
    }

    public void updateDisplay() {
        // Update status labels
        scoreLabel.setText("Score: " + mario.getScore());
        livesLabel.setText("Lives: " + mario.getLives());
        stateLabel.setText("State: " + mario.getStateName());

        List<String> abilities = mario.getAbilities();
        if (abilities.isEmpty()) {
            abilitiesLabel.setText("Abilities: None");
        } else {
            String abilitiesText = "Abilities: " + String.join(", ", abilities);
            if (abilitiesText.length() > 60) {
                abilitiesText = abilitiesText.substring(0, 57) + "...";
            }
            abilitiesLabel.setText(abilitiesText);
        }

        // Update invincibility bar
        if (mario.getStateName().contains("Invincible")) {
            invincibilityBar.setVisible(true);
        } else {
            invincibilityBar.setVisible(false);
        }

        // Color code the status based on Mario's state
        if (mario.getStateName().contains("Small")) {
            stateLabel.setForeground(Color.RED);
        } else if (mario.getStateName().contains("Big")) {
            stateLabel.setForeground(new Color(255, 165, 0));
        } else if (mario.getStateName().contains("Fire")) {
            stateLabel.setForeground(Color.RED);
        } else if (mario.getStateName().contains("Invincible")) {
            stateLabel.setForeground(Color.MAGENTA);
        }

        repaint();
    }

    public void updateInvincibilityTimer(int timer) {
        if (timer > 0) {
            invincibilityBar.setValue(timer);
            invincibilityBar.setString("Invincibility: " + timer + "s");
        }
        gamePanel.updateInvincibilityTimer(timer);
    }

    public void removeDecorator(String decoratorType) {
        // This is a simplified approach - in a real implementation,
        // you'd need a more sophisticated way to unwrap decorators
        updateDisplay();
    }

    public void showGameOver() {
        JOptionPane.showMessageDialog(this,
                "💀 GAME OVER! 💀\n\nFinal Score: " + mario.getScore() +
                        "\n\nThanks for playing Super Mario Design Patterns!",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);

        // Reset the game
        mario = new Mario();
        levelManager = new LevelManager();
        gamePanel = new GamePanel(mario, levelManager);
        remove(getContentPane().getComponent(0));
        add(gamePanel, BorderLayout.CENTER);
        updateDisplay();
        revalidate();
    }

    public void resetGame() {
        mario = new Mario();
        levelManager.resetCurrentLevel();
        addLogMessage("🔄 Game reset! Mario is back to Small Mario state.", Color.BLUE);
        updateDisplay();
    }

    // Make the frame accessible to other classes
    public MarioComponent getMario() { return mario; }
    public void setMario(MarioComponent mario) { this.mario = mario; }
    public GamePanel getGamePanel() { return gamePanel; }
}
