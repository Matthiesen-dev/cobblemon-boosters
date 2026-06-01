package dev.matthiesen.common.cobblemon_boosters.managers;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.config.WebhooksConfig;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.server.MinecraftServer;

import java.util.Queue;
import java.util.function.Consumer;

public final class TickManager {
    private static <T extends IBoost> void handleBoostTick(
            T activeBoost,
            Queue<T> queue,
            Consumer<T> setActiveBoost,
            WebhooksConfig.DiscordEmbed boostEndEmbed,
            WebhooksConfig.DiscordEmbed boostStartEmbed
    ) {
        if (activeBoost == null) return;
        decrementBoost(activeBoost);
        if (activeBoost.getTimeRemaining() > 0) return;
        activeBoost.getBossBar().hideBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
        CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                boostEndEmbed,
                activeBoost
        );
        T nextBoost = queue.poll();
        setActiveBoost.accept(nextBoost);
        if (nextBoost != null) {
            nextBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    boostStartEmbed,
                    nextBoost
            );
        }
        CacheConfig.setGlobalBoostData();
    }

    public static void tickBoosts() {
        BoostManager bm = CobblemonBoosters.INSTANCE.boostManager;
        var webhooks = CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;

        BoostManager.IBoostManager<ShinyBoost> shiny = bm.getShinyBoostManager();
        handleBoostTick(
                shiny.getActive(),
                shiny.getQueue(),
                shiny::setActive,
                webhooks.shinyEventEndEmbed,
                webhooks.shinyEventStartEmbed
        );

        BoostManager.IBoostManager<CatchBoost> catch_ = bm.getCatchBoostManager();
        handleBoostTick(
                catch_.getActive(),
                catch_.getQueue(),
                catch_::setActive,
                webhooks.catchEventEndEmbed,
                webhooks.catchEventStartEmbed
        );

        BoostManager.IBoostManager<ExperienceBoost> experience = bm.getExperienceBoostManager();
        handleBoostTick(
                experience.getActive(),
                experience.getQueue(),
                experience::setActive,
                webhooks.experienceEventEndEmbed,
                webhooks.experienceEventStartEmbed
        );

        BoostManager.IBoostManager<SpawnBucketBoost> spawnBucket = bm.getSpawnBucketBoostManager();
        handleBoostTick(
                spawnBucket.getActive(),
                spawnBucket.getQueue(),
                spawnBucket::setActive,
                webhooks.spawnBucketEventEndEmbed,
                webhooks.spawnBucketEventStartEmbed
        );
    }

    public static void updateBossBars() {
        BoostManager bm = CobblemonBoosters.INSTANCE.boostManager;

        if (bm.getShinyBoostManager().getActive() != null) updateBossBar(bm.getShinyBoostManager().getActive());
        if (bm.getCatchBoostManager().getActive() != null) updateBossBar(bm.getCatchBoostManager().getActive());
        if (bm.getExperienceBoostManager().getActive() != null) updateBossBar(bm.getExperienceBoostManager().getActive());
        if (bm.getSpawnBucketBoostManager().getActive() != null) updateBossBar(bm.getSpawnBucketBoostManager().getActive());
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

        boost.getBossBar().updateProgress(total);
        boost.getBossBar().setName(boost.getBossBarText());

        // if server tick is multiple of 20
        MinecraftServer server = MatthiesenLibApi.getMinecraftServer();
        if (server.getTickCount() % 20 == 0) {
            boost.getBossBar().verifyPlayerList(server.getPlayerList());
        }
    }
}
