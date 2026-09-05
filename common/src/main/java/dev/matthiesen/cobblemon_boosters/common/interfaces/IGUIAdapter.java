package dev.matthiesen.cobblemon_boosters.common.interfaces;


import net.minecraft.server.level.ServerPlayer;

public interface IGUIAdapter {
    void openMainMenuGUI(ServerPlayer player);
    void openQueuesGUI(ServerPlayer player);
    void openBoosterGUI(ServerPlayer player, String boosterId);
}
