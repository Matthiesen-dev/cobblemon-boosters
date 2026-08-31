package dev.matthiesen.cobblemon_boosters.common.services.boosts;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.BoostParts;
import dev.matthiesen.cobblemon_boosters.common.utils.*;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class ShinyBoost extends AbstractSimpleBoost {
    public ShinyBoost(BoostParts parts) {
        super(parts.multiplier(), parts.duration(), parts.timeRemaining());
    }

    public ShinyBoost(float multiplier, int duration) {
        super(multiplier, duration);
    }

    public static Optional<ShinyBoost> fromString(String raw) {
        try {
            BoostParts parts = parseRawStringToParts(raw, 3);
            if (parts == null) return Optional.empty();
            return Optional.of(new ShinyBoost(parts));
        } catch (Exception e) {
            return Optional.empty();
        }
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
