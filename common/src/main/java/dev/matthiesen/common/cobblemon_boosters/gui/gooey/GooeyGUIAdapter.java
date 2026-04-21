package dev.matthiesen.common.cobblemon_boosters.gui.gooey;

import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.*;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import net.minecraft.server.level.ServerPlayer;

public class GooeyGUIAdapter implements IGUIAdapter {
    @Override
    public void openBoostersGUI(ServerPlayer player) {
        new MainMenuGui(player).open();
    }

    @Override
    public void openQueuesGUI(ServerPlayer player) {
        new CheckQueuesGui(player).open();
    }

    @Override
    public void openBucketBoosterGUI(ServerPlayer player) {
        new BucketGui(player).open();
    }

    @Override
    public void openCatchBoosterGUI(ServerPlayer player) {
        new CatchGui(player).open();
    }

    @Override
    public void openExperienceBoosterGUI(ServerPlayer player) {
        new ExperienceGui(player).open();
    }

    @Override
    public void openShinyBoosterGUI(ServerPlayer player) {
        new ShinyGui(player).open();
    }
}
