package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class CacheConfig {
    @SerializedName("activeShinyBoost")
    public ShinyBoost activeShinyBoost = null;

    @SerializedName("activeCatchBoost")
    public CatchBoost activeCatchBoost = null;

    @SerializedName("activeExperienceBoost")
    public ExperienceBoost activeExperienceBoost = null;

    @SerializedName("activeSpawnBucketBoost")
    public SpawnBucketBoost activeSpawnBucketBoost = null;

    @SerializedName("queuedShinyBoosts")
    public List<ShinyBoost> queuedShinyBoosts = new ArrayList<>();

    @SerializedName("queuedCatchBoosts")
    public List<CatchBoost> queuedCatchBoosts = new ArrayList<>();

    @SerializedName("queuedExperienceBoosts")
    public List<ExperienceBoost> queuedExperienceBoosts = new ArrayList<>();

    @SerializedName("queuedSpawnBucketBoosts")
    public List<SpawnBucketBoost> queuedSpawnBucketBoosts = new ArrayList<>();

    public CacheConfig() {}

    public CacheConfig(
            ShinyBoost activeShinyBoost, CatchBoost activeCatchBoost, ExperienceBoost activeExperienceBoost, SpawnBucketBoost activeSpawnBucketBoost,
            List<ShinyBoost> queuedShinyBoosts, List<CatchBoost> queuedCatchBoosts, List<ExperienceBoost> queuedExperienceBoosts, List<SpawnBucketBoost> queuedSpawnBucketBoosts
    ) {
        this.activeShinyBoost = activeShinyBoost;
        this.activeCatchBoost = activeCatchBoost;
        this.activeExperienceBoost = activeExperienceBoost;
        this.activeSpawnBucketBoost = activeSpawnBucketBoost;
        this.queuedShinyBoosts = queuedShinyBoosts;
        this.queuedCatchBoosts = queuedCatchBoosts;
        this.queuedExperienceBoosts = queuedExperienceBoosts;
        this.queuedSpawnBucketBoosts = queuedSpawnBucketBoosts;
    }

    public static void setGlobalBoostData() {
        CobblemonBoosters.INSTANCE.CACHE_CONFIG_MANAGER.setConfig(new CacheConfig(
                CobblemonBoosters.INSTANCE.activeShinyBoost,
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.activeExperienceBoost,
                CobblemonBoosters.INSTANCE.activeSpawnBucketBoost,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts.stream().toList(),
                CobblemonBoosters.INSTANCE.queuedCatchBoosts.stream().toList(),
                CobblemonBoosters.INSTANCE.queuedExperienceBoosts.stream().toList(),
                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts.stream().toList()
        ));
    }

    @SuppressWarnings("unused")
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
