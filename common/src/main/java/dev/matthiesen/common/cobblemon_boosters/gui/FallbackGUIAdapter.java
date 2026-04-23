package dev.matthiesen.common.cobblemon_boosters.gui;

import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FallbackGUIAdapter implements IGUIAdapter {

    public Component getDefaultComponent() {
        String defaultMessage = TextUtils.parse("%prefix% <gray>GUI not available, please use command arguments instead.</gray>");
        return TextUtils.deserializeMC(defaultMessage);
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
