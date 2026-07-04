package dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.templates;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.cobblemon_boosters.common.config.MessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.subscreens.QueueGui;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.common.matthiesen_lib_api.permission.Permission;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public final class BoostersGuiTemplate extends BaseMenuGuiTemplate {
    public final String guiTitle;
    public final String boostType;
    public final IBoost activeBoost;
    public final Queue<? extends IBoost> queuedBoosts;
    public final String noActiveBoost;
    public final String stopBoostMsg;
    public final String boostInfo;
    public final Permission startPermission;
    public final Permission stopPermission;
    public final Permission statusPermission;
    public final Permission queuePermission;
    public final Consumer<ButtonAction> startOnClick;

    public BoostersGuiTemplate(
            String guiTitle,
            String boostType,
            ServerPlayer player,
            BoostManager.IBoostManager<? extends IBoost> boostManager,
            MessagesConfig.BoostMessagesConfig messagesConfig,
            Permission startPermission,
            Permission stopPermission,
            Permission statusPermission,
            Permission queuePermission,
            Runnable startOnClick
    ) {
        super(player);
        this.guiTitle = guiTitle;
        this.boostType = boostType;
        this.activeBoost = boostManager.getActive();
        this.queuedBoosts = boostManager.getQueue();
        this.noActiveBoost = messagesConfig.noActiveBoosts;
        this.stopBoostMsg = messagesConfig.boostStopped;
        this.boostInfo = messagesConfig.boostInfo;
        this.startPermission = startPermission;
        this.stopPermission = stopPermission;
        this.statusPermission = statusPermission;
        this.queuePermission = queuePermission;
        this.startOnClick = (startOnClick != null) ? (action) -> startOnClick.run() : null;
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserialize(TextUtils.parse(guiTitle));
    }

    public Button getStopButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getStopButton(activeBoost != null))
                .onClick(() -> new CancelConfirmGuiBuilder(
                        player,
                        "&cConfirm to stop active boost!",
                        () -> {
                            if (activeBoost != null) {
                                activeBoost.setTimeRemaining(1);
                                player.sendSystemMessage(TextUtils.deserialize(TextUtils.parse(stopBoostMsg)));
                                close();
                            } else {
                                player.sendSystemMessage(TextUtils.deserialize(TextUtils.parse(noActiveBoost)));
                                close();
                            }
                        },
                        this::open
                ).open())
                .build();
    }

    public Button getStatusButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getStatusItem(activeBoost != null))
                .onClick(() -> {
                    if (activeBoost != null) {
                        player.sendSystemMessage(TextUtils.deserialize(TextUtils.parse(boostInfo, activeBoost)));
                        close();
                    } else {
                        player.sendSystemMessage(TextUtils.deserialize(TextUtils.parse(noActiveBoost)));
                        close();
                    }
                })
                .build();
    }

    public Button getQueueButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getQueueItemForSubscreen(boostType))
                .onClick(() -> new QueueGui(
                        player,
                        boostType,
                        queuedBoosts
                ).open())
                .build();
    }

    public Button getStartButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getCreateNewBoosterItem(boostType))
                .onClick(startOnClick)
                .build();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        if (PermissionRegistry.checkPermission(player, startPermission))
            buttons.add(getStartButton());

        if (PermissionRegistry.checkPermission(player, stopPermission))
            buttons.add(getStopButton());

        if (PermissionRegistry.checkPermission(player, statusPermission))
            buttons.add(getStatusButton());

        if (PermissionRegistry.checkPermission(player, queuePermission))
            buttons.add(getQueueButton());

        return buttons;
    }
}
