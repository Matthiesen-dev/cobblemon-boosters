package dev.matthiesen.cobblemon_boosters.common.utils;

import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;

import java.util.Map;

public record TimeString(long raw) {
    public static TimeString.Boost fromBoost(IBoost boost) {
        return new TimeString.Boost(boost);
    }

    public boolean isZero() {
        return raw <= 0;
    }

    public long getSeconds() {
        return raw % 60;
    }

    public long getMinutes() {
        return (raw % 3600) / 60;
    }

    public long getHours() {
        return (raw % 86400) / 3600;
    }

    public long getDays() {
        return raw / 86400;
    }

    public String getShortString() {
        if (isZero()) {
            return "0s";
        }
        long days = getDays();
        if (days > 0) {
            return days + "d";
        }
        long hours = getHours();
        if (hours > 0) {
            return hours + "h";
        }
        long minutes = getMinutes();
        if (minutes > 0) {
            return minutes + "m";
        }
        return getSeconds() + "s";
    }

    public String getShortExtendedString() {
        if (isZero()) {
            return "0s";
        }
        long days = getDays();
        long hours = getHours();
        long minutes = getMinutes();
        long seconds = getSeconds();
        if (days > 0) {
            return days + "d " + hours + "h";
        }
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        }
        return seconds + "s";
    }

    public String getLongString() {
        if (isZero()) {
            return "0s";
        }
        long days = getDays();
        long hours = getHours();
        long minutes = getMinutes();
        long seconds = getSeconds();
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("d ");
        }
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        sb.append(seconds).append("s");
        return sb.toString().trim();
    }

    public record Boost(IBoost boost) {
        public TimeString duration() {
            return new TimeString(boost().getDuration());
        }

        public TimeString timeRemaining() {
            return new TimeString(boost().getTimeRemaining() / 20L);
        }

        public void registerPlaceholders(Map<String,String> placeholderMap) {
            TimeString duration = duration();
            TimeString timeRemaining = timeRemaining();

            placeholderMap.put("%duration%", duration.getLongString());
            placeholderMap.put("%time_remaining%", timeRemaining.getLongString());
            placeholderMap.put("%time_remaining_short%", timeRemaining.getShortString());
            placeholderMap.put("%time_remaining_short2%", timeRemaining.getShortExtendedString());
        }
    }
}
