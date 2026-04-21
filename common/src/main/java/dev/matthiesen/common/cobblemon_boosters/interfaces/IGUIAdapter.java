package dev.matthiesen.common.cobblemon_boosters.interfaces;


import net.minecraft.server.level.ServerPlayer;

public interface IGUIAdapter {
    // Main GUIs
    void openBoostersGUI(ServerPlayer player);
    void openQueuesGUI(ServerPlayer player);

    // Booster GUIs
    void openBucketBoosterGUI(ServerPlayer player);
    void openCatchBoosterGUI(ServerPlayer player);
    void openExperienceBoosterGUI(ServerPlayer player);
    void openShinyBoosterGUI(ServerPlayer player);
}
