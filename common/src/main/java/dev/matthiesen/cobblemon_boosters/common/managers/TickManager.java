package dev.matthiesen.cobblemon_boosters.common.managers;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoosters;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfigManager;
import dev.matthiesen.cobblemon_boosters.common.config.CacheConfig;
import dev.matthiesen.cobblemon_boosters.common.config.WebhooksConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.server.MinecraftServer;

public final class TickManager {
    private static int tickCounter = 0;

    public static int getSaveIntervalTicks() {
        return CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig().saveIntervalTicks;
    }

    public static void tick() {
        try {
            tickBoosts();
            MinecraftServer server = MatthiesenLibApi.getMinecraftServer();
            if (server != null) {
                CobblemonBoosters.INSTANCE.displayService.tick(server);
            }
            tickCounter++;
            var saveInterval = getSaveIntervalTicks();
            if (tickCounter >= saveInterval) {
                tickCounter = 0;
                if (CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig().verboseCacheLogging) {
                    Constants.createInfoLog("Saving Boosters to Cache...");
                }
                BoostersConfigManager.saveCache();
            }
        } catch (IllegalArgumentException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.LOGGER.error("Caught BossBar exception! ", e);
        }
    }

    public static void tickBoosts() {
        var webhooks = CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;

        handleBoostTick(
                BoostManager.getShinyBoostManager(),
                webhooks.shinyEventEndEmbed,
                webhooks.shinyEventStartEmbed
        );

        handleBoostTick(
                BoostManager.getCatchBoostManager(),
                webhooks.catchEventEndEmbed,
                webhooks.catchEventStartEmbed
        );

        handleBoostTick(
                BoostManager.getExperienceBoostManager(),
                webhooks.experienceEventEndEmbed,
                webhooks.experienceEventStartEmbed
        );

        handleBoostTick(
                BoostManager.getSpawnBucketBoostManager(),
                webhooks.spawnBucketEventEndEmbed,
                webhooks.spawnBucketEventStartEmbed
        );
    }

    private static void decrementBoost(IBoost boost) {
        boost.setTimeRemaining(boost.getTimeRemaining() - 1);
    }

    private static <T extends IBoost> void handleBoostTick(
            BoostManager.IBoostManager<T> boostManager,
            WebhooksConfig.DiscordEmbed boostEndEmbed,
            WebhooksConfig.DiscordEmbed boostStartEmbed
    ) {
        var activeBoost = boostManager.getActive();
        var queue = boostManager.getQueue();
        if (activeBoost == null && queue.isEmpty()) return;
        if (activeBoost != null) {
            decrementBoost(activeBoost);
            if (activeBoost.getTimeRemaining() > 0) return;
            CobblemonBoosters.INSTANCE.displayService.onBoostDeactivated(activeBoost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    boostEndEmbed,
                    activeBoost
            );
        }
        T nextBoost = queue.poll();
        boostManager.setActive(nextBoost);
        if (nextBoost != null) {
            CobblemonBoosters.INSTANCE.displayService.onBoostActivated(nextBoost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    boostStartEmbed,
                    nextBoost
            );
        }
        CacheConfig.setGlobalBoostData();
    }
}
