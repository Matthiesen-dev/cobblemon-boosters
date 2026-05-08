package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.QueueGui;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermission;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class BoostersGuiTemplate extends BaseMenuGuiTemplate {
    public final String guiTitle;
    public final String boostType;
    public final IBoost activeBoost;
    public final Queue<? extends IBoost> queuedBoosts;
    public final String noActiveBoost;
    public final String stopBoostMsg;
    public final String boostInfo;
    public final ModPermission startPermission;
    public final ModPermission stopPermission;
    public final ModPermission statusPermission;
    public final ModPermission queuePermission;
    public final Consumer<ButtonAction> startOnClick;

    public BoostersGuiTemplate(
            String guiTitle,
            String boostType,
            ServerPlayer player,
            IBoost activeBoost,
            Queue<? extends IBoost> queuedBoosts,
            String noActiveBoost,
            String stopBoostMsg,
            String boostInfo,
            ModPermission startPermission,
            ModPermission stopPermission,
            ModPermission statusPermission,
            ModPermission queuePermission,
            Runnable startOnClick
    ) {
        super(player);
        this.guiTitle = guiTitle;
        this.boostType = boostType;
        this.activeBoost = activeBoost;
        this.queuedBoosts = queuedBoosts;
        this.noActiveBoost = noActiveBoost;
        this.stopBoostMsg = stopBoostMsg;
        this.boostInfo = boostInfo;
        this.startPermission = startPermission;
        this.stopPermission = stopPermission;
        this.statusPermission = statusPermission;
        this.queuePermission = queuePermission;
        this.startOnClick = (startOnClick != null) ? (action) -> startOnClick.run() : null;
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserializeMC(TextUtils.parse(guiTitle));
    }

    public Button getStopButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getStopButton(activeBoost != null))
                .onClick(() -> new CancelConfirmGuiBuilder(
                        player,
                        "<red>Confirm to stop active boost!",
                        () -> {
                            if (activeBoost != null) {
                                activeBoost.setTimeRemaining(1);
                                player.sendSystemMessage(TextUtils.deserializeMC(TextUtils.parse(stopBoostMsg)));
                                close();
                            } else {
                                player.sendSystemMessage(TextUtils.deserializeMC(TextUtils.parse(noActiveBoost)));
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
                        player.sendSystemMessage(TextUtils.deserializeMC(TextUtils.parse(boostInfo, activeBoost)));
                        close();
                    } else {
                        player.sendSystemMessage(TextUtils.deserializeMC(TextUtils.parse(noActiveBoost)));
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

        if (ModPermissions.checkPermission(player, startPermission))
            buttons.add(getStartButton());

        if (ModPermissions.checkPermission(player, stopPermission))
            buttons.add(getStopButton());

        if (ModPermissions.checkPermission(player, statusPermission))
            buttons.add(getStatusButton());

        if (ModPermissions.checkPermission(player, queuePermission))
            buttons.add(getQueueButton());

        return buttons;
    }
}
