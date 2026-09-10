package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.services.BoostControllerServiceManager;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
        var shinyBoostManager = BoostControllerServiceManager.getShinyBoostManager();
        if (shinyBoostManager != null) {
            setActiveShinyBoost(shinyBoostManager.getActiveBoost());
            setQueuedShinyBoosts(shinyBoostManager.getBoostQueueAsList());
        }

        var catchBoostManager = BoostControllerServiceManager.getCatchBoostManager();
        if (catchBoostManager != null) {
            setActiveCatchBoost(catchBoostManager.getActiveBoost());
            setQueuedCatchBoosts(catchBoostManager.getBoostQueueAsList());
        }

        var experienceBoostManager = BoostControllerServiceManager.getExperienceBoostManager();
        if (experienceBoostManager != null) {
            setActiveExperienceBoost(experienceBoostManager.getActiveBoost());
            setQueuedExperienceBoosts(experienceBoostManager.getBoostQueueAsList());
        }

        var spawnBucketBoostManager = BoostControllerServiceManager.getSpawnBucketBoostManager();
        if (spawnBucketBoostManager != null) {
            setActiveSpawnBucketBoost(spawnBucketBoostManager.getActiveBoost());
            setQueuedSpawnBucketBoosts(spawnBucketBoostManager.getBoostQueueAsList());
        }

        logLifecycleDebug("Snapshot global state to cache memory");
        saveToConfig();
    }

    public static void loadFromConfig() {
        var cacheConfig = BoostersConfig.CACHE_SERVER_CONFIG;

        setActiveShinyBoost(parseActiveBoost("shiny", cacheConfig.raw_activeShinyBoost.get(), ShinyBoost::fromString));
        setActiveCatchBoost(parseActiveBoost("catch", cacheConfig.raw_activeCatchBoost.get(), CatchBoost::fromString));
        setActiveExperienceBoost(parseActiveBoost("experience", cacheConfig.raw_activeExperienceBoost.get(), ExperienceBoost::fromString));
        setActiveSpawnBucketBoost(parseActiveBoost("bucket", cacheConfig.raw_activeSpawnBucketBoost.get(), SpawnBucketBoost::fromString));

        setQueuedShinyBoosts(parseQueuedBoosts("shiny", cacheConfig.raw_queuedShinyBoosts.get(), ShinyBoost::fromString));
        setQueuedCatchBoosts(parseQueuedBoosts("catch", cacheConfig.raw_queuedCatchBoosts.get(), CatchBoost::fromString));
        setQueuedExperienceBoosts(parseQueuedBoosts("experience", cacheConfig.raw_queuedExperienceBoosts.get(), ExperienceBoost::fromString));
        setQueuedSpawnBucketBoosts(parseQueuedBoosts("bucket", cacheConfig.raw_queuedSpawnBucketBoosts.get(), SpawnBucketBoost::fromString));

        logLifecycleDebug("Loaded cache config -> active[shiny=" + describeBoost(activeShinyBoost)
                + ", catch=" + describeBoost(activeCatchBoost)
                + ", exp=" + describeBoost(activeExperienceBoost)
                + ", bucket=" + describeBoost(activeSpawnBucketBoost)
                + "] queueSizes[shiny=" + queuedShinyBoosts.size()
                + ", catch=" + queuedCatchBoosts.size()
                + ", exp=" + queuedExperienceBoosts.size()
                + ", bucket=" + queuedSpawnBucketBoosts.size() + "]");
    }

    private static <T extends IBoost> T parseActiveBoost(
            String boosterType,
            String raw,
            Function<String, Optional<T>> parser
    ) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        Optional<T> parsed = parser.apply(raw);
        if (parsed.isEmpty()) {
            logCacheWarning(boosterType, "active", "malformed", raw);
            return null;
        }

        T boost = parsed.get();
        if (boost.getTimeRemaining() <= 0) {
            logCacheWarning(boosterType, "active", "expired", raw);
            return null;
        }

        return boost;
    }

    private static <T extends IBoost> List<T> parseQueuedBoosts(
            String boosterType,
            List<? extends String> rawValues,
            Function<String, Optional<T>> parser
    ) {
        List<T> sanitized = new ArrayList<>();
        for (String raw : rawValues) {
            Optional<T> parsed = parser.apply(raw);
            if (parsed.isEmpty()) {
                logCacheWarning(boosterType, "queue", "malformed", raw);
                continue;
            }

            T boost = parsed.get();
            if (boost.getTimeRemaining() <= 0) {
                logCacheWarning(boosterType, "queue", "expired", raw);
                continue;
            }

            sanitized.add(boost);
        }
        return sanitized;
    }

    private static void logCacheWarning(String boosterType, String section, String reason, String rawValue) {
        CobblemonBoostersCommon.INSTANCE.createWarnLog(
                "[WARN] Dropping cached boost entry [type=" + boosterType
                        + ", section=" + section
                        + ", reason=" + reason
                        + ", raw='" + rawValue + "']"
        );
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

        logLifecycleDebug("Persisted cache config");
    }

    private static void logLifecycleDebug(String message) {
        if (BoostersConfig.CORE_SERVER_CONFIG.boosterLifecycleDebug.get()) {
            CobblemonBoostersCommon.INSTANCE.createInfoLog("[Lifecycle] [cache] " + message);
        }
    }

    private static String describeBoost(IBoost boost) {
        if (boost == null) {
            return "none";
        }
        return "mult=" + boost.getMultiplier() + ", rem=" + boost.getTimeRemaining();
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
