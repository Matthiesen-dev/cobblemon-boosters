package dev.matthiesen.common.cobblemon_boosters.utils;

import com.n1netails.n1netails.discord.exception.DiscordWebhookException;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.config.ModConfig;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;

import java.util.Queue;
import java.util.function.Consumer;

public class TickManager {
    private static <T extends IBoost> void handleBoostTick(
            T activeBoost,
            Queue<T> queue,
            Consumer<T> setActiveBoost,
            ModConfig.DiscordEmbed boostEndEmbed,
            ModConfig.DiscordEmbed boostStartEmbed
    ) throws DiscordWebhookException {
        if (activeBoost == null) return;
        decrementBoost(activeBoost);
        if (activeBoost.getTimeRemaining() > 0) return;
        CobblemonBoosters.INSTANCE.getAdventure().all().hideBossBar(activeBoost.getBossBar());
        CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                boostEndEmbed,
                activeBoost
        );
        T nextBoost = queue.poll();
        setActiveBoost.accept(nextBoost);
        if (nextBoost != null) {
            CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(nextBoost.getBossBar());
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    boostStartEmbed,
                    nextBoost
            );
        }
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
    }

    public static void tickBoosts() throws DiscordWebhookException {
        handleBoostTick(
                CobblemonBoosters.INSTANCE.activeShinyBoost,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                boost -> CobblemonBoosters.INSTANCE.activeShinyBoost = boost,
                CobblemonBoosters.INSTANCE.config.discordWebhookConfig.shinyEventEndEmbed,
                CobblemonBoosters.INSTANCE.config.discordWebhookConfig.shinyEventStartEmbed
        );
        handleBoostTick(
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                boost -> CobblemonBoosters.INSTANCE.activeCatchBoost = boost,
                CobblemonBoosters.INSTANCE.config.discordWebhookConfig.catchEventEndEmbed,
                CobblemonBoosters.INSTANCE.config.discordWebhookConfig.catchEventStartEmbed
        );
    }

    public static void updateBossBars() {
        if (CobblemonBoosters.INSTANCE.activeShinyBoost != null) {
            updateBossBar(CobblemonBoosters.INSTANCE.activeShinyBoost);
        }
        if (CobblemonBoosters.INSTANCE.activeCatchBoost != null) {
            updateBossBar(CobblemonBoosters.INSTANCE.activeCatchBoost);
        }
    }

    private static void decrementBoost(IBoost boost) {
        boost.setTimeRemaining(boost.getTimeRemaining() - 1);
    }

    private static void updateBossBar(IBoost boost) {
        float progressRate = 1.0F / (boost.getDuration() * 20L);
        float total = progressRate * boost.getTimeRemaining();

        if (total < 0F)
            total = 0F;
        if (total > 1F)
            total = 1F;

        boost.getBossBar().progress(total);
        boost.getBossBar().name(boost.getBossBarText());
    }
}
