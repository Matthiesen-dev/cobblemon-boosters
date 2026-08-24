package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.cobblemon_boosters.common.services.boosts.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.services.BoostController;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class CacheServerConfig {
    private static volatile ShinyBoost activeShinyBoost = null;
    private static volatile CatchBoost activeCatchBoost = null;
    private static volatile ExperienceBoost activeExperienceBoost = null;
    private static volatile SpawnBucketBoost activeSpawnBucketBoost = null;

    private static List<ShinyBoost> queuedShinyBoosts = Collections.emptyList();
    private static List<CatchBoost> queuedCatchBoosts = Collections.emptyList();
    private static List<ExperienceBoost> queuedExperienceBoosts = Collections.emptyList();
    private static List<SpawnBucketBoost> queuedSpawnBucketBoosts = Collections.emptyList();

    public static void setActiveShinyBoost(ShinyBoost boost) {
        activeShinyBoost = boost;
    }

    public static void setActiveCatchBoost(CatchBoost boost) {
        activeCatchBoost = boost;
    }

    public static void setActiveExperienceBoost(ExperienceBoost boost) {
        activeExperienceBoost = boost;
    }

    public static void setActiveSpawnBucketBoost(SpawnBucketBoost boost) {
        activeSpawnBucketBoost = boost;
    }

    public static ShinyBoost getActiveShinyBoost() {
        return activeShinyBoost;
    }

    public static CatchBoost getActiveCatchBoost() {
        return activeCatchBoost;
    }

    public static ExperienceBoost getActiveExperienceBoost() {
        return activeExperienceBoost;
    }

    public static SpawnBucketBoost getActiveSpawnBucketBoost() {
        return activeSpawnBucketBoost;
    }

    public static void setQueuedShinyBoosts(List<ShinyBoost> boosts) {
        queuedShinyBoosts = boosts;
    }

    public static void setQueuedCatchBoosts(List<CatchBoost> boosts) {
        queuedCatchBoosts = boosts;
    }

    public static void setQueuedExperienceBoosts(List<ExperienceBoost> boosts) {
        queuedExperienceBoosts = boosts;
    }

    public static void setQueuedSpawnBucketBoosts(List<SpawnBucketBoost> boosts) {
        queuedSpawnBucketBoosts = boosts;
    }

    public static List<ShinyBoost> getQueuedShinyBoosts() {
        return queuedShinyBoosts;
    }

    public static List<CatchBoost> getQueuedCatchBoosts() {
        return queuedCatchBoosts;
    }

    public static List<ExperienceBoost> getQueuedExperienceBoosts() {
        return queuedExperienceBoosts;
    }

    public static List<SpawnBucketBoost> getQueuedSpawnBucketBoosts() {
        return queuedSpawnBucketBoosts;
    }

    public static void setGlobalBoostData() {
        var shinyBoostManager = BoostController.getShinyBoostManager();
        if (shinyBoostManager != null) {
            setActiveShinyBoost(shinyBoostManager.getActiveBoost());
            setQueuedShinyBoosts(shinyBoostManager.getBoostQueueAsList());
        }

        var catchBoostManager = BoostController.getCatchBoostManager();
        if (catchBoostManager != null) {
            setActiveCatchBoost(catchBoostManager.getActiveBoost());
            setQueuedCatchBoosts(catchBoostManager.getBoostQueueAsList());
        }

        var experienceBoostManager = BoostController.getExperienceBoostManager();
        if (experienceBoostManager != null) {
            setActiveExperienceBoost(experienceBoostManager.getActiveBoost());
            setQueuedExperienceBoosts(experienceBoostManager.getBoostQueueAsList());
        }

        var spawnBucketBoostManager = BoostController.getSpawnBucketBoostManager();
        if (spawnBucketBoostManager != null) {
            setActiveSpawnBucketBoost(spawnBucketBoostManager.getActiveBoost());
            setQueuedSpawnBucketBoosts(spawnBucketBoostManager.getBoostQueueAsList());
        }

        saveToConfig();
    }

    public static void loadFromConfig() {
        var cacheConfig = BoostersConfig.CACHE_SERVER_CONFIG;

        setActiveShinyBoost(ShinyBoost.fromString(cacheConfig.raw_activeShinyBoost.get()).orElse(null));
        setActiveCatchBoost(CatchBoost.fromString(cacheConfig.raw_activeCatchBoost.get()).orElse(null));
        setActiveExperienceBoost(ExperienceBoost.fromString(cacheConfig.raw_activeExperienceBoost.get()).orElse(null));
        setActiveSpawnBucketBoost(SpawnBucketBoost.fromString(cacheConfig.raw_activeSpawnBucketBoost.get()).orElse(null));

        setQueuedShinyBoosts(cacheConfig.raw_queuedShinyBoosts.get().stream()
                .map(ShinyBoost::fromString)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());
        setQueuedCatchBoosts(cacheConfig.raw_queuedCatchBoosts.get().stream()
                .map(CatchBoost::fromString)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());
        setQueuedExperienceBoosts(cacheConfig.raw_queuedExperienceBoosts.get().stream()
                .map(ExperienceBoost::fromString)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());
        setQueuedSpawnBucketBoosts(cacheConfig.raw_queuedSpawnBucketBoosts.get().stream()
                .map(SpawnBucketBoost::fromString)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());
    }

    public static void saveToConfig() {
        var cacheConfig = BoostersConfig.CACHE_SERVER_CONFIG;

        cacheConfig.raw_activeShinyBoost.set(activeShinyBoost != null ? activeShinyBoost.serialize() : "");
        cacheConfig.raw_activeShinyBoost.save();
        cacheConfig.raw_activeCatchBoost.set(activeCatchBoost != null ? activeCatchBoost.serialize() : "");
        cacheConfig.raw_activeCatchBoost.save();
        cacheConfig.raw_activeExperienceBoost.set(activeExperienceBoost != null ? activeExperienceBoost.serialize() : "");
        cacheConfig.raw_activeExperienceBoost.save();
        cacheConfig.raw_activeSpawnBucketBoost.set(activeSpawnBucketBoost != null ? activeSpawnBucketBoost.serialize() : "");
        cacheConfig.raw_activeSpawnBucketBoost.save();

        cacheConfig.raw_queuedShinyBoosts.set(queuedShinyBoosts.stream()
                .map(ShinyBoost::serialize)
                .toList());
        cacheConfig.raw_queuedShinyBoosts.save();
        cacheConfig.raw_queuedCatchBoosts.set(queuedCatchBoosts.stream()
                .map(CatchBoost::serialize)
                .toList());
        cacheConfig.raw_queuedCatchBoosts.save();
        cacheConfig.raw_queuedExperienceBoosts.set(queuedExperienceBoosts.stream()
                .map(ExperienceBoost::serialize)
                .toList());
        cacheConfig.raw_queuedExperienceBoosts.save();
        cacheConfig.raw_queuedSpawnBucketBoosts.set(queuedSpawnBucketBoosts.stream()
                .map(SpawnBucketBoost::serialize)
                .toList());
        cacheConfig.raw_queuedSpawnBucketBoosts.save();
    }

    public ModConfigSpec.ConfigValue<String> raw_activeShinyBoost;
    public ModConfigSpec.ConfigValue<String> raw_activeCatchBoost;
    public ModConfigSpec.ConfigValue<String> raw_activeExperienceBoost;
    public ModConfigSpec.ConfigValue<String> raw_activeSpawnBucketBoost;

    public ModConfigSpec.ConfigValue<List<? extends String>> raw_queuedShinyBoosts;
    public ModConfigSpec.ConfigValue<List<? extends String>> raw_queuedCatchBoosts;
    public ModConfigSpec.ConfigValue<List<? extends String>> raw_queuedExperienceBoosts;
    public ModConfigSpec.ConfigValue<List<? extends String>> raw_queuedSpawnBucketBoosts;

    public CacheServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("Boosters Cache", "This file should not be edited manually").push("cache");

        builder.comment("Active Boosts").push("activeBoosts");
        raw_activeShinyBoost = builder.comment("The currently active Shiny Boost, serialized as a string")
                .define("activeShinyBoost", "");
        raw_activeCatchBoost = builder.comment("The currently active Catch Boost, serialized as a string")
                .define("activeCatchBoost", "");
        raw_activeExperienceBoost = builder.comment("The currently active Experience Boost, serialized as a string")
                .define("activeExperienceBoost", "");
        raw_activeSpawnBucketBoost = builder.comment("The currently active Spawn Bucket Boost, serialized as a string")
                .define("activeSpawnBucketBoost", "");
        builder.pop();

        builder.comment("Queued Boosts").push("queuedBoosts");
        raw_queuedShinyBoosts = builder.comment("The queued Shiny Boosts, serialized as a list of strings")
                .defineList(
                        "queuedShinyBoosts",
                        Collections.emptyList(),
                        () -> "",
                        o -> o instanceof String str && ShinyBoost.fromString(str).isPresent()
                );
        raw_queuedCatchBoosts = builder.comment("The queued Catch Boosts, serialized as a list of strings")
                .defineList(
                        "queuedCatchBoosts",
                        Collections.emptyList(),
                        () -> "",
                        o -> o instanceof String str && CatchBoost.fromString(str).isPresent()
                );
        raw_queuedExperienceBoosts = builder.comment("The queued Experience Boosts, serialized as a list of strings")
                .defineList(
                        "queuedExperienceBoosts",
                        Collections.emptyList(),
                        () -> "",
                        o -> o instanceof String str && ExperienceBoost.fromString(str).isPresent()
                );
        raw_queuedSpawnBucketBoosts = builder.comment("The queued Spawn Bucket Boosts, serialized as a list of strings")
                .defineList(
                        "queuedSpawnBucketBoosts",
                        Collections.emptyList(),
                        () -> "",
                        o -> o instanceof String str && SpawnBucketBoost.fromString(str).isPresent()
                );
        builder.pop();

        builder.pop();
    }
}
