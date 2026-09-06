package dev.matthiesen.cobblemon_boosters.common.utils;

import com.cobblemon.mod.common.api.events.entity.SpawnBucketChosenEvent;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.SpawnBucketBoost;

import java.util.*;

public final class SpawnBucketOverrideSelector {

    private SpawnBucketOverrideSelector() {}

    public static String recalculateOverrideBucket(
            SpawnBucketChosenEvent event,
            SpawnBucketBoost activeBoost
    ) {
        Objects.requireNonNull(event, "SpawnBucketChosenEvent");
        Objects.requireNonNull(activeBoost, "SpawnBucketBoost");

        String originalChosenBucket = event.getBucket();
        Map<String, Float> originalWeights = event.getBucketWeights();
        String boostedBucketName = activeBoost.getBucketName();
        float boostMultiplier = activeBoost.getMultiplier();

        if (originalWeights.isEmpty()) {
            return originalChosenBucket;
        }
        if (Float.isNaN(boostMultiplier) || Float.isInfinite(boostMultiplier) || boostMultiplier < 0f) {
            return originalChosenBucket;
        }

        Map<String, Float> adjusted = new LinkedHashMap<>();
        for (Map.Entry<String, Float> e : originalWeights.entrySet()) {
            float w = e.getValue() == null ? 0f : e.getValue();
            if (Float.isNaN(w) || Float.isInfinite(w) || w < 0f) w = 0f;
            adjusted.put(e.getKey(), w);
        }

        // Apply boost to the user-selected target bucket.
        if (boostedBucketName == null) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Could not find Spawn Bucket with empty name. Check your config for typos.");
            return originalChosenBucket;
        }
        float base = adjusted.getOrDefault(boostedBucketName, 0f);
        adjusted.put(boostedBucketName, base * boostMultiplier);

        Random random = new Random();

        String rerolled = weightedPick(adjusted, random);
        return rerolled != null ? rerolled : originalChosenBucket;
    }

    private static String weightedPick(Map<String, Float> weights, Random random) {
        float total = 0f;
        for (float w : weights.values()) {
            if (w > 0f) total += w;
        }
        if (total <= 0f) return null;

        float roll = random.nextFloat() * total;
        float cumulative = 0f;

        for (Map.Entry<String, Float> e : weights.entrySet()) {
            float w = e.getValue();
            if (w <= 0f) continue;
            cumulative += w;
            if (roll <= cumulative) return e.getKey();
        }

        String fallback = null;
        for (Map.Entry<String, Float> e : weights.entrySet()) {
            if (e.getValue() > 0f) fallback = e.getKey();
        }
        return fallback;
    }
}