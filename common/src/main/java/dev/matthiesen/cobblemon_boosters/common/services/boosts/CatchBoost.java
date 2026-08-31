package dev.matthiesen.cobblemon_boosters.common.services.boosts;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.BoostParts;
import dev.matthiesen.cobblemon_boosters.common.utils.*;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class CatchBoost extends AbstractSimpleBoost {
    public CatchBoost(BoostParts parts) {
        super(parts.multiplier(), parts.duration(), parts.timeRemaining());
    }

    public CatchBoost(float multiplier, int duration) {
        super(multiplier, duration);
    }

    public static Optional<CatchBoost> fromString(String raw) {
        try {
            BoostParts parts = parseRawStringToParts(raw, 3);
            if (parts == null) return Optional.empty();
            return Optional.of(new CatchBoost(parts));
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
