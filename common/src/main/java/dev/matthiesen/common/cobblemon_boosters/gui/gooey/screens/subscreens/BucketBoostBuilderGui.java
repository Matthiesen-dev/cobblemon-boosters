package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BucketBoostBuilderGui {
    public final ServerPlayer player;
    public final String boostType;
    public BoostBuilder boostBuilder;
    public String currentMode = null;
    public final Consumer<SpawnBucketBoost> setActiveBoost;

    public final List<String> allowedBuckets = List.of("common", "uncommon", "rare", "ultra-rare");
    public final List<String> allowedUnits = List.of("seconds", "minutes", "hours", "days");
    public final Map<String, String> labelToColor = Map.of(
            "Multiplier", "<green>",
            "Duration", "<aqua>",
            "Unit", "<yellow>",
            "Bucket", "<gold>"
    );

    public String getCurrentMode() {
        return currentMode;
    }

    public BucketBoostBuilderGui setCurrentMode(String mode) {
        this.currentMode = mode;
        return this;
    }

    public static class BoostBuilder {
        public String bucket;
        public Float multiplier;
        public Integer duration;
        public String unit;

        public BoostBuilder setBucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public BoostBuilder setMultiplier(float multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        public BoostBuilder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public BoostBuilder setUnit(String unit) {
            List<String> allowedUnits = List.of("seconds", "minutes", "hours", "days");
            if (allowedUnits.contains(unit.toLowerCase())) {
                this.unit = unit.toLowerCase();
            } else {
                this.unit = unit;
            }
            return this;
        }

        public static int parseTotalSeconds(int duration, String unit) {
            int totalSeconds;
            switch (unit) {
                case "minutes" -> totalSeconds = duration * 60;
                case "hours" -> totalSeconds = duration * 3600;
                case "days" -> totalSeconds = duration * 86400;
                default -> totalSeconds = duration;
            }
            return totalSeconds;
        }

        public SpawnBucketBoost build() {
            try {
                SpawnBucketBoost boost = new SpawnBucketBoost();
                boost.setMultiplier(multiplier);
                int totalSeconds = parseTotalSeconds(duration, unit);
                boost.setDuration(totalSeconds);
                boost.setBucket(bucket);
                return boost;
            } catch (Exception e) {
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

    public void openUpdatedPage(BucketBoostBuilderGui boostBuilderGui) {
        new BucketBoostBuilderGui(
                boostBuilderGui.player,
                boostBuilderGui.boostType,
                boostBuilderGui.setActiveBoost,
                boostBuilderGui.boostBuilder
        )
                .setCurrentMode(getCurrentMode())
                .open();
    }

    public Component getTitle() {
        return TextUtils.deserializeMC(
                TextUtils.parse("Cobblemon Boosters - Boost Builder")
        );
    }

    public Button getFrame() {
        return GooeyButton.builder()
                .display(MenuUtils.getFrameItem())
                .build();
    }

    public Button buildModeButton(
            String label,
            Object value
    ) {
        List<Component> lore = new ArrayList<>();

        if (value != null) {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Current: <white>" + value)));
        } else {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Current: <red>Not set")));
        }

        Component[] loreArray = new Component[lore.size()];
        for  (int i = 0; i < lore.size(); i++) {
            loreArray[i] = lore.get(i);
        }

        boolean isActive = getCurrentMode() != null && getCurrentMode().equals(label.toLowerCase());

        return GooeyButton.builder()
                .display(MenuUtils.getQueueEntryBuilder()
                        .setCustomName(TextUtils.deserializeMC(TextUtils.parse(labelToColor.get(label) + label)))
                        .addLore(loreArray)
                        .setEnchanted(isActive)
                        .build()
                )
                .onClick(() -> {
                    if (isActive) {
                        setCurrentMode(null);
                    } else {
                        setCurrentMode(label.toLowerCase());
                    }
                    openUpdatedPage(this);
                })
                .build();
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

        lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Boost Type: <white>" + boostType)));

        if (boostBuilder.bucket != null) {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Bucket: <white>" + boostBuilder.bucket)));
        } else {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Bucket: <red>Not set")));
        }

        if (boostBuilder.multiplier != null) {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Multiplier: <white>" + boostBuilder.multiplier)));
        } else {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Multiplier: <red>Not set")));
        }

        if (boostBuilder.duration != null) {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Duration: <white>" + boostBuilder.duration)));
        } else {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Duration: <red>Not set")));
        }

        if (boostBuilder.unit != null) {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Unit: <white>" + boostBuilder.unit)));
        } else {
            lore.add(TextUtils.deserializeMC(TextUtils.parse("<gray>Unit: <red>Not set")));
        }

        Component[] loreArray = new Component[lore.size()];
        for (int i = 0; i < lore.size(); i++) {
            loreArray[i] = lore.get(i);
        }

        return GooeyButton.builder()
                .display(MenuUtils.getQueueEntryBuilder()
                        .setCustomName(TextUtils.deserializeMC(TextUtils.parse("<gold>Details")))
                        .addLore(loreArray)
                        .build()
                )
                .build();
    }

    public Button getAddButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getPlusItem())
                .onClick(() -> {
                    if (getCurrentMode() == null) {
                        sendPlayerMessage("<red>Please select a field to modify first by clicking on its button!");
                        return;
                    }
                    switch (getCurrentMode()) {
                        case "multiplier" -> {
                            if (boostBuilder.multiplier == null) {
                                boostBuilder = boostBuilder.setMultiplier(1.0f);
                            } else {
                                boostBuilder = boostBuilder.setMultiplier(boostBuilder.multiplier + 1.0f);
                            }
                        }
                        case "duration" -> {
                            if (boostBuilder.duration == null) {
                                boostBuilder = boostBuilder.setDuration(1);
                            } else {
                                boostBuilder = boostBuilder.setDuration(boostBuilder.duration + 1);
                            }
                        }
                        case "unit" -> {
                            if (boostBuilder.unit == null) {
                                boostBuilder = boostBuilder.setUnit("seconds");
                            } else {
                                int currentIndex = allowedUnits.indexOf(boostBuilder.unit);
                                int nextIndex = (currentIndex + 1) % allowedUnits.size();
                                boostBuilder = boostBuilder.setUnit(allowedUnits.get(nextIndex));
                            }
                        }
                        case "bucket" -> {
                            if (boostBuilder.bucket == null) {
                                boostBuilder = boostBuilder.setBucket("common");
                            } else {
                                int currentIndex = allowedBuckets.indexOf(boostBuilder.bucket);
                                int nextIndex = (currentIndex + 1) % allowedBuckets.size();
                                boostBuilder = boostBuilder.setBucket(allowedBuckets.get(nextIndex));
                            }
                        }
                        default -> sendPlayerMessage("<red>Please select a field to modify first by clicking on its button!");
                    }
                    openUpdatedPage(this);
                })
                .build();
    }

    public Button getSubtractButton() {
        return GooeyButton.builder()
                .display(MenuUtils.getMinusItem())
                .onClick(() -> {
                    if (getCurrentMode() == null) {
                        sendPlayerMessage("<red>Please select a field to modify first by clicking on its button!");
                        return;
                    }
                    switch (getCurrentMode()) {
                        case "multiplier" -> {
                            if (boostBuilder.multiplier != null) {
                                boostBuilder = boostBuilder.setMultiplier(boostBuilder.multiplier - 1.0f);
                                if (boostBuilder.multiplier < 0) boostBuilder = boostBuilder.setMultiplier(1.0f);
                            } else {
                                boostBuilder = boostBuilder.setMultiplier(1.0f);
                            }
                        }
                        case "duration" -> {
                            if (boostBuilder.duration != null) {
                                boostBuilder = boostBuilder.setDuration(boostBuilder.duration - 1);
                                if (boostBuilder.duration < 0) boostBuilder = boostBuilder.setDuration(1);
                            } else {
                                boostBuilder = boostBuilder.setDuration(1);
                            }
                        }
                        case "unit" -> {
                            if (boostBuilder.unit != null) {
                                int currentIndex = allowedUnits.indexOf(boostBuilder.unit);
                                int nextIndex = (currentIndex - 1 + allowedUnits.size()) % allowedUnits.size();
                                boostBuilder = boostBuilder.setUnit(allowedUnits.get(nextIndex));
                            } else {
                                boostBuilder = boostBuilder.setUnit("seconds");
                            }
                        }
                        case "bucket" -> {
                            if (boostBuilder.bucket != null) {
                                int currentIndex = allowedBuckets.indexOf(boostBuilder.bucket);
                                int nextIndex = (currentIndex - 1 + allowedBuckets.size()) % allowedBuckets.size();
                                boostBuilder = boostBuilder.setBucket(allowedBuckets.get(nextIndex));
                            } else {
                                boostBuilder = boostBuilder.setBucket("common");
                            }
                        }
                        default -> sendPlayerMessage("<red>Please select a field to modify first by clicking on its button!");
                    }
                    openUpdatedPage(this);
                })
                .build();
    }

    public ChestTemplate.Builder addModifierButtons(ChestTemplate.Builder builder) {
        builder = builder.set(1, 5, getAddButton());
        builder = builder.set(1, 6, getSubtractButton());
        return builder;
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
                        "<green>Confirm to start/queue boost!",
                        () -> {
                            if (isReadyToConfirm()) {
                                SpawnBucketBoost boost = boostBuilder.build();
                                setActiveBoost.accept(boost);
                                close();
                            } else {
                                sendPlayerMessage("<red>You must fill out all fields before confirming!");
                                openUpdatedPage(this);
                            }
                        },
                        () -> openUpdatedPage(this)
                ).open())
                .build();
    }

    public ChestTemplate getTemplate() {
        ChestTemplate.Builder builder = ChestTemplate.builder(3);

        builder = builder.set(1, 0, getBucketButton());
        builder = builder.set(1, 1, getMultiplierButton());
        builder = builder.set(1, 2, getDurationButton());
        builder = builder.set(1, 3, getUnitButton());
        builder = builder.set(1, 4, getDetailsButton());

        if (getCurrentMode() != null)
            builder = addModifierButtons(builder);

        if (isReadyToConfirm())
            builder = builder.set(1, 7, getConfirmButton());

        return builder.fill(getFrame()).build();
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
        CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(TextUtils.deserialize(TextUtils.parse(rawMessage)));
    }
}
