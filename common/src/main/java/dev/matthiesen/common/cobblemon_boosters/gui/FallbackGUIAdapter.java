package dev.matthiesen.common.cobblemon_boosters.gui;

import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class FallbackGUIAdapter implements IGUIAdapter {

    public FallbackGUIAdapter() {
        Constants.createInfoLog("No compatible GUI library detected, using fallback GUI adapter which sends messages to players instead of opening GUIs");
    }

    public Component getDefaultComponent() {
        String defaultMessage = TextUtils.parse("%prefix% &7GUI not available, please use command arguments instead.&r");
        return TextUtils.deserialize(defaultMessage);
    }

    @Override
    public void openMainMenuGUI(ServerPlayer player) {
        player.sendSystemMessage(getDefaultComponent());
    }

    @Override
    public void openQueuesGUI(ServerPlayer player) {
        player.sendSystemMessage(getDefaultComponent());
    }

    @Override
    public void openBucketBoosterGUI(ServerPlayer player) {
        player.sendSystemMessage(getDefaultComponent());
    }

    @Override
    public void openCatchBoosterGUI(ServerPlayer player) {
        player.sendSystemMessage(getDefaultComponent());
    }

    @Override
    public void openExperienceBoosterGUI(ServerPlayer player) {
        player.sendSystemMessage(getDefaultComponent());
    }

    @Override
    public void openShinyBoosterGUI(ServerPlayer player) {
        player.sendSystemMessage(getDefaultComponent());
    }
}
