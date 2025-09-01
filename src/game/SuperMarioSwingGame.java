package game;

import core.Mario;
import decorators.ShieldDecorator;
import decorators.SpeedBoostDecorator;
import interfaces.MarioComponent;
import ui.GameFrame;

import javax.swing.*;

/**
 * Main application launcher for Super Mario Design Patterns Game
 * Entry point that demonstrates the complete implementation of State and Decorator patterns
 */
public class SuperMarioSwingGame {

    public static void main(String[] args) {
        // Set look and feel to system default for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system LAF is not available
            System.out.println("Using default look and feel");
        }

        // Run tests first to validate implementation
        System.out.println("=".repeat(60));
        System.out.println("🍄 SUPER MARIO DESIGN PATTERNS - STARTING TESTS 🍄");
        System.out.println("=".repeat(60));



        System.out.println("=".repeat(60));
        System.out.println("🎮 LAUNCHING GAME... 🎮");
        System.out.println("=".repeat(60));

        // Create and show the game on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            GameFrame game = GameFrame.getInstance();
            game.setVisible(true);

            // Show welcome message with instructions
            showWelcomeDialog(game);
        });
    }

    /**
     * Display welcome dialog with game instructions
     */
    private static void showWelcomeDialog(GameFrame parent) {
        String welcomeMessage =
                "🍄 Welcome to Super Mario Design Patterns! 🍄\n\n" +

                        "📚 DESIGN PATTERNS DEMONSTRATION:\n" +
                        "• State Pattern: Mario's abilities change based on his current form\n" +
                        "• Decorator Pattern: Temporary abilities that enhance any state\n\n" +

                        "🎮 GAME CONTROLS:\n" +
                        "• Mouse: Click buttons for actions\n" +
                        "• Keyboard: W/Space=Jump, A=Move, S=Fire, D=Break\n" +
                        "• Numbers: 1=Mushroom, 2=Fire Flower, 3=Star, 4=Damage\n\n" +

                        "🎯 GAMEPLAY:\n" +
                        "• Collect items by moving Mario near them (automatic)\n" +
                        "• Watch Mario's behavior change with different states!\n" +
                        "• Add temporary abilities with decorators!\n" +
                        "• Try the Demo button for automatic pattern demonstration\n\n" +

                        "🏆 LEVELS:\n" +
                        "• 5 unique levels with different challenges\n" +
                        "• Use Previous/Next Level buttons to explore\n\n" +

                        "Have fun exploring how design patterns work together!";

        JOptionPane.showMessageDialog(parent, welcomeMessage,
                "Welcome to Super Mario Design Patterns!",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Alternative console-only launcher for testing without GUI
     */
    public static void runConsoleDemo() {
        System.out.println("🍄 CONSOLE DEMO - Super Mario Design Patterns 🍄\n");

        // Create Mario instance
        Mario mario = new Mario();
        System.out.println("Initial state: " + mario.getStateName());
        System.out.println("Can break blocks: " + mario.canBreakBlocks());
        System.out.println("Can shoot fire: " + mario.canShootFire());

        // Demonstrate State Pattern
        System.out.println("\n📖 STATE PATTERN DEMO:");
        mario.collectMushroom();
        System.out.println("After mushroom: " + mario.getStateName());

        mario.collectFireFlower();
        System.out.println("After fire flower: " + mario.getStateName());
        System.out.println("Can shoot fire: " + mario.canShootFire());

        // Demonstrate Decorator Pattern
        System.out.println("\n📖 DECORATOR PATTERN DEMO:");
        MarioComponent decoratedMario = mario;
        decoratedMario = new SpeedBoostDecorator(decoratedMario);
        decoratedMario = new ShieldDecorator(decoratedMario);

        System.out.println("With decorators: " + decoratedMario.getStateName());
        System.out.println("Abilities: " + decoratedMario.getAbilities());

        // Demonstrate Integration
        System.out.println("\n📖 INTEGRATION DEMO:");
        decoratedMario.takeDamage(); // Shield should protect
        System.out.println("After damage (shield active): " + decoratedMario.getStateName());

        decoratedMario.takeDamage(); // Shield should break
        System.out.println("After second damage: " + decoratedMario.getStateName());

        System.out.println("\n🎉 Console demo complete!");
    }
}