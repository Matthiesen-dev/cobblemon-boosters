package dev.matthiesen.cobblemon_boosters.common.services;

import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.Booster;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BoostController {
    private static final List<Booster<?>> REGISTERED_BOOSTERS = new ArrayList<>();
    private static final Map<String, BoosterGuiDefinition<?>> REGISTERED_GUI_DEFINITIONS = new LinkedHashMap<>();

    public static void registerBooster(Booster<?> booster) {
        REGISTERED_BOOSTERS.add(booster);
    }

    public static void registerGuiDefinition(BoosterGuiDefinition<?> definition) {
        REGISTERED_GUI_DEFINITIONS.put(definition.getCommandId(), definition);
    }

    public static List<BoosterGuiDefinition<?>> getGuiDefinitions() {
        return List.copyOf(REGISTERED_GUI_DEFINITIONS.values());
    }

    public static List<String> getGuiDefinitionIds() {
        return List.copyOf(REGISTERED_GUI_DEFINITIONS.keySet());
    }

    public static BoosterGuiDefinition<?> getGuiDefinition(String commandId) {
        return REGISTERED_GUI_DEFINITIONS.get(commandId);
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
        for (Booster<?> booster : REGISTERED_BOOSTERS) {
            IBoost activeBoost = booster.getActiveBoost();
            if (activeBoost != null) {
                activeBoosts.add(activeBoost);
            }
        }
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


    @SuppressWarnings("unchecked")
    private static <T extends Booster<?>> T getBoosterByType(Constants.SupportedBoosterTypes type) {
        for (Booster<?> booster : REGISTERED_BOOSTERS) {
            if (booster.getType() == type) {
                return (T) booster;
            }
        }
        return null;
    }
}
