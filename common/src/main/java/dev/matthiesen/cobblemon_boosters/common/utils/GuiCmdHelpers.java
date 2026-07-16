package dev.matthiesen.cobblemon_boosters.common.utils;

public final class GuiCmdHelpers {
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
}
