package dev.matthiesen.cobblemon_boosters.common.services.managers;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfigManager;
import dev.matthiesen.cobblemon_boosters.common.config.CacheConfig;
import dev.matthiesen.cobblemon_boosters.common.config.WebhooksConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import net.minecraft.server.MinecraftServer;

public final class TickManager {
    private static int tickCounter = 0;

    public static int getSaveIntervalTicks() {
        return CobblemonBoostersCommon.INSTANCE.getCoreConfigManager().getConfig().saveIntervalTicks;
    }

    public static void tick() {
        try {
            tickBoosts();
            MinecraftServer server = CobblemonBoostersCommon.INSTANCE.getMinecraftServer();
            if (server != null) {
                ServiceManager.getDisplayService().tick(server);
            }
            tickCounter++;
            var saveInterval = getSaveIntervalTicks();
            if (tickCounter >= saveInterval) {
                tickCounter = 0;
                if (CobblemonBoostersCommon.INSTANCE.getCoreConfigManager().getConfig().verboseCacheLogging) {
                    CobblemonBoostersCommon.INSTANCE.createInfoLog("Saving Boosters to Cache...");
                }
                BoostersConfigManager.saveCache();
            }
        } catch (IllegalArgumentException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Caught BossBar exception! ", e);
        }
    }

    public static void tickBoosts() {
        var webhooks = CobblemonBoostersCommon.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;

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
            ServiceManager.getDisplayService().onBoostDeactivated(activeBoost);
            ServiceManager.getDiscordWebhookService().sendMessage(
                    boostEndEmbed,
                    activeBoost
            );
        }
        T nextBoost = queue.poll();
        boostManager.setActive(nextBoost);
        if (nextBoost != null) {
            ServiceManager.getDisplayService().onBoostActivated(nextBoost);
            ServiceManager.getDiscordWebhookService().sendMessage(
                    boostStartEmbed,
                    nextBoost
            );
        }
        CacheConfig.setGlobalBoostData();
    }
}
