package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.QueueGui;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class CheckQueuesGui extends BaseMenuGuiTemplate {

    public CheckQueuesGui(ServerPlayer player) {
        super(player);
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserialize(
                TextUtils.parse("&aAll Boost Queues&r")
        );
    }

    public Button getQueueButton(String boostType, Queue<? extends IBoost> queuedBoosts) {
        return GooeyButton.builder()
                .display(MenuUtils.getQueueItem(boostType))
                .onClick(() -> new QueueGui(
                        player,
                        boostType,
                        queuedBoosts
                ).open())
                .build();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(getQueueButton("Spawn Bucket", BoostManager.getSpawnBucketBoostManager().getQueue()));
        buttons.add(getQueueButton("Catch", BoostManager.getCatchBoostManager().getQueue()));
        buttons.add(getQueueButton("Experience", BoostManager.getExperienceBoostManager().getQueue()));
        buttons.add(getQueueButton("Shiny", BoostManager.getShinyBoostManager().getQueue()));
        return buttons;
    }
}
