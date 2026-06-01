package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.QueueGui;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CheckQueuesGui extends BaseMenuGuiTemplate {

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
        var boostManager = CobblemonBoosters.INSTANCE.boostManager;

        buttons.add(getQueueButton("Spawn Bucket", boostManager.getSpawnBucketBoostManager().getQueue()));
        buttons.add(getQueueButton("Catch", boostManager.getCatchBoostManager().getQueue()));
        buttons.add(getQueueButton("Experience", boostManager.getExperienceBoostManager().getQueue()));
        buttons.add(getQueueButton("Shiny", boostManager.getShinyBoostManager().getQueue()));

        return buttons;
    }
}
