package dev.matthiesen.common.cobblemon_boosters.managers;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.config.BoostersConfigManager;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.config.WebhooksConfig;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
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
            updateBossBars();
            tickCounter++;
            var saveInterval = getSaveIntervalTicks();
            if (tickCounter >= saveInterval) {
                tickCounter = 0;
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

    public static void updateBossBars() {
        var shinyManager = BoostManager.getShinyBoostManager();
        var catchManager = BoostManager.getCatchBoostManager();
        var experienceManager = BoostManager.getExperienceBoostManager();
        var spawnBucketManager = BoostManager.getSpawnBucketBoostManager();

        if (shinyManager.getActive() != null) updateBossBar(shinyManager.getActive());
        if (catchManager.getActive() != null) updateBossBar(catchManager.getActive());
        if (experienceManager.getActive() != null) updateBossBar(experienceManager.getActive());
        if (spawnBucketManager.getActive() != null) updateBossBar(spawnBucketManager.getActive());
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
            activeBoost.getBossBar().hideBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    boostEndEmbed,
                    activeBoost
            );
        }
        T nextBoost = queue.poll();
        boostManager.setActive(nextBoost);
        if (nextBoost != null) {
            nextBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    boostStartEmbed,
                    nextBoost
            );
        }
        CacheConfig.setGlobalBoostData();
    }

    private static void updateBossBar(IBoost boost) {
        float progressRate = 1.0F / (boost.getDuration() * 20L);
        float total = progressRate * boost.getTimeRemaining();

        if (total < 0F)
            total = 0F;
        if (total > 1F)
            total = 1F;

        boost.getBossBar().updateProgress(total);
        boost.getBossBar().setName(boost.getBossBarText());

        // if server tick is multiple of 20
        MinecraftServer server = MatthiesenLibApi.getMinecraftServer();
        if (server.getTickCount() % 20 == 0) {
            boost.getBossBar().verifyPlayerList(server.getPlayerList());
        }
    }
}
