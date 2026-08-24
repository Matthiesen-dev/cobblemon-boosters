package dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.templates;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.subscreens.QueueGui;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class BoostersGuiTemplate extends BaseMenuGuiTemplate {
    public final BoosterGuiDefinition<?> definition;
    public final IBoost activeBoost;
    public final String noActiveBoost;
    public final String stopBoostMsg;
    public final String boostInfo;
    public final java.util.function.Consumer<ButtonAction> startOnClick;

    public BoostersGuiTemplate(
            ServerPlayer player,
            BoosterGuiDefinition<?> definition,
            Runnable startOnClick
    ) {
        super(player);
        this.definition = definition;
        this.activeBoost = definition.getActiveBoost();
        this.noActiveBoost = definition.getMessages().noActiveBoosts();
        this.stopBoostMsg = definition.getMessages().boostStopped();
        this.boostInfo = definition.getMessages().boostInfo();
        this.startOnClick = (startOnClick != null) ? (action) -> startOnClick.run() : null;
    }

    @Override
    public Component getTitle() {
        return TextUtils.deserialize(TextUtils.parse(definition.getMenuTitle()));
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
                .display(MenuUtils.getQueueItemForSubscreen(definition.getDisplayName()))
                .onClick(() -> new QueueGui(
                        player,
                        definition.getDisplayName(),
                        definition.getBoostQueue()
                ).open())
                .build();
    }

    public Button getStartButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getCreateNewBoosterItem(definition.getDisplayName()))
                .onClick(startOnClick)
                .build();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        if (PermissionRegistry.checkPermission(player, definition.getStartPermission()))
            buttons.add(getStartButton());

        if (PermissionRegistry.checkPermission(player, definition.getStopPermission()))
            buttons.add(getStopButton());

        if (PermissionRegistry.checkPermission(player, definition.getStatusPermission()))
            buttons.add(getStatusButton());

        if (PermissionRegistry.checkPermission(player, definition.getQueuePermission()))
            buttons.add(getQueueButton());

        return buttons;
    }
}
