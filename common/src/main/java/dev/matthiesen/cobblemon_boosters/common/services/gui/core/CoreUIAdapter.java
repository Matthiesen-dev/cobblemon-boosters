package dev.matthiesen.cobblemon_boosters.common.services.gui.core;

import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.CheckQueuesGui;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.MainMenuGui;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGUIAdapter;
import net.minecraft.server.level.ServerPlayer;

public final class CoreUIAdapter implements IGUIAdapter {
    public CoreUIAdapter() {
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
