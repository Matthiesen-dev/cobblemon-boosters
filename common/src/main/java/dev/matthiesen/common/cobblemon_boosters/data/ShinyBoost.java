package dev.matthiesen.common.cobblemon_boosters.data;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public class ShinyBoost {
    public float multiplier;
    public int duration;
    public long timeRemaining;
    public BossBar bossBar;

    public ShinyBoost(float multiplier, int duration) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = duration * 20L;
        this.bossBar = getBossBar();
    }

    public ShinyBoost(float multiplier, int duration, long timeRemaining) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = timeRemaining;
        this.bossBar = getBossBar();
    }

    public BossBar getBossBar() {
        return BossBar.bossBar(
                getBossBarText(),
                1F,
                CobblemonBoosters.INSTANCE.config.messages.shinyBarColor,
                CobblemonBoosters.INSTANCE.config.messages.shinyBarOverlay
        );
    }

    public Component getBossBarText() {
        return TextUtils.deserialize(
                TextUtils.parse(
                        CobblemonBoosters.INSTANCE.config.messages.shinyBarText,
                        this
                )
        );
    }
}
