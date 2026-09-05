package dev.matthiesen.cobblemon_boosters.common.services;

import dev.matthiesen.cobblemon_boosters.common.interfaces.SupportedBoosterTypes;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoostController;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BoostControllerServiceManager {
    private static final List<IBoostController<?>> REGISTERED_BOOSTERS = new ArrayList<>();
    private static final Map<String, BoosterGuiDefinition<?>> REGISTERED_GUI_DEFINITIONS = new LinkedHashMap<>();

    public static void registerBooster(IBoostController<?> booster) {
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
        for (IBoostController<?> booster : REGISTERED_BOOSTERS) {
            booster.setupSubscriber();
        }
    }

    public static void teardownSubscribers() {
        for (IBoostController<?> booster : REGISTERED_BOOSTERS) {
            booster.teardownSubscriber();
        }
    }

    public static void refreshQueuePriorities() {
        for (IBoostController<?> booster : REGISTERED_BOOSTERS) {
            booster.refreshQueuePriority();
        }
    }

    public static void tickBoosts() {
        for (IBoostController<?> booster : REGISTERED_BOOSTERS) {
            booster.tickBoosts();
        }
    }

    public static List<IBoost> getActiveBoosts() {
        List<IBoost> activeBoosts = new ArrayList<>();
        for (IBoostController<?> booster : REGISTERED_BOOSTERS) {
            IBoost activeBoost = booster.getActiveBoost();
            if (activeBoost != null) {
                activeBoosts.add(activeBoost);
            }
        }
        return activeBoosts;
    }

    public static IBoostController<ShinyBoost> getShinyBoostManager() {
        return getBoosterByType(SupportedBoosterTypes.SHINY);
    }

    public static IBoostController<CatchBoost> getCatchBoostManager() {
        return getBoosterByType(SupportedBoosterTypes.CATCH);
    }

    public static IBoostController<ExperienceBoost> getExperienceBoostManager() {
        return getBoosterByType(SupportedBoosterTypes.EXPERIENCE);
    }

    public static IBoostController<SpawnBucketBoost> getSpawnBucketBoostManager() {
        return getBoosterByType(SupportedBoosterTypes.SPAWN_BUCKET);
    }


    @SuppressWarnings("unchecked")
    private static <T extends IBoostController<?>> T getBoosterByType(SupportedBoosterTypes type) {
        for (IBoostController<?> booster : REGISTERED_BOOSTERS) {
            if (booster.getType() == type) {
                return (T) booster;
            }
        }
        return null;
    }
}
