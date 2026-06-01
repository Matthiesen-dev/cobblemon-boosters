package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.utils;

import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;

import java.util.List;

public class BaseBoostBuilder {
    public Float multiplier;
    public Integer duration;
    public String unit;

    public BaseBoostBuilder() {}

    public BaseBoostBuilder setMultiplier(float multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public BaseBoostBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public BaseBoostBuilder setUnit(String unit) {
        List<String> allowedUnits = Helpers.allowedUnits;
        if (allowedUnits.contains(unit.toLowerCase())) {
            this.unit = unit.toLowerCase();
        } else {
            this.unit = unit;
        }
        return this;
    }

    public <T extends IBoost> T build(Class<T> boostClass) {
        try {
            int totalSeconds = Helpers.parseTotalSeconds(duration, unit);
            return boostClass.getDeclaredConstructor(float.class, int.class)
                    .newInstance(multiplier, totalSeconds);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build boost", e);
        }
    }
}
