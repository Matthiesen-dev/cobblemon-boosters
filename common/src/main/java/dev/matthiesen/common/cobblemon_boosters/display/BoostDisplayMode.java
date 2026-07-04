package dev.matthiesen.common.cobblemon_boosters.display;

import java.util.Locale;

/**
 * How active boosts are shown to players.
 *
 * <ul>
 *     <li>{@link #BOSSBAR} - vanilla boss bar, one per active boost (default).</li>
 *     <li>{@link #SIDEBAR} - vanilla scoreboard sidebar, one line per active boost.</li>
 *     <li>{@link #NONE} - nothing is displayed.</li>
 * </ul>
 */
public enum BoostDisplayMode {
    BOSSBAR,
    SIDEBAR,
    NONE;

    public static BoostDisplayMode fromString(String value) {
        if (value == null) {
            return BOSSBAR;
        }

        String normalized = value.trim().toUpperCase(Locale.ROOT);
        for (BoostDisplayMode mode : values()) {
            if (mode.name().equals(normalized)) {
                return mode;
            }
        }
        return BOSSBAR;
    }
}
