package dev.matthiesen.cobblemon_boosters.common.boosts;

import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.matthiesen_core.common.utility.BossBar;

import java.util.List;

public abstract class AbstractSimpleBoost implements IBoost {
    public float multiplier;
    public int duration;
    public long timeRemaining;
    public transient BossBar bossBar;

    public AbstractSimpleBoost(float multiplier, int duration, long timeRemaining) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = timeRemaining;
    }

    public static BoostParts parseRawStringToParts(String string, int expectedParts) {
        String[] parts = string.split(";");
        if (parts.length != expectedParts) return null;

        float multiplier = Float.parseFloat(parts[0]);
        int duration = Integer.parseInt(parts[1]);
        long timeRemaining = Long.parseLong(parts[2]);

        List<String> remainingParts = List.of(parts).subList(3, parts.length);

        return new BoostParts(multiplier, duration, timeRemaining, remainingParts);
    }

    public record BoostParts(float multiplier, int duration, long timeRemaining, List<String> remainingParts) {
        public String getRemainingPart(int index) {
            if (index < 0 || index >= remainingParts.size()) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for remainingParts of size " + remainingParts.size());
            }
            return remainingParts.get(index);
        }
    }

    public AbstractSimpleBoost(float multiplier, int duration) {
        this(multiplier, duration, duration * 20L);
    }

    public String serialize() {
        return multiplier + ";" + duration + ";" + timeRemaining;
    }

    @Override
    public float getMultiplier() {
        return this.multiplier;
    }

    @Override
    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
        this.timeRemaining = duration * 20L;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public long getTimeRemaining() {
        return this.timeRemaining;
    }

    @Override
    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    @Override
    public BossBar.Builder getBossBar() {
        if (this.bossBar == null) {
            this.bossBar = createBossBar();
        }
        return this.bossBar.getBuilder();
    }
}
