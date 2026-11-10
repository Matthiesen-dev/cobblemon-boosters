package dev.matthiesen.cobblemon_boosters.common.services.gui.core.screens.utils;

import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGui;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.Button;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.PlaceholderButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.button.ServerButton;
import dev.matthiesen.matthiesen_core.common.api.server_menu.template.types.ChestTemplate;
import net.minecraft.ChatFormatting;
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
    public static final List<String> allowedBuckets = List.of("common", "uncommon", "rare", "ultra-rare", "boss");

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean ensureModeSelected(String currentMode, Consumer<String> sendMessage) {
        if (currentMode != null) {
            return true;
        }
        sendMessage.accept(BoostersConfig.TEXT_CONFIG.messages_error_selectModeFirst.get());
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
        var placeholders = TextUtils.getPlaceholders();
        return TextUtils.getTextParser().parse(rawText, placeholders);
    }

    public static Component text(String rawText, IBoost boost) {
        var placeholders = TextUtils.getPlaceholders(boost);
        return TextUtils.getTextParser().parse(rawText, placeholders);
    }

    public static void sendPlayerMessage(ServerPlayer player, String rawMessage) {
        player.sendSystemMessage(text(rawMessage));
    }

    public static Component getBoostBuilderTitle() {
        return text(BoostersConfig.TEXT_CONFIG.gui_boosterBuilder_title.get());
    }

    public static ChestTemplate.Builder addModifierButtons(ChestTemplate.Builder builder, Button subtractButton, Button addButton) {
        return builder
                .set(1, 6, subtractButton)
                .set(1, 7, addButton);
    }

    public static Button getFrame() {
        return ServerButton.builder()
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
            Map<String, ChatFormatting> labelToColor,
            Supplier<String> getCurrentMode,
            Function<String, T> setCurrentMode,
            Function<T, Void> openUpdatedPage,
            T page
    ) {
        List<Component> lore = new ArrayList<>();

        var placeholders = TextUtils.getPlaceholders();
        var config = BoostersConfig.TEXT_CONFIG;

        if (value != null) {
            String message = String.format(config.gui_boosterBuilder_mode_current.get(), value);
            lore.add(TextUtils.getTextParser().parse(message, placeholders));
        } else {
            lore.add(TextUtils.getTextParser().parse(config.gui_boosterBuilder_mode_notSet.get(), placeholders));
        }

        Component[] loreArray = toComponentArray(lore);

        var currentMode = getCurrentMode.get();

        boolean isActive = currentMode != null && currentMode.equals(label.toLowerCase());

        return ServerButton.builder()
                .display(MenuUtils.getQueueEntryBuilder()
                        .setCustomName(
                                TextUtils.getTextParser()
                                        .parse(label, placeholders)
                                        .copy()
                                        .withStyle(labelToColor.get(label))
                        )
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
        var placeholders = TextUtils.getPlaceholders();
        var config = BoostersConfig.TEXT_CONFIG;

        String multiplierText = config.gui_boosterBuilder_multiplier.get();
        if (boostBuilder.multiplier != null) {
            lore.add(TextUtils.getTextParser().parse(String.format(multiplierText, boostBuilder.multiplier), placeholders));
        } else {
            lore.add(TextUtils.getTextParser().parse(String.format(multiplierText, config.gui_boosterBuilder_noValue.get()), placeholders));
        }

        String durationText = config.gui_boosterBuilder_duration.get();
        if (boostBuilder.duration != null) {
            lore.add(TextUtils.getTextParser().parse(String.format(durationText, boostBuilder.duration), placeholders));
        } else {
            lore.add(TextUtils.getTextParser().parse(String.format(durationText, config.gui_boosterBuilder_noValue.get()), placeholders));
        }

        String unitText = config.gui_boosterBuilder_unit.get();
        if (boostBuilder.unit != null) {
            lore.add(TextUtils.getTextParser().parse(String.format(unitText, boostBuilder.unit), placeholders));
        } else {
            lore.add(TextUtils.getTextParser().parse(String.format(unitText, config.gui_boosterBuilder_noValue.get()), placeholders));
        }

        Component[] loreArray = toComponentArray(lore);

        return ServerButton.builder()
                .display(MenuUtils.getDetailsItemBuilder()
                        .setCustomName(TextUtils.getTextParser().parse(config.gui_boosterBuilder_details.get(), placeholders))
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
