package dev.matthiesen.cobblemon_boosters.common.services;

import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.boosts.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.Booster;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;

import java.util.ArrayList;
import java.util.List;

public final class BoostController {
    private static final List<Booster<?>> REGISTERED_BOOSTERS = new ArrayList<>();

    public static void registerBooster(Booster<?> booster) {
        REGISTERED_BOOSTERS.add(booster);
    }

    public static void setupSubscribers() {
        for (Booster<?> booster : REGISTERED_BOOSTERS) {
            booster.setupSubscriber();
        }
    }

    public static void teardownSubscribers() {
        for (Booster<?> booster : REGISTERED_BOOSTERS) {
            booster.teardownSubscriber();
        }
    }

    public static void refreshQueuePriorities() {
        for (Booster<?> booster : REGISTERED_BOOSTERS) {
            booster.refreshQueuePriority();
        }
    }

    public static void tickBoosts() {
        for (Booster<?> booster : REGISTERED_BOOSTERS) {
            booster.tickBoosts();
        }
    }

    public static List<IBoost> getActiveBoosts() {
        List<IBoost> activeBoosts = new ArrayList<>();
        addIfActive(activeBoosts, Constants.SupportedBoosterTypes.SHINY);
        addIfActive(activeBoosts, Constants.SupportedBoosterTypes.CATCH);
        addIfActive(activeBoosts, Constants.SupportedBoosterTypes.EXPERIENCE);
        addIfActive(activeBoosts, Constants.SupportedBoosterTypes.SPAWN_BUCKET);
        return activeBoosts;
    }

    public static Booster<ShinyBoost> getShinyBoostManager() {
        return getBoosterByType(Constants.SupportedBoosterTypes.SHINY);
    }

    public static Booster<CatchBoost> getCatchBoostManager() {
        return getBoosterByType(Constants.SupportedBoosterTypes.CATCH);
    }

    public static Booster<ExperienceBoost> getExperienceBoostManager() {
        return getBoosterByType(Constants.SupportedBoosterTypes.EXPERIENCE);
    }

    public static Booster<SpawnBucketBoost> getSpawnBucketBoostManager() {
        return getBoosterByType(Constants.SupportedBoosterTypes.SPAWN_BUCKET);
    }

    private static void addIfActive(List<IBoost> activeBoosts, Constants.SupportedBoosterTypes type) {
        Booster<?> booster = getBoosterByType(type);
        if (booster != null) {
            IBoost activeBoost = booster.getActiveBoost();
            if (activeBoost != null) {
                activeBoosts.add(activeBoost);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Booster<?>> T getBoosterByType(Constants.SupportedBoosterTypes type) {
        for (Booster<?> booster : REGISTERED_BOOSTERS) {
            if (booster.getType() == type) {
                return (T) booster;
            }
        }
        return null;
    }
}
