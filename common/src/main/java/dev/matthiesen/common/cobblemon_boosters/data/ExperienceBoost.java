package dev.matthiesen.common.cobblemon_boosters.data;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.utils.BossBar;
import dev.matthiesen.common.cobblemon_boosters.utils.BoostersItemBuilder;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class ExperienceBoost implements IBoost {
    public float multiplier;
    public int duration;
    public long timeRemaining;
    public transient BossBar bossBar;

    public ExperienceBoost(float multiplier, int duration) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = duration * 20L;
        this.bossBar = createBossBar();
    }

    @Override
    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void setDuration(int duration) {
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
    public BossBar.Builder getBossBar() {
        if (this.bossBar == null) {
            this.bossBar = createBossBar();
        }
        return this.bossBar.getBuilder();
    }

    private BossBar createBossBar() {
        return new BossBar(
                getBossBarText(),
                1F,
                CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages.barColor,
                CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages.barOverlay
        );
    }

    @Override
    public Component getBossBarText() {
        return TextUtils.deserialize(
                TextUtils.parse(
                        CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages.barText,
                        this
                )
        );
    }

    @Override
    public ItemStack getGUIItem(net.minecraft.network.chat.Component[] lore) {
        return new BoostersItemBuilder(MenuUtils.EXPERIENCE_ITEM)
                .hideAdditional()
                .setCustomName(TextUtils.deserialize(TextUtils.parse("&a%multiplier%x Experience Boost&r", this)))
                .addLore(lore)
                .build();
    }
}
