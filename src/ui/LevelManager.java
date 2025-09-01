package ui;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Individual level class
 */
class Level {
    private List<GameObject> items;
    private List<Block> blocks;
    private String name;
    private String description;
    private Color backgroundColor;

    public Level(String name, String description, Color backgroundColor) {
        this.name = name;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.items = new ArrayList<>();
        this.blocks = new ArrayList<>();
    }

    public void addItem(GameObject item) {
        items.add(item);
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public List<GameObject> getItems() { return items; }
    public List<Block> getBlocks() { return blocks; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Color getBackgroundColor() { return backgroundColor; }

    public void reset() {
        for (GameObject item : items) {
            item.collected = false;
        }
        for (Block block : blocks) {
            block.broken = false;
        }
    }
}

class LevelManager {
    private List<Level> levels;
    private int currentLevelIndex;

    public LevelManager() {
        levels = new ArrayList<>();
        currentLevelIndex = 0;
        createLevels();
    }

    private void createLevels() {
        // Level 1: Tutorial
        Level level1 = new Level("Level 1: Tutorial", "Learn the basics of Mario!", new Color(135, 206, 250));
        level1.addItem(new Mushroom(new Point(200, 400)));
        level1.addItem(new FireFlower(new Point(300, 350)));
        level1.addItem(new Star(new Point(450, 380)));
        level1.addBlock(new Block(new Point(350, 420)));
        level1.addBlock(new Block(new Point(390, 420)));
        levels.add(level1);

        // Level 2: Power-ups Galore
        Level level2 = new Level("Level 2: Power-ups Galore", "Collect all the power-ups!", new Color(144, 238, 144));
        level2.addItem(new Mushroom(new Point(150, 380)));
        level2.addItem(new Mushroom(new Point(250, 350)));
        level2.addItem(new FireFlower(new Point(350, 370)));
        level2.addItem(new FireFlower(new Point(450, 340)));
        level2.addItem(new Star(new Point(550, 390)));
        level2.addBlock(new Block(new Point(200, 400)));
        level2.addBlock(new Block(new Point(240, 400)));
        level2.addBlock(new Block(new Point(300, 420)));
        level2.addBlock(new Block(new Point(400, 390)));
        levels.add(level2);

        // Level 3: Decorator Challenge
        Level level3 = new Level("Level 3: Decorator Challenge", "Master the decorator abilities!", new Color(255, 182, 193));
        level3.addItem(new Star(new Point(180, 370)));
        level3.addItem(new FireFlower(new Point(320, 360)));
        level3.addItem(new Star(new Point(480, 380)));
        for (int i = 0; i < 8; i++) {
            level3.addBlock(new Block(new Point(150 + i * 45, 420)));
        }
        levels.add(level3);

        // Level 4: Speed Run
        Level level4 = new Level("Level 4: Speed Run", "Fast-paced action level!", new Color(255, 218, 185));
        for (int i = 0; i < 3; i++) {
            level4.addItem(new Mushroom(new Point(180 + i * 120, 390)));
            level4.addItem(new FireFlower(new Point(220 + i * 120, 360)));
        }
        level4.addItem(new Star(new Point(500, 380)));
        for (int i = 0; i < 10; i++) {
            level4.addBlock(new Block(new Point(120 + i * 42, 430)));
        }
        levels.add(level4);

        // Level 5: Boss Level
        Level level5 = new Level("Level 5: Boss Level", "The ultimate challenge!", new Color(186, 85, 211));
        level5.addItem(new Star(new Point(200, 350)));
        level5.addItem(new Star(new Point(400, 360)));
        level5.addItem(new FireFlower(new Point(300, 340)));
        // Create a castle-like structure
        for (int i = 0; i < 12; i++) {
            level5.addBlock(new Block(new Point(100 + i * 40, 440)));
        }
        for (int i = 0; i < 4; i++) {
            level5.addBlock(new Block(new Point(200 + i * 40, 400)));
            level5.addBlock(new Block(new Point(280 + i * 40, 400)));
        }
        levels.add(level5);
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public void nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
        }
    }

    public void previousLevel() {
        if (currentLevelIndex > 0) {
            currentLevelIndex--;
        }
    }

    public void resetCurrentLevel() {
        getCurrentLevel().reset();
    }

    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    public int getTotalLevels() {
        return levels.size();
    }

    public boolean isLastLevel() {
        return currentLevelIndex == levels.size() - 1;
    }
}
