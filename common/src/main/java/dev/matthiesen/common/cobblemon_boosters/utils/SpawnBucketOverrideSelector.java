package dev.matthiesen.common.cobblemon_boosters.utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.events.entity.SpawnBucketChosenEvent;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;

import java.util.*;

public final class SpawnBucketOverrideSelector {

    private SpawnBucketOverrideSelector() {}

    public static SpawnBucket getBucketByName(String name) {
        List<SpawnBucket> buckets = Cobblemon.INSTANCE.getBestSpawner().getConfig().getBuckets();
        for (SpawnBucket bucket : buckets) {
            if (bucket.getName().equalsIgnoreCase(name)) {
                return bucket;
            }
        }
        return null;
    }

    public static SpawnBucket recalculateOverrideBucket(
            SpawnBucketChosenEvent event,
            SpawnBucketBoost activeBoost
    ) {
        Objects.requireNonNull(event, "SpawnBucketChosenEvent");
        Objects.requireNonNull(activeBoost, "SpawnBucketBoost");

        SpawnBucket originalChosenBucket = event.getBucket();
        Map<SpawnBucket, Float> originalWeights = event.getBucketWeights();
        String boostedBucketName = activeBoost.getBucketDisplayName();
        float boostMultiplier = activeBoost.getMultiplier();

        if (originalWeights.isEmpty()) {
            return originalChosenBucket;
        }
        if (Float.isNaN(boostMultiplier) || Float.isInfinite(boostMultiplier) || boostMultiplier < 0f) {
            return originalChosenBucket;
        }

        Map<SpawnBucket, Float> adjusted = new LinkedHashMap<>();
        for (Map.Entry<SpawnBucket, Float> e : originalWeights.entrySet()) {
            float w = e.getValue() == null ? 0f : e.getValue();
            if (Float.isNaN(w) || Float.isInfinite(w) || w < 0f) w = 0f;
            adjusted.put(e.getKey(), w);
        }

        // Apply boost to the user-selected target bucket.
        SpawnBucket boostedBucket = getBucketByName(boostedBucketName);
        if (boostedBucket == null) {
            Constants.createErrorLog("Could not find Spawn Bucket with name '" + boostedBucketName + "'. Check your config for typos.");
            return originalChosenBucket;
        }
        float base = adjusted.getOrDefault(boostedBucket, 0f);
        adjusted.put(boostedBucket, base * boostMultiplier);

        Random random = new Random();

        SpawnBucket rerolled = weightedPick(adjusted, random);
        return rerolled != null ? rerolled : originalChosenBucket;
    }

    private static SpawnBucket weightedPick(Map<SpawnBucket, Float> weights, Random random) {
        float total = 0f;
        for (float w : weights.values()) {
            if (w > 0f) total += w;
        }
        if (total <= 0f) return null;

        float roll = random.nextFloat() * total;
        float cumulative = 0f;

        for (Map.Entry<SpawnBucket, Float> e : weights.entrySet()) {
            float w = e.getValue();
            if (w <= 0f) continue;
            cumulative += w;
            if (roll <= cumulative) return e.getKey();
        }

        SpawnBucket fallback = null;
        for (Map.Entry<SpawnBucket, Float> e : weights.entrySet()) {
            if (e.getValue() > 0f) fallback = e.getKey();
        }
        return fallback;
    }
}