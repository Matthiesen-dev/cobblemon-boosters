package dev.matthiesen.common.cobblemon_boosters.default_adapters;

import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import net.minecraft.server.level.ServerPlayer;

public class DummyGUIAdapter implements IGUIAdapter {
    @Override
    public void openBoostersGUI(ServerPlayer player) {}

    @Override
    public void openBucketBoosterGUI(ServerPlayer player) {}

    @Override
    public void openCatchBoosterGUI(ServerPlayer player) {}

    @Override
    public void openExperienceBoosterGUI(ServerPlayer player) {}

    @Override
    public void openShinyBoosterGUI(ServerPlayer player) {}
}
