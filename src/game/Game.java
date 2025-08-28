package game;
import interfaces.MarioComponent;
import core.Mario;
import decorators.*;
import ui.GameUI;
import java.util.Scanner;

/**
 * Main Game class for demonstration and testing
 */
public class Game {
    private static MarioComponent mario;
    private static Scanner scanner;

    public static void main(String[] args) {
        mario = new Mario();
        scanner = new Scanner(System.in);

        GameUI.printHeader();
        GameUI.printInfo("Welcome to the Mario Design Patterns demonstration!");
        GameUI.printInfo("This game showcases State Pattern and Decorator Pattern working together.");

        gameLoop();
    }

    private static void gameLoop() {
        while (true) {
            GameUI.printMarioStatus(mario);
            GameUI.printMenu();

            try {
                int choice = scanner.nextInt();
                executeAction(choice);

                if (choice == 0) break;

                // Small delay for better UX
                Thread.sleep(500);

            } catch (Exception e) {
                GameUI.printError("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        GameUI.printInfo("Thanks for playing! 👋");
        scanner.close();
    }

    private static void executeAction(int choice) throws InterruptedException {
        switch (choice) {
            case 1: mario.jump(); break;
            case 2: mario.move(); break;
            case 3: shootFire(); break;
            case 4: breakBlock(); break;
            case 5: mario.collectMushroom(); break;
            case 6: mario.collectFireFlower(); break;
            case 7: mario.collectStar(); break;
            case 8: mario.takeDamage(); break;
            case 9: addSpeedBoost(); break;
            case 10: addDoubleJump(); break;
            case 11: addShield(); break;
            case 12: addSuperStrength(); break;
            case 13: demonstratePatterns(); break;
            case 14: resetGame(); break;
            case 0: return;
            default: GameUI.printError("Invalid choice!");
        }
    }

    private static void shootFire() {
        if (mario.canShootFire()) {
            if (mario instanceof Mario) {
                ((Mario) mario).shootFire();
            } else {
                // If mario is decorated, we need to find the underlying Mario
                GameUI.printAction("🔥💥 Shooting fireball through decorators!");
                mario.addScore(50);
            }
        } else {
            GameUI.printError("❌ Cannot shoot fire in current state!");
        }
    }

    private static void breakBlock() {
        if (mario.canBreakBlocks()) {
            GameUI.printAction("🧱💥 Breaking block!");
            mario.addScore(25);

            if (mario.hasAbility("Super Strength")) {
                GameUI.printDecorator("💪 SUPER STRENGTH: Breaking extra blocks!");
                mario.addScore(75); // Bonus with super strength
            }
        } else {
            GameUI.printError("❌ Cannot break blocks in current state!");
        }
    }

    private static void addSpeedBoost() {
        if (!mario.hasAbility("Speed Boost")) {
            mario = new SpeedBoostDecorator(mario);
            GameUI.printDecorator("🚀 Speed Boost activated!");
        } else {
            GameUI.printError("❌ Speed Boost already active!");
        }
    }

    private static void addDoubleJump() {
        if (!mario.hasAbility("Double Jump")) {
            mario = new DoubleJumpDecorator(mario);
            GameUI.printDecorator("⬆️ Double Jump activated!");
        } else {
            GameUI.printError("❌ Double Jump already active!");
        }
    }

    private static void addShield() {
        if (!mario.hasAbility("Shield")) {
            mario = new ShieldDecorator(mario);
            GameUI.printDecorator("🛡️ Shield activated!");
        } else {
            GameUI.printError("❌ Shield already active!");
        }
    }

    private static void addSuperStrength() {
        if (!mario.hasAbility("Super Strength")) {
            mario = new SuperStrengthDecorator(mario);
            GameUI.printDecorator("💪 Super Strength activated!");
        } else {
            GameUI.printError("❌ Super Strength already active!");
        }
    }

    private static void resetGame() {
        mario = new Mario();
        GameUI.printInfo("🔄 Game reset! Mario is back to Small Mario state.");
    }

    /**
     * Demonstrates both design patterns working together
     */
    private static void demonstratePatterns() throws InterruptedException {
        GameUI.printHeader();
        GameUI.printInfo("🎭 Starting Design Patterns Demonstration...\n");

        // Reset for clean demo
        mario = new Mario();

        GameUI.printInfo("📖 PART 1: STATE PATTERN DEMONSTRATION");
        GameUI.printInfo("Showing how Mario's behavior changes based on his state...\n");

        // Demonstrate state transitions
        GameUI.printMarioStatus(mario);
        Thread.sleep(2000);

        GameUI.printInfo("🍄 Mario collects a mushroom...");
        mario.collectMushroom();
        GameUI.printMarioStatus(mario);
        Thread.sleep(2000);

        GameUI.printInfo("🌸 Mario collects a fire flower...");
        mario.collectFireFlower();
        GameUI.printMarioStatus(mario);
        Thread.sleep(2000);

        GameUI.printInfo("🔥 Testing fire shooting ability...");
        shootFire();
        Thread.sleep(2000);

        GameUI.printInfo("💥 Mario takes damage...");
        mario.takeDamage();
        GameUI.printMarioStatus(mario);
        Thread.sleep(2000);

        GameUI.printInfo("\n📖 PART 2: DECORATOR PATTERN DEMONSTRATION");
        GameUI.printInfo("Adding temporary abilities using decorators...\n");

        // Add multiple decorators
        GameUI.printInfo("🚀 Adding Speed Boost...");
        addSpeedBoost();
        Thread.sleep(1000);

        GameUI.printInfo("⬆️ Adding Double Jump...");
        addDoubleJump();
        Thread.sleep(1000);

        GameUI.printInfo("🛡️ Adding Shield...");
        addShield();
        Thread.sleep(1000);

        GameUI.printInfo("💪 Adding Super Strength...");
        addSuperStrength();
        Thread.sleep(1000);

        GameUI.printMarioStatus(mario);

        GameUI.printInfo("\n📖 PART 3: INTEGRATION DEMONSTRATION");
        GameUI.printInfo("Showing how state changes work through decorators...\n");

        GameUI.printInfo("🍄 Mario (with all abilities) collects mushroom...");
        mario.collectMushroom();
        GameUI.printMarioStatus(mario);
        Thread.sleep(2000);

        GameUI.printInfo("🌸 Mario collects fire flower...");
        mario.collectFireFlower();
        GameUI.printMarioStatus(mario);
        Thread.sleep(2000);

        GameUI.printInfo("🔥 Testing enhanced fire shooting...");
        shootFire();
        Thread.sleep(1000);

        GameUI.printInfo("🧱 Testing enhanced block breaking...");
        breakBlock();
        Thread.sleep(1000);

        GameUI.printInfo("💥 Testing shield protection...");
        mario.takeDamage();
        Thread.sleep(1000);

        GameUI.printInfo("💥 Testing shield again...");
        mario.takeDamage();
        Thread.sleep(1000);

        GameUI.printInfo("💥 Testing after shield is gone...");
        mario.takeDamage();

        GameUI.printMarioStatus(mario);

        GameUI.printInfo("\n🎉 DEMONSTRATION COMPLETE!");
        GameUI.printInfo("You've seen both patterns working together seamlessly!");
        GameUI.printInfo("- State Pattern: Mario's form determines his base abilities");
        GameUI.printInfo("- Decorator Pattern: Temporary abilities enhance any state");
        GameUI.printInfo("- Integration: State changes preserve decorators until they expire\n");
    }
}