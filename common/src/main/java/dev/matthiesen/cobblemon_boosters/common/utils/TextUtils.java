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
                .getTextParser(BoostersConfig.TEXT_CONFIG.textParser.get());
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

            TimeString.Boost boostTimeStrings = TimeString.fromBoost(boost);
            boostTimeStrings.registerPlaceholders(map);

            if (boost instanceof SpawnBucketBoost spawnBucketBoost && spawnBucketBoost.bucket != null) {
                map.put("%bucket%", spawnBucketBoost.getBucketDisplayName());
            }
        }

        return map;
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
}
