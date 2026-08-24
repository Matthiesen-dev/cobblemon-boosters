package dev.matthiesen.cobblemon_boosters.common.boosts;

import dev.matthiesen.cobblemon_boosters.common.config.def.BoostMessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.utils.*;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class ExperienceBoost extends AbstractSimpleBoost {
    public ExperienceBoost(BoostParts parts) {
        super(parts.multiplier(), parts.duration(), parts.timeRemaining());
    }

    public ExperienceBoost(float multiplier, int duration) {
        super(multiplier, duration);
    }

    public static Optional<ExperienceBoost> fromString(String raw) {
        try {
            BoostParts parts = parseRawStringToParts(raw, 3);
            if (parts == null) return Optional.empty();
            return Optional.of(new ExperienceBoost(parts));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public BoostMessagesConfig getMessages() {
        return BoostersConfig.getExperienceMessages();
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
