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
    private JLabel scoreLabel, livesLabel, stateLabel, abilitiesLabel, levelLabel;
    private JProgressBar invincibilityBar;
    private JPanel logPanel;
    private JTextArea logArea;

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
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(45, 45, 45));

        // Create game panel
        gamePanel = new GamePanel(mario, levelManager);
        add(gamePanel, BorderLayout.CENTER);

        // Create control panel
        createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        // Create status panel
        createStatusPanel();
        add(statusPanel, BorderLayout.NORTH);

        // Create log panel
        createLogPanel();
        add(logPanel, BorderLayout.EAST);

        // Set window properties
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        // Add keyboard controls
        addKeyBindings();

        updateDisplay();
    }

    private void createControlPanel() {
        controlPanel = new JPanel(new GridLayout(3, 5, 8, 8));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        "Game Controls",
                        javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        new Color(220, 220, 220)
                ),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));
        controlPanel.setBackground(new Color(60, 60, 60));
        controlPanel.setForeground(Color.WHITE);

        // Action buttons
        addStyledButton("🦘 Jump", e -> { mario.jump(); gamePanel.checkCollisions(); }, new Color(65, 105, 225));
        addStyledButton("⬅️ Move Left", e -> { mario.move(-1); gamePanel.checkCollisions(); }, new Color(70, 130, 180));
        addStyledButton("➡️ Move Right", e -> { mario.move(1); gamePanel.checkCollisions(); }, new Color(70, 130, 180));
        addStyledButton("🔥 Fire", e -> shootFire(), new Color(220, 20, 60));
        addStyledButton("🧱 Break", e -> breakBlock(), new Color(139, 69, 19));

        addStyledButton("🍄 Mushroom", e -> { mario.collectMushroom(); gamePanel.checkCollisions(); }, new Color(255, 99, 71));
        addStyledButton("🌸 Fire Flower", e -> { mario.collectFireFlower(); gamePanel.checkCollisions(); }, new Color(255, 69, 0));
        addStyledButton("⭐ Star", e -> { mario.collectStar(); gamePanel.checkCollisions(); }, new Color(255, 215, 0));
        addStyledButton("💥 Damage", e -> mario.takeDamage(), new Color(178, 34, 34));
        addStyledButton("🚀 Speed", e -> addSpeedBoost(), new Color(30, 144, 255));

        addStyledButton("⬆️ DblJump", e -> addDoubleJump(), new Color(123, 104, 238));
        addStyledButton("🛡️ Shield", e -> addShield(), new Color(0, 191, 255));
        addStyledButton("💪 Strength", e -> addSuperStrength(), new Color(218, 165, 32));
        addStyledButton("🎭 Demo", e -> demonstratePatterns(), new Color(138, 43, 226));
        addStyledButton("◀️ Prev Level", e -> previousLevel(), new Color(72, 61, 139));
        addStyledButton("▶️ Next Level", e -> nextLevel(), new Color(72, 61, 139));
    }

    private void createStatusPanel() {
        statusPanel = new JPanel(new BorderLayout(10, 5));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        "Mario Status",
                        javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        new Color(220, 220, 220)
                ),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));
        statusPanel.setBackground(new Color(50, 50, 50));
        statusPanel.setForeground(Color.WHITE);

        // Left side - Score and Lives
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);
        scoreLabel = createStatusLabel("Score: 0", new Color(50, 205, 50));
        livesLabel = createStatusLabel("Lives: 3", new Color(255, 99, 71));
        leftPanel.add(scoreLabel);
        leftPanel.add(livesLabel);

        // Center - State and Abilities
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        centerPanel.setOpaque(false);
        stateLabel = createStatusLabel("State: Small Mario", new Color(135, 206, 250));
        abilitiesLabel = createStatusLabel("Abilities: None", new Color(255, 215, 0));
        levelLabel = createStatusLabel("Level: 1-1", new Color(147, 112, 219));
        centerPanel.add(stateLabel);
        centerPanel.add(abilitiesLabel);
        centerPanel.add(levelLabel);

        // Right side - Invincibility timer
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        rightPanel.setOpaque(false);
        invincibilityBar = new JProgressBar(0, 8);
        invincibilityBar.setStringPainted(true);
        invincibilityBar.setString("Invincibility");
        invincibilityBar.setForeground(new Color(255, 105, 180));
        invincibilityBar.setBackground(new Color(50, 50, 50));
        invincibilityBar.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        invincibilityBar.setFont(new Font("Arial", Font.BOLD, 10));
        invincibilityBar.setVisible(false);
        rightPanel.add(invincibilityBar);

        statusPanel.add(leftPanel, BorderLayout.WEST);
        statusPanel.add(centerPanel, BorderLayout.CENTER);
        statusPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void createLogPanel() {
        logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        "Game Log",
                        javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14),
                        new Color(220, 220, 220)
                ),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));
        logPanel.setBackground(new Color(50, 50, 50));
        logPanel.setPreferredSize(new Dimension(250, 0));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));

        logPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JLabel createStatusLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(color);
        return label;
    }

    private void addStyledButton(String text, ActionListener action, Color color) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusable(false);
        button.setBackground(color);
        button.setForeground(color);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

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
                    addLogMessage("📖 Decorator Pattern: Adding Speed Boost...", Color.PINK);
                    if (!mario.hasAbility("Speed Boost")) {
                        mario = new SpeedBoostDecorator(mario);
                    }
                    break;
                case 3:
                    addLogMessage("📖 Decorator Pattern: Adding Shield...", Color.PINK);
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
        // Add to game panel if needed
        gamePanel.addLogMessage(message, color);

        // Also add to the log area
        SwingUtilities.invokeLater(() -> {
            logArea.setForeground(color);
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void updateDisplay() {
        // Update status labels
        scoreLabel.setText("Score: " + mario.getScore());
        livesLabel.setText("Lives: " + mario.getLives());
        stateLabel.setText("State: " + mario.getStateName());
        levelLabel.setText("Level: " + levelManager.getCurrentLevel().getName());

        List<String> abilities = mario.getAbilities();
        if (abilities.isEmpty()) {
            abilitiesLabel.setText("Abilities: None");
        } else {
            String abilitiesText = "Abilities: " + String.join(", ", abilities);
            if (abilitiesText.length() > 40) {
                abilitiesText = abilitiesText.substring(0, 37) + "...";
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

        // Clear log
        logArea.setText("");
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