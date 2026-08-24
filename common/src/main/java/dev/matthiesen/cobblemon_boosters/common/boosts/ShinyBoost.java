package dev.matthiesen.cobblemon_boosters.common.boosts;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.utils.BoostersItemBuilder;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.utility.BossBar;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class ShinyBoost implements IBoost {
    public float multiplier;
    public int duration;
    public long timeRemaining;
    public transient BossBar bossBar;

    public ShinyBoost(float multiplier, int duration) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = duration * 20L;
    }

    public ShinyBoost(float multiplier, int duration, long timeRemaining) {
        this.multiplier = multiplier;
        this.duration = duration;
        this.timeRemaining = timeRemaining;
    }

    public static Optional<ShinyBoost> fromString(String raw) {
        try {
            String[] parts = raw.split(";");
            if (parts.length != 3) return Optional.empty();

            float multiplier = Float.parseFloat(parts[0]);
            int duration = Integer.parseInt(parts[1]);
            long timeRemaining = Long.parseLong(parts[2]);

            return Optional.of(new ShinyBoost(multiplier, duration, timeRemaining));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String serialize() {
        return multiplier + ";" + duration + ";" + timeRemaining;
    }

    @Override
    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
        this.timeRemaining = duration * 20L;
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

    @Override
    public BoostMessagesConfig getMessages() {
        return BoostersConfig.getShinyMessages();
    }

    @Override
    public ItemStack getGUIItem(net.minecraft.network.chat.Component[] lore) {
        return new BoostersItemBuilder(MenuUtils.SHINY_ITEM)
                .hideAdditional()
                .setCustomName(TextUtils.deserialize(TextUtils.parse("&a%multiplier%x Shiny Boost&r", this)))
                .addLore(lore)
                .build();
    }
}
