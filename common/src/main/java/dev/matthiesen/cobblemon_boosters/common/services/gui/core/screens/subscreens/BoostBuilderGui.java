package dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.subscreens;

import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.utils.BaseBoostBuilder;
import dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.matthiesen_core.common.api.server_menu.ServerUIManager;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.Page;
import dev.matthiesen.matthiesen_core.common.api.server_menu.page.ServerPage;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.ChestTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class BoostBuilderGui implements IGui {
    public final ServerPlayer player;
    public final String boostType;
    public final Class<? extends IBoost> boostClass;
    public BaseBoostBuilder boostBuilder;
    public String currentMode = null;
    public final Consumer<IBoost> setActiveBoost;

    public final List<String> allowedUnits = Helpers.allowedUnits;

    public String getCurrentMode() {
        return currentMode;
    }

    public BoostBuilderGui setCurrentMode(String mode) {
        this.currentMode = mode;
        return this;
    }

    public BoostBuilderGui(
            ServerPlayer player,
            String boostType,
            Class<? extends IBoost> boostClass,
            Consumer<IBoost> setActiveBoost,
            BaseBoostBuilder boostBuilder
    ) {
        this.player = player;
        this.boostType = boostType;
        this.boostClass = boostClass;
        this.setActiveBoost = setActiveBoost;
        this.boostBuilder = boostBuilder;
    }

    public BoostBuilderGui(
            ServerPlayer player,
            String boostType,
            Class<? extends IBoost> boostClass,
            Consumer<IBoost> setActiveBoost
    ) {
        this(player, boostType, boostClass, setActiveBoost, new BaseBoostBuilder());
    }

    public Void openUpdatedPage(BoostBuilderGui boostBuilderGui) {
        new BoostBuilderGui(
                boostBuilderGui.player,
                boostBuilderGui.boostType,
                boostBuilderGui.boostClass,
                boostBuilderGui.setActiveBoost,
                boostBuilderGui.boostBuilder
        )
                .setCurrentMode(getCurrentMode())
                .open();
        return null;
    }

    public Component getTitle() {
        return Helpers.getBoostBuilderTitle();
    }

    public Button buildModeButton(
            String label,
            Object value
    ) {
        return Helpers.buildModeButton(
                label,
                value,
                getLabelToColor(),
                this::getCurrentMode,
                this::setCurrentMode,
                this::openUpdatedPage,
                this
        );
    }

    public Button getMultiplierButton() {
        return buildModeButton(
                BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_label_multiplier.get(),
                boostBuilder.multiplier
        );
    }

    public Button getDurationButton() {
        return buildModeButton(
                BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_label_duration.get(),
                boostBuilder.duration
        );
    }

    public Button getUnitButton() {
        return buildModeButton(
                BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_label_unit.get(),
                boostBuilder.unit
        );
    }

    public Button getDetailsButton() {
        List<Component> lore = new ArrayList<>();

        String boostTypeText = String.format(BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_boostType.get(), boostType);
        lore.add(Helpers.text(boostTypeText));

        return Helpers.getDetailsButton(lore, boostBuilder);
    }

    public Button getAddButton() {
        return ServerButton.builder()
                .display(MenuUtils.getPlusItem())
                .onClick(() -> {
                    if (!Helpers.ensureModeSelected(getCurrentMode(), this::sendPlayerMessage)) {
                        return;
                    }
                    if (!Helpers.applyBaseAddForMode(getCurrentMode(), boostBuilder, allowedUnits)) {
                        sendPlayerMessage(BoostersConfig.TEXT_CONFIG.messages_error_selectModeFirst.get());
                    }
                    openUpdatedPage(this);
                })
                .build();
    }

    public Button getSubtractButton() {
        return ServerButton.builder()
                .display(MenuUtils.getMinusItem())
                .onClick(() -> {
                    if (!Helpers.ensureModeSelected(getCurrentMode(), this::sendPlayerMessage)) {
                        return;
                    }
                    if (!Helpers.applyBaseSubtractForMode(getCurrentMode(), boostBuilder, allowedUnits)) {
                        sendPlayerMessage(BoostersConfig.TEXT_CONFIG.messages_error_selectModeFirst.get());
                    }
                    openUpdatedPage(this);
                })
                .build();
    }

    public ChestTemplate.Builder addModifierButtons(ChestTemplate.Builder builder) {
        return Helpers.addModifierButtons(builder, getSubtractButton(), getAddButton());
    }

    public boolean isReadyToConfirm() {
        if (boostBuilder.multiplier == null) return false;
        if (boostBuilder.duration == null) return false;
        if (boostBuilder.unit == null) return false;
        return getCurrentMode() == null;
    }

    public Button getConfirmButton() {
        return ServerButton.builder()
                .display(MenuUtils.getConfirmItem())
                .onClick(() -> new CancelConfirmGuiBuilder(
                        player,
                        BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_confirm_title.get(),
                        () -> {
                            if (isReadyToConfirm()) {
                                IBoost boost = boostBuilder.build(boostClass);
                                setActiveBoost.accept(boost);
                                close();
                            } else {
                                sendPlayerMessage(BoostersConfig.TEXT_CONFIG.messages_error_missingFields.get());
                                openUpdatedPage(this);
                            }
                        },
                        () -> openUpdatedPage(this),
                        getDetailsButton()
                ).open())
                .build();
    }

    public ChestTemplate getTemplate() {
        ChestTemplate.Builder builder = ChestTemplate.builder(3);

        builder = builder.set(1, 1, getMultiplierButton());
        builder = builder.set(1, 2, getDurationButton());
        builder = builder.set(1, 3, getUnitButton());
        builder = builder.set(1, 4, getDetailsButton());

        if (getCurrentMode() != null)
            builder = addModifierButtons(builder);

        if (isReadyToConfirm())
            builder = builder.set(1, 7, getConfirmButton());

        return builder.fill(Helpers.getFrame()).build();
    }

    public Page getPage() {
        ServerPage page = ServerPage.builder().template(getTemplate()).build();
        page.setTitle(getTitle());
        return page;
    }

    public void open() {
        ServerUIManager.openUIForcefully(player, getPage());
    }

    public void close() {
        ServerUIManager.closeUI(player);
    }

    public void sendPlayerMessage(String rawMessage) {
        Helpers.sendPlayerMessage(player, rawMessage);
    }
}
