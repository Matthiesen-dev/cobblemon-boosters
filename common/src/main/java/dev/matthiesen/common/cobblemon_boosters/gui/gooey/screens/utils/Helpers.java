package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.utils;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;

import java.util.List;

public final class Helpers {
    public static final List<String> allowedUnits = List.of("seconds", "minutes", "hours", "days");
    public static final List<String> allowedBuckets = List.of("common", "uncommon", "rare", "ultra-rare");

    public static int parseTotalSeconds(int duration, String unit) {
        int totalSeconds;
        switch (unit) {
            case "minutes" -> totalSeconds = duration * 60;
            case "hours" -> totalSeconds = duration * 3600;
            case "days" -> totalSeconds = duration * 86400;
            default -> totalSeconds = duration;
        }
        return totalSeconds;
    }

    public static Button getFrame() {
        return GooeyButton.builder()
                .display(MenuUtils.getFrameItem())
                .build();
    }

    public static PlaceholderButton getPlaceholder() {
        return new PlaceholderButton();
    }
}
