package dev.matthiesen.common.cobblemon_boosters.gui;

import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FallbackGUIAdapter implements IGUIAdapter {
    private static final String defaultMessage = TextUtils.parse("%prefix% <gray>GUI not available, please use command arguments instead.</gray>");
    private static final Component defaultComponent = TextUtils.deserializeMC(defaultMessage);

    @Override
    public void openBoostersGUI(ServerPlayer player) {
        player.sendSystemMessage(defaultComponent);
    }

    @Override
    public void openQueuesGUI(ServerPlayer player) {
        player.sendSystemMessage(defaultComponent);
    }

    @Override
    public void openBucketBoosterGUI(ServerPlayer player) {
        player.sendSystemMessage(defaultComponent);
    }

    @Override
    public void openCatchBoosterGUI(ServerPlayer player) {
        player.sendSystemMessage(defaultComponent);
    }

    @Override
    public void openExperienceBoosterGUI(ServerPlayer player) {
        player.sendSystemMessage(defaultComponent);
    }

    @Override
    public void openShinyBoosterGUI(ServerPlayer player) {
        player.sendSystemMessage(defaultComponent);
    }
}
