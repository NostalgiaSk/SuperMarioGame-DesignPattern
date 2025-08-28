package decorators;

import interfaces.MarioComponent;
import ui.GameUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

/**
 * Shield Decorator - Absorbs damage
 */
public class ShieldDecorator extends MarioDecorator {
    private int shieldStrength;
    private int duration;
    private ScheduledExecutorService timer;

    public ShieldDecorator(MarioComponent mario) {
        super(mario);
        this.shieldStrength = 2;
        this.duration = 12; // 12 seconds
        startTimer();
    }

    @Override
    public void takeDamage() {
        if (shieldStrength > 0) {
            shieldStrength--;
            GameUI.printDecorator("🛡️ SHIELD: Damage blocked! Strength remaining: " + shieldStrength);
            if (shieldStrength <= 0) {
                GameUI.printDecorator("🛡️ Shield broken!");
            }
        } else {
            decoratedMario.takeDamage();
        }
    }

    @Override
    public String getStateName() {
        return decoratedMario.getStateName() + " [🛡️Shield:" + shieldStrength + "/" + duration + "s]";
    }

    @Override
    public boolean hasAbility(String ability) {
        return ability.equals("Shield") || decoratedMario.hasAbility(ability);
    }

    @Override
    public List<String> getAbilities() {
        List<String> abilities = new ArrayList<>(decoratedMario.getAbilities());
        abilities.add("Shield (" + shieldStrength + " hits, " + duration + "s)");
        return abilities;
    }

    private void startTimer() {
        timer = Executors.newScheduledThreadPool(1);
        timer.scheduleAtFixedRate(() -> {
            duration--;
            if (duration <= 0 || shieldStrength <= 0) {
                GameUI.printDecorator("🛡️ Shield expired!");
                timer.shutdown();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean isActive() {
        return duration > 0 && shieldStrength > 0;
    }
}