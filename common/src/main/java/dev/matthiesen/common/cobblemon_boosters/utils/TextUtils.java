package dev.matthiesen.common.cobblemon_boosters.utils;

import com.cobblemon.mod.common.Cobblemon;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TextUtils {
    public static net.minecraft.network.chat.Component deserializeMC(String text) {
        return CobblemonBoosters.INSTANCE.getAdventure().asNative(
                MiniMessage.miniMessage().deserialize("<!i>" + text)
        );
    }

    public static Component deserialize(String text) {
        return CobblemonBoosters.INSTANCE.getAdventure().asAdventure(deserializeMC(text));
    }

    public static String parse(String text) {
        return text
                .replaceAll("%prefix%", CobblemonBoosters.INSTANCE.config.messages.prefix)
                .replaceAll("%base_shiny_rate%", String.valueOf(Cobblemon.config.getShinyRate()));
    }

    public static String parse(String text, IBoost boost) {
        text = parse(text);
        return text
                .replaceAll("%multiplier%", String.valueOf(boost.getMultiplier()))
                .replaceAll("%duration%", hms(boost.getDuration()))
                .replaceAll("%time_remaining%", hms(boost.getTimeRemaining() / 20L));
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
