package dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.subscreens;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import dev.matthiesen.cobblemon_boosters.common.data.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.utils.BaseBoostBuilder;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
import dev.matthiesen.cobblemon_boosters.common.managers.MetricManager;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class BucketBoostBuilderGui implements IGui {
    public final ServerPlayer player;
    public final String boostType;
    public BoostBuilder boostBuilder;
    public String currentMode = null;
    public final Consumer<SpawnBucketBoost> setActiveBoost;

    public final List<String> allowedBuckets = Helpers.allowedBuckets;
    public final List<String> allowedUnits = Helpers.allowedUnits;
    public final Map<String, String> labelToColor = Map.of(
            "Multiplier", "&a",
            "Duration", "&b",
            "Unit", "&e",
            "Bucket", "&6"
    );

    public String getCurrentMode() {
        return currentMode;
    }

    public BucketBoostBuilderGui setCurrentMode(String mode) {
        this.currentMode = mode;
        return this;
    }

    public static class BoostBuilder extends BaseBoostBuilder {
        public String bucket;

        public BoostBuilder setBucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public SpawnBucketBoost build() {
            try {
                int totalSeconds = Helpers.parseTotalSeconds(duration, unit);
                return new SpawnBucketBoost(multiplier, totalSeconds).setBucket(bucket);
            } catch (Exception e) {
                MetricManager.ERROR_TRACKER.trackError(e);
                throw new RuntimeException("Failed to build boost", e);
            }
        }
    }

    public BucketBoostBuilderGui(
            ServerPlayer player,
            String boostType,
            Consumer<SpawnBucketBoost> setActiveBoost,
            BoostBuilder boostBuilder
    ) {
        this.player = player;
        this.boostType = boostType;
        this.setActiveBoost = setActiveBoost;
        this.boostBuilder = boostBuilder;
    }

    public BucketBoostBuilderGui(
            ServerPlayer player,
            String boostType,
            Consumer<SpawnBucketBoost> setActiveBoost
    ) {
        this(player, boostType, setActiveBoost, new BoostBuilder());
    }

    public Void openUpdatedPage(BucketBoostBuilderGui boostBuilderGui) {
        new BucketBoostBuilderGui(
                boostBuilderGui.player,
                boostBuilderGui.boostType,
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

    public Button buildModeButton(String label, Object value) {
        return Helpers.buildModeButton(
                label,
                value,
                labelToColor,
                this::getCurrentMode,
                this::setCurrentMode,
                this::openUpdatedPage,
                this
        );
    }

    public Button getMultiplierButton() {
        return buildModeButton(
                "Multiplier",
                boostBuilder.multiplier
        );
    }

    public Button getDurationButton() {
        return buildModeButton(
                "Duration",
                boostBuilder.duration
        );
    }

    public Button getUnitButton() {
        return buildModeButton(
                "Unit",
                boostBuilder.unit
        );
    }

    public Button getBucketButton() {
        return buildModeButton(
                "Bucket",
                boostBuilder.bucket
        );
    }

    public Button getDetailsButton() {
        List<Component> lore = new ArrayList<>();

        lore.add(Helpers.text("&7Boost Type: &f" + boostType));

        if (boostBuilder.bucket != null) {
            lore.add(Helpers.text("&7Bucket: &f" + boostBuilder.bucket));
        } else {
            lore.add(Helpers.text("&7Bucket: &cNot set"));
        }

        return Helpers.getDetailsButton(lore, boostBuilder);
    }

    public Button getAddButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getPlusItem())
                .onClick(() -> {
                    if (!Helpers.ensureModeSelected(getCurrentMode(), this::sendPlayerMessage)) {
                        return;
                    }
                    if (Helpers.applyBaseAddForMode(getCurrentMode(), boostBuilder, allowedUnits)) {
                        openUpdatedPage(this);
                        return;
                    }

                    if (getCurrentMode().equals("bucket")) {
                        boostBuilder.setBucket(Helpers.nextCyclicValue(boostBuilder.bucket, allowedBuckets, "common"));
                    } else {
                        sendPlayerMessage("&cPlease select a field to modify first by clicking on its button!");
                    }

                    openUpdatedPage(this);
                })
                .build();
    }

    public Button getSubtractButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getMinusItem())
                .onClick(() -> {
                    if (!Helpers.ensureModeSelected(getCurrentMode(), this::sendPlayerMessage)) {
                        return;
                    }
                    if (Helpers.applyBaseSubtractForMode(getCurrentMode(), boostBuilder, allowedUnits)) {
                        openUpdatedPage(this);
                        return;
                    }

                    if (getCurrentMode().equals("bucket")) {
                        boostBuilder.setBucket(Helpers.previousCyclicValue(boostBuilder.bucket, allowedBuckets, "common"));
                    } else {
                        sendPlayerMessage("&cPlease select a field to modify first by clicking on its button!");
                    }

                    openUpdatedPage(this);
                })
                .build();
    }

    public ChestTemplate.Builder addModifierButtons(ChestTemplate.Builder builder) {
        return Helpers.addModifierButtons(builder, getSubtractButton(), getAddButton());
    }

    public boolean isReadyToConfirm() {
        if (boostBuilder.bucket == null) return false;
        if (boostBuilder.multiplier == null) return false;
        if (boostBuilder.duration == null) return false;
        if (boostBuilder.unit == null) return false;
        return getCurrentMode() == null;
    }

    public Button getConfirmButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getConfirmItem())
                .onClick(() -> new CancelConfirmGuiBuilder(
                        player,
                        "&aConfirm to start/queue boost!",
                        () -> {
                            if (isReadyToConfirm()) {
                                SpawnBucketBoost boost = boostBuilder.build();
                                setActiveBoost.accept(boost);
                                close();
                            } else {
                                sendPlayerMessage("&cYou must fill out all fields before confirming!");
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

        builder = builder.set(1, 1, getBucketButton());
        builder = builder.set(1, 2, getMultiplierButton());
        builder = builder.set(1, 3, getDurationButton());
        builder = builder.set(1, 4, getUnitButton());
        builder = builder.set(1, 5, getDetailsButton());

        if (getCurrentMode() != null)
            builder = addModifierButtons(builder);

        if (isReadyToConfirm())
            builder = builder.set(1, 7, getConfirmButton());

        return builder.fill(Helpers.getFrame()).build();
    }

    public Page getPage() {
        GooeyPage page = GooeyPage.builder().template(getTemplate()).build();
        page.setTitle(getTitle());
        return page;
    }

    public void open() {
        UIManager.openUIForcefully(player, getPage());
    }

    public void close() {
        UIManager.closeUI(player);
    }

    public void sendPlayerMessage(String rawMessage) {
        Helpers.sendPlayerMessage(player, rawMessage);
    }
}
