package dev.matthiesen.cobblemon_boosters.common.interfaces;

import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import net.minecraft.ChatFormatting;

import java.util.Map;

public interface IGui {
    void open();
    void close();
    void sendPlayerMessage(String rawMessage);

    default Map<String, ChatFormatting> getLabelToColor() {
        return Map.of(
                BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_label_multiplier.get(), ChatFormatting.GREEN,
                BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_label_duration.get(), ChatFormatting.AQUA,
                BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_label_unit.get(), ChatFormatting.YELLOW,
                BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_label_bucket.get(), ChatFormatting.GOLD
        );
    }
}
