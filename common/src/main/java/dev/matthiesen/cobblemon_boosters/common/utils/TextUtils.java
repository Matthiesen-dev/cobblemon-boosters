package dev.matthiesen.cobblemon_boosters.common.utils;

import com.cobblemon.mod.common.Cobblemon;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public final class TextUtils {
    public static TextParser getTextParser() {
        return CobblemonBoostersCommon.INSTANCE.getTextParserManager()
                .getTextParser(BoostersConfig.TEXT_CONFIG.messages_textParser.get());
    }

    public static String getCurrentTimestampForDiscordEmbed() {
        return Instant.now().toString();
    }

    public static Map<String, String> getPlaceholders() {
        return getPlaceholders(null);
    }

    public static Map<String, String> getPlaceholders(@Nullable IBoost boost) {
        Map<String, String> map = new HashMap<>();

        map.put("%prefix%", BoostersConfig.TEXT_CONFIG.messages_prefix.get());
        map.put("%base_shiny_rate%", String.valueOf(Cobblemon.config.getShinyRate()));
        map.put("%discord_webhook_author_name%", BoostersConfig.WEBHOOKS_SERVER_CONFIG.discordAuthorName.get());
        map.put("%discord_webhook_author_icon_url%", BoostersConfig.WEBHOOKS_SERVER_CONFIG.discordAuthorIconUrl.get());
        map.put("%timestamp%", getCurrentTimestampForDiscordEmbed());

        if (boost != null) {
            map.put("%multiplier%", formatMultiplier(boost.getMultiplier()));
            map.put("%duration%", hms(boost.getDuration()));
            map.put("%time_remaining_short2%", hmsShort2(boost.getTimeRemaining() / 20L));
            map.put("%time_remaining_short%", hmsShort(boost.getTimeRemaining() / 20L));
            map.put("%time_remaining%", hms(boost.getTimeRemaining() / 20L));

            if (boost instanceof SpawnBucketBoost spawnBucketBoost && spawnBucketBoost.bucket != null) {
                map.put("%bucket%", spawnBucketBoost.getBucketDisplayName());
            }
        }

        return map;
    }

    /** Compact time: only the largest non-zero unit (e.g. {@code 2h}, {@code 59m}, {@code 30s}). */
    public static String hmsShort(long raw_time) {
        if (raw_time < 0) {
            raw_time = 0;
        }
        long days = raw_time / 86400;
        if (days > 0) {
            return days + "d";
        }
        long hours = raw_time / 3600;
        if (hours > 0) {
            return hours + "h";
        }
        long minutes = raw_time / 60;
        if (minutes > 0) {
            return minutes + "m";
        }
        return (raw_time % 60) + "s";
    }

    /** Formats a multiplier without a trailing {@code .0} (e.g. {@code 2}, {@code 2.5}, {@code 2.25}). */
    public static String formatMultiplier(float multiplier) {
        if (multiplier == Math.rint(multiplier) && !Float.isInfinite(multiplier)) {
            return Long.toString((long) multiplier);
        }
        return new BigDecimal(Float.toString(multiplier))
                .stripTrailingZeros()
                .toPlainString();
    }

    /** Compact time with the two largest units (e.g. {@code 1d 5h}, {@code 5h 30m}, {@code 30m 10s}, {@code 45s}). */
    public static String hmsShort2(long raw_time) {
        if (raw_time < 0) {
            raw_time = 0;
        }
        long days = raw_time / 86400;
        long hours = (raw_time % 86400) / 3600;
        long minutes = (raw_time % 3600) / 60;
        long seconds = raw_time % 60;

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

    public static String hms(long raw_time) {
        if (raw_time < 0) {
            raw_time = 0;
        }
        long days;
        long hours;
        long minutes;
        long seconds = raw_time;

        String output = "";

        days = seconds / 86400;
        seconds %= 86400;
        hours = seconds / 3600;
        seconds %= 3600;
        minutes = seconds / 60;
        seconds %= 60;

        if (days > 0) {
            output = output.concat(days + "d ");
        }
        if (hours > 0) {
            output = output.concat(hours + "h ");
        }
        if (minutes > 0) {
            output = output.concat(minutes + "m ");
        }
        output = output.concat(seconds + "s");

        return output;
    }
}
