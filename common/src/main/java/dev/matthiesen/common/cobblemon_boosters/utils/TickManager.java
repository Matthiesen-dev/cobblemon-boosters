package dev.matthiesen.common.cobblemon_boosters.utils;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;

public class TickManager {
    public static void tickBoosts() {
        if (CobblemonBoosters.INSTANCE.activeShinyBoost != null) {
            decrementBoost(CobblemonBoosters.INSTANCE.activeShinyBoost);
            if (CobblemonBoosters.INSTANCE.activeShinyBoost.timeRemaining <= 0) {
                CobblemonBoosters.INSTANCE.getAdventure().all().hideBossBar(CobblemonBoosters.INSTANCE.activeShinyBoost.getBossBar());
                CobblemonBoosters.INSTANCE.activeShinyBoost = CobblemonBoosters.INSTANCE.queuedShinyBoosts.poll();
                if (CobblemonBoosters.INSTANCE.activeShinyBoost != null) {
                    CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeShinyBoost.getBossBar());
                }
                CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
            }
        }
        if (CobblemonBoosters.INSTANCE.activeCatchBoost != null) {
            decrementBoost(CobblemonBoosters.INSTANCE.activeCatchBoost);
            if (CobblemonBoosters.INSTANCE.activeCatchBoost.timeRemaining <= 0) {
                CobblemonBoosters.INSTANCE.getAdventure().all().hideBossBar(CobblemonBoosters.INSTANCE.activeCatchBoost.getBossBar());
                CobblemonBoosters.INSTANCE.activeCatchBoost = CobblemonBoosters.INSTANCE.queuedCatchBoosts.poll();
                if (CobblemonBoosters.INSTANCE.activeCatchBoost != null) {
                    CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeCatchBoost.getBossBar());
                }
                CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
            }
        }
    }

    public static void updateBossBars() {
        if (CobblemonBoosters.INSTANCE.activeShinyBoost != null) {
            updateBossBar(CobblemonBoosters.INSTANCE.activeShinyBoost);
        }
        if (CobblemonBoosters.INSTANCE.activeCatchBoost != null) {
            updateBossBar(CobblemonBoosters.INSTANCE.activeCatchBoost);
        }
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

        boost.getBossBar().progress(total);
        boost.getBossBar().name(boost.getBossBarText());
    }
}
