package dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.cobblemon_boosters.common.services.BoostController;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.subscreens.QueueGui;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
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
        buttons.add(getQueueButton("Spawn Bucket", BoostController.getSpawnBucketBoostManager().getBoostQueue()));
        buttons.add(getQueueButton("Catch", BoostController.getCatchBoostManager().getBoostQueue()));
        buttons.add(getQueueButton("Experience", BoostController.getExperienceBoostManager().getBoostQueue()));
        buttons.add(getQueueButton("Shiny", BoostController.getShinyBoostManager().getBoostQueue()));
        return buttons;
    }
}
