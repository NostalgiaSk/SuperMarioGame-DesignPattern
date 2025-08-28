package ui;
import interfaces.MarioComponent;
import java.util.List;

/**
 * Game UI class for beautiful console output
 */
public class GameUI {
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String GREEN = "\033[32m";
    private static final String BLUE = "\033[34m";
    private static final String RED = "\033[31m";
    private static final String YELLOW = "\033[33m";
    private static final String PURPLE = "\033[35m";
    private static final String CYAN = "\033[36m";

    public static void printHeader() {
        System.out.println("\n" + BOLD + CYAN +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║                🍄 SUPER MARIO DESIGN PATTERNS 🍄              ║\n" +
                "║              State Pattern + Decorator Pattern               ║\n" +
                "╚══════════════════════════════════════════════════════════════╝" +
                RESET + "\n");
    }

    public static void printMarioStatus(MarioComponent mario) {
        System.out.println("\n" + BOLD + "═══ MARIO STATUS ═══" + RESET);
        System.out.println("🎭 State: " + YELLOW + mario.getStateName() + RESET);
        System.out.println("🎯 Score: " + GREEN + mario.getScore() + RESET);
        System.out.println("❤️  Lives: " + RED + mario.getLives() + RESET);
        System.out.println("🔧 Can Break Blocks: " + (mario.canBreakBlocks() ? GREEN + "YES" : RED + "NO") + RESET);
        System.out.println("🔥 Can Shoot Fire: " + (mario.canShootFire() ? GREEN + "YES" : RED + "NO") + RESET);

        List<String> abilities = mario.getAbilities();
        if (!abilities.isEmpty()) {
            System.out.println("⚡ Active Abilities: " + PURPLE + String.join(", ", abilities) + RESET);
        }
        System.out.println("─────────────────────────────────────");
    }

    public static void printAction(String message) {
        System.out.println(BLUE + "🎮 " + message + RESET);
    }

    public static void printStateChange(String message) {
        System.out.println(GREEN + BOLD + "🔄 " + message + RESET);
    }

    public static void printDecorator(String message) {
        System.out.println(PURPLE + "⚡ " + message + RESET);
    }

    public static void printDamage(String message) {
        System.out.println(RED + BOLD + "💥 " + message + RESET);
    }

    public static void printBonus(String message) {
        System.out.println(YELLOW + "🎁 " + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + "❌ " + message + RESET);
    }

    public static void printTimer(String message) {
        System.out.println(CYAN + "⏱️  " + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println("ℹ️  " + message);
    }

    public static void printGameOver(String message) {
        System.out.println("\n" + RED + BOLD +
                "╔════════════════════╗\n" +
                "║    " + message + "    ║\n" +
                "╚════════════════════╝" +
                RESET + "\n");
    }

    public static void printMenu() {
        System.out.println("\n" + BOLD + "🎮 GAME CONTROLS:" + RESET);
        System.out.println("1️⃣  Jump");
        System.out.println("2️⃣  Move");
        System.out.println("3️⃣  Shoot Fire");
        System.out.println("4️⃣  Break Block");
        System.out.println("5️⃣  Collect Mushroom 🍄");
        System.out.println("6️⃣  Collect Fire Flower 🌸");
        System.out.println("7️⃣  Collect Star ⭐");
        System.out.println("8️⃣  Take Damage 💥");
        System.out.println("9️⃣  Add Speed Boost 🚀");
        System.out.println("🔟 Add Double Jump ⬆️");
        System.out.println("1️⃣1️⃣ Add Shield 🛡️");
        System.out.println("1️⃣2️⃣ Add Super Strength 💪");
        System.out.println("1️⃣3️⃣ Demo Sequence 🎭");
        System.out.println("1️⃣4️⃣ Reset Game 🔄");
        System.out.println("0️⃣  Exit");
        System.out.print("\n" + CYAN + "Choose an action: " + RESET);
    }
}