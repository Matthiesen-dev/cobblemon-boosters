package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.cobblemon_boosters.common.boosts.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.queue.QueuePrioritySettings;
import dev.matthiesen.matthiesen_core.common.api.events.config.ConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public final class BoostersConfig {
    public static final CoreServerConfig CORE_SERVER_CONFIG;
    public static final ModConfigSpec CORE_SERVER_SPEC;

    public static final CacheServerConfig CACHE_SERVER_CONFIG;
    public static final ModConfigSpec CACHE_SERVER_SPEC;

    public static final WebhooksServerConfig WEBHOOKS_SERVER_CONFIG;
    public static final ModConfigSpec WEBHOOKS_SERVER_SPEC;

    public static final PermissionsStartupConfig PERMISSIONS_STARTUP_CONFIG;
    public static final ModConfigSpec PERMISSIONS_STARTUP_SPEC;

    static {
        Pair<CoreServerConfig, ModConfigSpec> serverPair = new ModConfigSpec.Builder().configure(CoreServerConfig::new);
        CORE_SERVER_CONFIG = serverPair.getLeft();
        CORE_SERVER_SPEC = serverPair.getRight();

        Pair<CacheServerConfig, ModConfigSpec> cachePair = new ModConfigSpec.Builder().configure(CacheServerConfig::new);
        CACHE_SERVER_CONFIG = cachePair.getLeft();
        CACHE_SERVER_SPEC = cachePair.getRight();

        Pair<WebhooksServerConfig, ModConfigSpec> webhooksPair = new ModConfigSpec.Builder().configure(WebhooksServerConfig::new);
        WEBHOOKS_SERVER_CONFIG = webhooksPair.getLeft();
        WEBHOOKS_SERVER_SPEC = webhooksPair.getRight();

        Pair<PermissionsStartupConfig, ModConfigSpec> permissionsPair = new ModConfigSpec.Builder().configure(PermissionsStartupConfig::new);
        PERMISSIONS_STARTUP_CONFIG = permissionsPair.getLeft();
        PERMISSIONS_STARTUP_SPEC = permissionsPair.getRight();
    }

    public static void onConfigLoad(ConfigEvent.Loading event) {
        if (event.config().spec() == BoostersConfig.CACHE_SERVER_SPEC) {
            CacheServerConfig.loadFromConfig();
        }
    }

    public static BoostMessagesConfig getShinyMessages() {
        return new BoostMessagesConfig(
                CORE_SERVER_CONFIG.messages_shiny_barColor.get(),
                CORE_SERVER_CONFIG.messages_shiny_barOverlay.get(),
                CORE_SERVER_CONFIG.messages_shiny_barText.get(),
                CORE_SERVER_CONFIG.messages_shiny_noActiveBoosts.get(),
                CORE_SERVER_CONFIG.messages_shiny_boostStarted.get(),
                CORE_SERVER_CONFIG.messages_shiny_boostAddedToQueue.get(),
                CORE_SERVER_CONFIG.messages_shiny_boostStopped.get(),
                CORE_SERVER_CONFIG.messages_shiny_boostQueueCleared.get(),
                CORE_SERVER_CONFIG.messages_shiny_boostInfo.get(),
                CORE_SERVER_CONFIG.messages_shiny_noQueuedBoosts.get(),
                CORE_SERVER_CONFIG.messages_shiny_sidebarLine.get()
        );
    }

    public static BoostMessagesConfig getCatchMessages() {
        return new BoostMessagesConfig(
                CORE_SERVER_CONFIG.messages_catch_barColor.get(),
                CORE_SERVER_CONFIG.messages_catch_barOverlay.get(),
                CORE_SERVER_CONFIG.messages_catch_barText.get(),
                CORE_SERVER_CONFIG.messages_catch_noActiveBoosts.get(),
                CORE_SERVER_CONFIG.messages_catch_boostStarted.get(),
                CORE_SERVER_CONFIG.messages_catch_boostAddedToQueue.get(),
                CORE_SERVER_CONFIG.messages_catch_boostStopped.get(),
                CORE_SERVER_CONFIG.messages_catch_boostQueueCleared.get(),
                CORE_SERVER_CONFIG.messages_catch_boostInfo.get(),
                CORE_SERVER_CONFIG.messages_catch_noQueuedBoosts.get(),
                CORE_SERVER_CONFIG.messages_catch_sidebarLine.get()
        );
    }

    public static BoostMessagesConfig getExperienceMessages() {
        return new BoostMessagesConfig(
                CORE_SERVER_CONFIG.messages_experience_barColor.get(),
                CORE_SERVER_CONFIG.messages_experience_barOverlay.get(),
                CORE_SERVER_CONFIG.messages_experience_barText.get(),
                CORE_SERVER_CONFIG.messages_experience_noActiveBoosts.get(),
                CORE_SERVER_CONFIG.messages_experience_boostStarted.get(),
                CORE_SERVER_CONFIG.messages_experience_boostAddedToQueue.get(),
                CORE_SERVER_CONFIG.messages_experience_boostStopped.get(),
                CORE_SERVER_CONFIG.messages_experience_boostQueueCleared.get(),
                CORE_SERVER_CONFIG.messages_experience_boostInfo.get(),
                CORE_SERVER_CONFIG.messages_experience_noQueuedBoosts.get(),
                CORE_SERVER_CONFIG.messages_experience_sidebarLine.get()
        );
    }

    public static BoostMessagesConfig getSpawnBucketMessages() {
        return new BoostMessagesConfig(
                CORE_SERVER_CONFIG.messages_spawnBucket_barColor.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_barOverlay.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_barText.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_noActiveBoosts.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_boostStarted.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_boostAddedToQueue.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_boostStopped.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_boostQueueCleared.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_boostInfo.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_noQueuedBoosts.get(),
                CORE_SERVER_CONFIG.messages_spawnBucket_sidebarLine.get()
        );
    }

    private static final DiscordEmbed.DiscordAuthor DefaultAuthor = new DiscordEmbed.DiscordAuthor(
            "%discord_webhook_author_name%",
            "%discord_webhook_author_icon_url%"
    );

    private static final List<DiscordEmbed.DiscordEmbedField> MultiplierFields = List.of(
            new DiscordEmbed.DiscordEmbedField("Multiplier", "%multiplier%x", true),
            new DiscordEmbed.DiscordEmbedField("Duration", "%duration%", true)
    );

    public static DiscordEmbed getCatchEventStartEmbed() {
        return new DiscordEmbed(
                WEBHOOKS_SERVER_CONFIG.catch_start_title.get(),
                WEBHOOKS_SERVER_CONFIG.catch_start_description.get(),
                WEBHOOKS_SERVER_CONFIG.catch_start_embedColor.get().getValue(),
                DefaultAuthor,
                MultiplierFields,
                "%timestamp%"
        );
    }

    public static DiscordEmbed getCatchEventEndEmbed() {
        return new DiscordEmbed(
                WEBHOOKS_SERVER_CONFIG.catch_end_title.get(),
                WEBHOOKS_SERVER_CONFIG.catch_end_description.get(),
                WEBHOOKS_SERVER_CONFIG.catch_end_embedColor.get().getValue(),
                DefaultAuthor,
                null,
                "%timestamp%"
        );
    }

    public static DiscordEmbed getExperienceEventStartEmbed() {
        return new DiscordEmbed(
                WEBHOOKS_SERVER_CONFIG.experience_start_title.get(),
                WEBHOOKS_SERVER_CONFIG.experience_start_description.get(),
                WEBHOOKS_SERVER_CONFIG.experience_start_embedColor.get().getValue(),
                DefaultAuthor,
                MultiplierFields,
                "%timestamp%"
        );
    }

    public static DiscordEmbed getExperienceEventEndEmbed() {
        return new DiscordEmbed(
                WEBHOOKS_SERVER_CONFIG.experience_end_title.get(),
                WEBHOOKS_SERVER_CONFIG.experience_end_description.get(),
                WEBHOOKS_SERVER_CONFIG.experience_end_embedColor.get().getValue(),
                DefaultAuthor,
                null,
                "%timestamp%"
        );
    }

    public static DiscordEmbed getShinyEventStartEmbed() {
        return new DiscordEmbed(
                WEBHOOKS_SERVER_CONFIG.shiny_start_title.get(),
                WEBHOOKS_SERVER_CONFIG.shiny_start_description.get(),
                WEBHOOKS_SERVER_CONFIG.shiny_start_embedColor.get().getValue(),
                DefaultAuthor,
                MultiplierFields,
                "%timestamp%"
        );
    }

    public static DiscordEmbed getShinyEventEndEmbed() {
        return new DiscordEmbed(
                WEBHOOKS_SERVER_CONFIG.shiny_end_title.get(),
                WEBHOOKS_SERVER_CONFIG.shiny_end_description.get(),
                WEBHOOKS_SERVER_CONFIG.shiny_end_embedColor.get().getValue(),
                DefaultAuthor,
                null,
                "%timestamp%"
        );
    }

    public static DiscordEmbed getSpawnBucketEventStartEmbed() {
        List<DiscordEmbed.DiscordEmbedField> fields = List.of(
                new DiscordEmbed.DiscordEmbedField("Bucket", "%bucket%", true),
                new DiscordEmbed.DiscordEmbedField("Duration", "%duration%", true)
        );
        return new DiscordEmbed(
                WEBHOOKS_SERVER_CONFIG.bucket_start_title.get(),
                WEBHOOKS_SERVER_CONFIG.bucket_start_description.get(),
                WEBHOOKS_SERVER_CONFIG.bucket_start_embedColor.get().getValue(),
                DefaultAuthor,
                fields,
                "%timestamp%"
        );
    }

    public static DiscordEmbed getSpawnBucketEventEndEmbed() {
        return new DiscordEmbed(
                WEBHOOKS_SERVER_CONFIG.bucket_end_title.get(),
                WEBHOOKS_SERVER_CONFIG.bucket_end_description.get(),
                WEBHOOKS_SERVER_CONFIG.bucket_end_embedColor.get().getValue(),
                DefaultAuthor,
                null,
                "%timestamp%"
        );
    }

    public static ShinyBoost getActiveShinyBoost() {
        return CacheServerConfig.getActiveShinyBoost();
    }

    public static CatchBoost getActiveCatchBoost() {
        return CacheServerConfig.getActiveCatchBoost();
    }

    public static ExperienceBoost getActiveExperienceBoost() {
        return CacheServerConfig.getActiveExperienceBoost();
    }

    public static SpawnBucketBoost getActiveSpawnBucketBoost() {
        return CacheServerConfig.getActiveSpawnBucketBoost();
    }

    public static List<ShinyBoost> getQueuedShinyBoosts() {
        return CacheServerConfig.getQueuedShinyBoosts();
    }

    public static List<CatchBoost> getQueuedCatchBoosts() {
        return CacheServerConfig.getQueuedCatchBoosts();
    }

    public static List<ExperienceBoost> getQueuedExperienceBoosts() {
        return CacheServerConfig.getQueuedExperienceBoosts();
    }

    public static List<SpawnBucketBoost> getQueuedSpawnBucketBoosts() {
        return CacheServerConfig.getQueuedSpawnBucketBoosts();
    }

    public static void saveCacheToConfig() {
        CacheServerConfig.saveToConfig();
    }

    public static QueuePrioritySettings getQueuePrioritySettings() {
        return new QueuePrioritySettings(
                BoostersConfig.CORE_SERVER_CONFIG.queuePriorityEnabled.getAsBoolean(),
                BoostersConfig.CORE_SERVER_CONFIG.queuePriorityMode.get(),
                BoostersConfig.CORE_SERVER_CONFIG.timePriorityDirection.get(),
                BoostersConfig.CORE_SERVER_CONFIG.activePreemptionEnabled.getAsBoolean()
        );
    }
}
