package dev.matthiesen.cobblemon_boosters.common.gui.gooey;

import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.CheckQueuesGui;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.MainMenuGui;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGUIAdapter;
import net.minecraft.server.level.ServerPlayer;

public final class GooeyGUIAdapter implements IGUIAdapter {
    public GooeyGUIAdapter() {
        Constants.createInfoLog("GooeyLibs detected, using GooeyGUIAdapter for GUI integration");
    }

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
