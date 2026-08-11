package dev.matthiesen.cobblemon_boosters.common.services.managers;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.config.*;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.matthiesen_core.common.api.events.server.ServerEvent;
import net.minecraft.server.MinecraftServer;

public final class TickManager {
    private static int tickCounter = 0;

    public static int getSaveIntervalTicks() {
        return BoostersConfig.CORE_SERVER_CONFIG.saveIntervalTicks.getAsInt();
    }

    @SuppressWarnings("unused")
    public static void onEndTick(ServerEvent.EndTick event) {
        tick();
    }

    public static void tick() {
        try {
            tickBoosts();
            MinecraftServer server = CobblemonBoostersCommon.INSTANCE.getCommonUtils().getServer();
            if (server != null) {
                ServiceManager.getDisplayService().tick(server);
            }
            tickCounter++;
            var saveInterval = getSaveIntervalTicks();
            if (tickCounter >= saveInterval) {
                tickCounter = 0;
                if (BoostersConfig.CORE_SERVER_CONFIG.verboseCacheLogging.get()) {
                    CobblemonBoostersCommon.INSTANCE.createInfoLog("Saving Boosters to Cache...");
                }
                BoostersConfig.saveCacheToConfig();
            }
        } catch (IllegalArgumentException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Caught BossBar exception! ", e);
        }
    }

    public static void tickBoosts() {
        handleBoostTick(
                BoostManager.getShinyBoostManager(),
                BoostersConfig.getShinyEventEndEmbed(),
                BoostersConfig.getShinyEventStartEmbed()
        );

        handleBoostTick(
                BoostManager.getCatchBoostManager(),
                BoostersConfig.getCatchEventEndEmbed(),
                BoostersConfig.getCatchEventStartEmbed()
        );

        handleBoostTick(
                BoostManager.getExperienceBoostManager(),
                BoostersConfig.getExperienceEventEndEmbed(),
                BoostersConfig.getExperienceEventStartEmbed()
        );

        handleBoostTick(
                BoostManager.getSpawnBucketBoostManager(),
                BoostersConfig.getSpawnBucketEventEndEmbed(),
                BoostersConfig.getSpawnBucketEventStartEmbed()
        );
    }

    private static void decrementBoost(IBoost boost) {
        boost.setTimeRemaining(boost.getTimeRemaining() - 1);
    }

    private static <T extends IBoost> void handleBoostTick(
            BoostManager.IBoostManager<T> boostManager,
            DiscordEmbed boostEndEmbed,
            DiscordEmbed boostStartEmbed
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
        CacheServerConfig.setGlobalBoostData();
    }
}
