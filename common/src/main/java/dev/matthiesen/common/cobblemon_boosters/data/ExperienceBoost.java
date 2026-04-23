package dev.matthiesen.common.cobblemon_boosters.data;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.utils.ItemBuilder;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ExperienceBoost implements IBoost {
    public float multiplier;
    public int duration;
    public long timeRemaining;
    public BossBar bossBar;

    public ExperienceBoost(float multiplier, int duration) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = duration * 20L;
        this.bossBar = createBossBar();
    }

    public ExperienceBoost() {}

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
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.barColor,
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.barOverlay
        );
    }

    @Override
    public Component getBossBarText() {
        return TextUtils.deserialize(
                TextUtils.parse(
                        CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.barText,
                        this
                )
        );
    }

    @Override
    public String getBoostType() {
        return "Experience Boost";
    }

    @Override
    public ItemStack getGUIItem() {
        return new ItemBuilder(Items.EXPERIENCE_BOTTLE)
                .hideAdditional()
                .setCustomName(TextUtils.deserializeMC(TextUtils.parse("<green>%multiplier%x Experience Boost</green>", this)))
                .build();
    }

    @Override
    public ItemStack getGUIItem(net.minecraft.network.chat.Component[] lore) {
        return new ItemBuilder(Items.EXPERIENCE_BOTTLE)
                .hideAdditional()
                .setCustomName(TextUtils.deserializeMC(TextUtils.parse("<green>%multiplier%x Experience Boost</green>", this)))
                .addLore(lore)
                .build();
    }
}
