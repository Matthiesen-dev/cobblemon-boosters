package dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.utils;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Helpers {
    public static final List<String> allowedUnits = List.of("seconds", "minutes", "hours", "days");
    public static final List<String> allowedBuckets = List.of("common", "uncommon", "rare", "ultra-rare");

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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean ensureModeSelected(String currentMode, Consumer<String> sendMessage) {
        if (currentMode != null) {
            return true;
        }
        sendMessage.accept("&cPlease select a field to modify first by clicking on its button!");
        return false;
    }

    public static boolean applyBaseAddForMode(String mode, BaseBoostBuilder boostBuilder, List<String> allowedUnits) {
        switch (mode) {
            case "multiplier" -> {
                if (boostBuilder.multiplier == null) {
                    boostBuilder.setMultiplier(1.0f);
                } else {
                    boostBuilder.setMultiplier(boostBuilder.multiplier + 1.0f);
                }
                return true;
            }
            case "duration" -> {
                if (boostBuilder.duration == null) {
                    boostBuilder.setDuration(1);
                } else {
                    boostBuilder.setDuration(boostBuilder.duration + 1);
                }
                return true;
            }
            case "unit" -> {
                boostBuilder.setUnit(nextCyclicValue(boostBuilder.unit, allowedUnits, "seconds"));
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public static boolean applyBaseSubtractForMode(String mode, BaseBoostBuilder boostBuilder, List<String> allowedUnits) {
        switch (mode) {
            case "multiplier" -> {
                if (boostBuilder.multiplier != null) {
                    boostBuilder.setMultiplier(boostBuilder.multiplier - 1.0f);
                    if (boostBuilder.multiplier < 0) boostBuilder.setMultiplier(1.0f);
                } else {
                    boostBuilder.setMultiplier(1.0f);
                }
                return true;
            }
            case "duration" -> {
                if (boostBuilder.duration != null) {
                    boostBuilder.setDuration(boostBuilder.duration - 1);
                    if (boostBuilder.duration < 0) boostBuilder.setDuration(1);
                } else {
                    boostBuilder.setDuration(1);
                }
                return true;
            }
            case "unit" -> {
                boostBuilder.setUnit(previousCyclicValue(boostBuilder.unit, allowedUnits, "seconds"));
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public static String nextCyclicValue(String currentValue, List<String> allowedValues, String fallback) {
        if (currentValue == null) {
            return fallback;
        }
        int currentIndex = allowedValues.indexOf(currentValue);
        if (currentIndex == -1) {
            return fallback;
        }
        int nextIndex = (currentIndex + 1) % allowedValues.size();
        return allowedValues.get(nextIndex);
    }

    public static String previousCyclicValue(String currentValue, List<String> allowedValues, String fallback) {
        if (currentValue == null) {
            return fallback;
        }
        int currentIndex = allowedValues.indexOf(currentValue);
        if (currentIndex == -1) {
            return fallback;
        }
        int nextIndex = (currentIndex - 1 + allowedValues.size()) % allowedValues.size();
        return allowedValues.get(nextIndex);
    }

    public static Component text(String rawText) {
        return TextUtils.deserialize(TextUtils.parse(rawText));
    }

    public static Component text(String rawText, IBoost boost) {
        return TextUtils.deserialize(TextUtils.parse(rawText, boost));
    }

    public static void sendPlayerMessage(ServerPlayer player, String rawMessage) {
        player.sendSystemMessage(text(rawMessage));
    }

    public static Component getBoostBuilderTitle() {
        return text("Boost Builder");
    }

    public static ChestTemplate.Builder addModifierButtons(ChestTemplate.Builder builder, Button subtractButton, Button addButton) {
        return builder
                .set(1, 6, subtractButton)
                .set(1, 7, addButton);
    }

    public static Button getFrame() {
        return GooeyButton.builder()
                .display(MenuUtils.getFrameItem())
                .build();
    }

    public static PlaceholderButton getPlaceholder() {
        return new PlaceholderButton();
    }

    public static ChestTemplate.Builder getBaseChestTemplate() {
        return ChestTemplate.builder(3)
                .row(0, Helpers.getFrame())
                .row(1, Helpers.getFrame())
                .set(1, 1, Helpers.getPlaceholder())
                .set(1, 2, Helpers.getPlaceholder())
                .set(1, 3, Helpers.getPlaceholder())
                .set(1, 4, Helpers.getPlaceholder())
                .set(1, 5, Helpers.getPlaceholder())
                .set(1, 6, Helpers.getPlaceholder())
                .set(1, 7, Helpers.getPlaceholder())
                .row(2, Helpers.getFrame());
    }

    public static <T extends IGui> Button buildModeButton(
            String label,
            Object value,
            Map<String, String> labelToColor,
            Supplier<String> getCurrentMode,
            Function<String, T> setCurrentMode,
            Function<T, Void> openUpdatedPage,
            T page
    ) {
        List<Component> lore = new ArrayList<>();

        if (value != null) {
            lore.add(TextUtils.deserialize(TextUtils.parse("&7Current: &f" + value)));
        } else {
            lore.add(TextUtils.deserialize(TextUtils.parse("&7Current: &cNot set")));
        }

        Component[] loreArray = toComponentArray(lore);

        var currentMode = getCurrentMode.get();

        boolean isActive = currentMode != null && currentMode.equals(label.toLowerCase());

        return GooeyButton.builder()
                .display(MenuUtils.getQueueEntryBuilder()
                        .setCustomName(TextUtils.deserialize(TextUtils.parse(labelToColor.get(label) + label)))
                        .addLore(loreArray)
                        .setEnchanted(isActive)
                        .build()
                )
                .onClick(() -> {
                    if (isActive) {
                        setCurrentMode.apply(null);
                    } else {
                        setCurrentMode.apply(label.toLowerCase());
                    }
                    openUpdatedPage.apply(page);
                })
                .build();
    }

    public static Button getDetailsButton(List<Component> lore, BaseBoostBuilder boostBuilder) {
        if (boostBuilder.multiplier != null) {
            lore.add(TextUtils.deserialize(TextUtils.parse("&7Multiplier: &f" + boostBuilder.multiplier)));
        } else {
            lore.add(TextUtils.deserialize(TextUtils.parse("&7Multiplier: &cNot set")));
        }

        if (boostBuilder.duration != null) {
            lore.add(TextUtils.deserialize(TextUtils.parse("&7Duration: &f" + boostBuilder.duration)));
        } else {
            lore.add(TextUtils.deserialize(TextUtils.parse("&7Duration: &cNot set")));
        }

        if (boostBuilder.unit != null) {
            lore.add(TextUtils.deserialize(TextUtils.parse("&7Unit: &f" + boostBuilder.unit)));
        } else {
            lore.add(TextUtils.deserialize(TextUtils.parse("&7Unit: &cNot set")));
        }

        Component[] loreArray = toComponentArray(lore);

        return GooeyButton.builder()
                .display(MenuUtils.getDetailsItemBuilder()
                        .setCustomName(TextUtils.deserialize(TextUtils.parse("&6Details")))
                        .addLore(loreArray)
                        .build()
                )
                .build();
    }

    public static Component[] toComponentArray(List<Component> components) {
        Component[] result = new Component[components.size()];
        for (int i = 0; i < components.size(); i++) {
            result[i] = components.get(i);
        }
        return result;
    }
}
