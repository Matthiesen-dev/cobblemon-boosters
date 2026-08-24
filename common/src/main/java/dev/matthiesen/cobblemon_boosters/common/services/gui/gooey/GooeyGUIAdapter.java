package dev.matthiesen.cobblemon_boosters.common.services.gui.gooey;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.CheckQueuesGui;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.MainMenuGui;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGUIAdapter;
import net.minecraft.server.level.ServerPlayer;

public final class GooeyGUIAdapter implements IGUIAdapter {
    public GooeyGUIAdapter() {
        CobblemonBoostersCommon.INSTANCE.createInfoLog("GooeyLibs detected, using GooeyGUIAdapter for GUI integration");
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
    public void openBoosterGUI(ServerPlayer player, String boosterId) {
        MainMenuGui.openBoosterGui(player, boosterId);
    }
}
