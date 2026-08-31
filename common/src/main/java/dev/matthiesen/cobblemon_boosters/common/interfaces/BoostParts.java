package dev.matthiesen.cobblemon_boosters.common.interfaces;

import java.util.List;

public record BoostParts(float multiplier, int duration, long timeRemaining, List<String> remainingParts) {
    public String getRemainingPart(int index) {
        if (index < 0 || index >= remainingParts.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for remainingParts of size " + remainingParts.size());
        }
        return remainingParts.get(index);
    }
}
