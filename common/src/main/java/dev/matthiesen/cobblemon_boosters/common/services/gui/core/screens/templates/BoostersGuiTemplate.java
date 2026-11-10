package dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.templates;

import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.subscreens.CancelConfirmGuiBuilder;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.subscreens.QueueGui;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ButtonAction;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
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
        var placeholders = TextUtils.getPlaceholders(activeBoost);
        return TextUtils.getTextParser().parse(definition.getMenuTitle(), placeholders);
    }

    public Button getStopButton() {
        return ServerButton.builder()
                .display(MenuUtils.getStopButton(activeBoost != null))
                .onClick(() -> new CancelConfirmGuiBuilder(
                        player,
                        BoostersConfig.TEXT_CONFIG.gui_stopActiveBoost_title.get(),
                        () -> {
                            var placeholders = TextUtils.getPlaceholders(activeBoost);
                            if (activeBoost != null) {
                                activeBoost.setTimeRemaining(1);
                                player.sendSystemMessage(TextUtils.getTextParser().parse(stopBoostMsg, placeholders));
                                close();
                            } else {
                                player.sendSystemMessage(TextUtils.getTextParser().parse(noActiveBoost, placeholders));
                                close();
                            }
                        },
                        this::open
                ).open())
                .build();
    }

    public Button getStatusButton() {
        return ServerButton.builder()
                .display(MenuUtils.getStatusItem(activeBoost != null))
                .onClick(() -> {
                    var placeholders = TextUtils.getPlaceholders(activeBoost);
                    if (activeBoost != null) {
                        player.sendSystemMessage(TextUtils.getTextParser().parse(boostInfo, placeholders));
                        close();
                    } else {
                        player.sendSystemMessage(TextUtils.getTextParser().parse(noActiveBoost, placeholders));
                        close();
                    }
                })
                .build();
    }

    public Button getQueueButton() {
        return ServerButton.builder()
                .display(MenuUtils.getQueueItemForSubscreen(definition.getDisplayName()))
                .onClick(() -> new QueueGui(
                        player,
                        definition.getDisplayName(),
                        definition.getBoostQueue()
                ).open())
                .build();
    }

    public Button getStartButton() {
        return ServerButton.builder()
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
