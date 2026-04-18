package dev.matthiesen.common.cobblemon_boosters.utils;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;

public class TickManager {
    public static void tickBoosts() {
        if (CobblemonBoosters.INSTANCE.globalBoost != null) {
            CobblemonBoosters.INSTANCE.globalBoost.timeRemaining--;
            if (CobblemonBoosters.INSTANCE.globalBoost.timeRemaining <= 0) {
                CobblemonBoosters.INSTANCE.getAdventure().all().hideBossBar(CobblemonBoosters.INSTANCE.globalBoost.bossBar);
                CobblemonBoosters.INSTANCE.globalBoost = CobblemonBoosters.INSTANCE.queuedShinyBoosts.poll();
                if (CobblemonBoosters.INSTANCE.globalBoost != null) {
                    CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.globalBoost.bossBar);
                }
                CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
            }
        }
    }

    public static void updateBossBars() {
        if (CobblemonBoosters.INSTANCE.globalBoost != null) {
            float progressRate = 1.0F / (CobblemonBoosters.INSTANCE.globalBoost.duration * 20L);
            float total = progressRate * CobblemonBoosters.INSTANCE.globalBoost.timeRemaining;

            if (total < 0F)
                total = 0F;
            if (total > 1F)
                total = 1F;

            CobblemonBoosters.INSTANCE.globalBoost.bossBar.progress(total);
            CobblemonBoosters.INSTANCE.globalBoost.bossBar.name(CobblemonBoosters.INSTANCE.globalBoost.getBossBarText());
        }
    }
}
