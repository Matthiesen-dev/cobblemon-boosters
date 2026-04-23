package dev.matthiesen.common.cobblemon_boosters.gui.gooey;

import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.*;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import net.minecraft.server.level.ServerPlayer;

public class GooeyGUIAdapter implements IGUIAdapter {
    @Override
    public void openMainMenuGUI(ServerPlayer player) {
        new MainMenuGui(player).open();
    }

    @Override
    public void openQueuesGUI(ServerPlayer player) {
        new CheckQueuesGui(player).open();
    }

    @Override
    public void openBucketBoosterGUI(ServerPlayer player) {
        MainMenuGui.openBucketGui(player);
    }

    @Override
    public void openCatchBoosterGUI(ServerPlayer player) {
        MainMenuGui.openCatchGUI(player);
    }

    @Override
    public void openExperienceBoosterGUI(ServerPlayer player) {
        MainMenuGui.openExperienceGUI(player);
    }

    @Override
    public void openShinyBoosterGUI(ServerPlayer player) {
        MainMenuGui.openShinyGUI(player);
    }
}
