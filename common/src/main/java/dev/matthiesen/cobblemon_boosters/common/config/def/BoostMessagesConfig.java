package dev.matthiesen.cobblemon_boosters.common.config.def;

import net.minecraft.world.BossEvent;

public record BoostMessagesConfig(
        BossEvent.BossBarColor barColor,
        BossEvent.BossBarOverlay barOverlay,
        String barText,
        String noActiveBoosts,
        String boostStarted,
        String boostAddedToQueue,
        String boostStopped,
        String boostQueueCleared,
        String boostInfo,
        String noQueuedBoosts,
        String sidebarLine
) {}
