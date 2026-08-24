package dev.matthiesen.cobblemon_boosters.common.boosts;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.utils.*;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class CatchBoost extends AbstractSimpleBoost {
    public CatchBoost(float multiplier, int duration, long timeRemaining) {
        super(multiplier, duration, timeRemaining);
    }

    public CatchBoost(float multiplier, int duration) {
        super(multiplier, duration);
    }

    public static Optional<CatchBoost> fromString(String raw) {
        try {
            String[] parts = raw.split(";");
            if (parts.length != 3) return Optional.empty();

            float multiplier = Float.parseFloat(parts[0]);
            int duration = Integer.parseInt(parts[1]);
            long timeRemaining = Long.parseLong(parts[2]);

            return Optional.of(new CatchBoost(multiplier, duration, timeRemaining));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public  BoostMessagesConfig getMessages() {
        return BoostersConfig.getCatchMessages();
    }

    @Override
    public ItemStack getGUIItem(net.minecraft.network.chat.Component[] lore) {
        return new BoostersItemBuilder(MenuUtils.CATCH_ITEM)
                .hideAdditional()
                .setCustomName(TextUtils.deserialize(TextUtils.parse("&a%multiplier%x Catch Boost&r", this)))
                .addLore(lore)
                .build();
    }
}
