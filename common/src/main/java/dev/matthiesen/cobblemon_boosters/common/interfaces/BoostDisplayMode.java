package dev.matthiesen.cobblemon_boosters.common.interfaces;

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
    NONE
}
