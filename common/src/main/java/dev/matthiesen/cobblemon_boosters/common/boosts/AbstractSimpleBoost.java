package dev.matthiesen.cobblemon_boosters.common.boosts;

import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.matthiesen_core.common.utility.BossBar;

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
