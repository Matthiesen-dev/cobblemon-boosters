package dev.matthiesen.common.cobblemon_boosters.data;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public class CatchBoost implements IBoost {
    public float multiplier;
    public int duration;
    public long timeRemaining;
    public BossBar bossBar;

    public CatchBoost(float multiplier, int duration) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = duration * 20L;
        this.bossBar = createBossBar();
    }

    @Override
    public float getMultiplier() {
        return this.multiplier;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public long getTimeRemaining() {
        return this.timeRemaining;
    }

    @Override
    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    @Override
    public BossBar getBossBar() {
        if (this.bossBar == null) {
            this.bossBar = createBossBar();
        }
        return this.bossBar;
    }

    private BossBar createBossBar() {
        return BossBar.bossBar(
                getBossBarText(),
                1F,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostBarColor,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostBarOverlay
        );
    }

    @Override
    public Component getBossBarText() {
        return TextUtils.deserialize(
                TextUtils.parse(
                        CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostBarText,
                        this
                )
        );
    }
}
