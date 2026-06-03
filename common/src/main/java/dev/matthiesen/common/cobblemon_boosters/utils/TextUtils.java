package dev.matthiesen.common.cobblemon_boosters.utils;

import com.cobblemon.mod.common.Cobblemon;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import net.minecraft.network.chat.Component;

import java.time.Instant;

public final class TextUtils {
    public static Component deserialize(String text) {
        return MatthiesenLibApi.getTextParser(MatthiesenLibBuiltInTextParsers.VANILLA).parse(text);
    }

    public static String getCurrentTimestampForDiscordEmbed() {
        return Instant.now().toString();
    }

    public static String parse(String text) {
        return text
                .replaceAll("%prefix%", CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.prefix)
                .replaceAll("%base_shiny_rate%", String.valueOf(Cobblemon.config.getShinyRate()));
    }

    public static String parse(String text, IBoost boost) {
        text = parse(text);
        if (boost instanceof SpawnBucketBoost && ((SpawnBucketBoost) boost).bucket != null) {
            text = text.replaceAll("%bucket%", ((SpawnBucketBoost) boost).getBucketDisplayName());
        }
        return text
                .replaceAll("%multiplier%", String.valueOf(boost.getMultiplier()))
                .replaceAll("%duration%", hms(boost.getDuration()))
                .replaceAll("%time_remaining%", hms(boost.getTimeRemaining() / 20L))
                .replaceAll("%discord_webhook_author_name%", CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig.discordAuthorName)
                .replaceAll("%discord_webhook_author_icon_url%", CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig.discordAuthorIconUrl)
                .replaceAll("%timestamp%", getCurrentTimestampForDiscordEmbed());
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
