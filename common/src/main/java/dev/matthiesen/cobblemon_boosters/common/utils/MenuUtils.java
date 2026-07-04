package dev.matthiesen.cobblemon_boosters.common.utils;

import com.cobblemon.mod.common.CobblemonItems;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoosters;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class MenuUtils {
    public static final Item BACKGROUND = Items.GRAY_STAINED_GLASS_PANE;
    public static final Item PAGE_PLACEHOLDER = Items.PAPER;
    public static final Item NAV_ITEM = Items.ARROW;
    public static final Item CATCH_ITEM = CobblemonItems.POKE_BALL;
    public static final Item EXPERIENCE_ITEM = Items.EXPERIENCE_BOTTLE;
    public static final Item SHINY_ITEM = CobblemonItems.SHINY_STONE;
    public static final Item QUEUE_ITEM = Items.CLOCK;
    public static final Item ADMIN_ITEM = Items.COMMAND_BLOCK;
    public static final Item RELOAD_ITEM = Items.WIND_CHARGE;
    public static final Item CLEAR_QUEUE_ITEM = Items.BARRIER;
    public static final Item YES_ITEM = Items.GREEN_STAINED_GLASS_PANE;
    public static final Item NO_ITEM = Items.RED_STAINED_GLASS_PANE;
    public static final Item STOP_ITEM = Items.REDSTONE_BLOCK;
    public static final Item QUEUE_ENTRY_ITEM = Items.NAME_TAG;
    public static final Item STATUS_ITEM = Items.BOOK;
    public static final Item CREATE_NEW_BOOSTER_ITEM = Items.EMERALD;
    public static final Item PLUS_ITEM = Items.LIME_STAINED_GLASS_PANE;
    public static final Item MINUS_ITEM = Items.RED_STAINED_GLASS_PANE;
    public static final Item BOOST_CONFIRM_ITEM = Items.GREEN_WOOL;
    public static final Item BUCKET_ITEM = Items.TURTLE_SPAWN_EGG;

    public static ItemStack getConfirmItem() {
        return new BoostersItemBuilder(BOOST_CONFIRM_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Confirm Boost")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.GREEN)
                                )
                )
                .build();
    }

    public static BoostersItemBuilder getQueueEntryBuilder() {
        return new BoostersItemBuilder(QUEUE_ENTRY_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Boost Queue Entry")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                );
    }

    public static BoostersItemBuilder getDetailsItemBuilder() {
        return new BoostersItemBuilder(STATUS_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("View Boost Details")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                );
    }

    public static ItemStack getPlusItem() {
        return new BoostersItemBuilder(PLUS_ITEM)
                .hideAdditional()
                .setCustomName(Component.literal("Increment")
                        .withStyle(
                                style -> style.withColor(ChatFormatting.GREEN)
                        )
                )
                .build();
    }

    public static ItemStack getMinusItem() {
        return new BoostersItemBuilder(MINUS_ITEM)
                .hideAdditional()
                .setCustomName(Component.literal("Decrement")
                        .withStyle(
                                style -> style.withColor(ChatFormatting.RED)
                        )
                )
                .build();
    }

    public static ItemStack getFrameItem() {
        return new BoostersItemBuilder(BACKGROUND)
                .hideAdditional()
                .setCustomName(Component.literal(" "))
                .build();
    }

    public static ItemStack getBucketItem() {
        Item item = BUCKET_ITEM;

        if (CobblemonBoosters.INSTANCE.COBBREEDING_AVAILABLE) {
            item = BuiltInRegistries.ITEM.getOptional(Constants.COMPAT.COBBREEDING_EGG).orElse(BUCKET_ITEM);
        }

        return new BoostersItemBuilder(item)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Spawn Bucket Boosters")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getCatchItem() {
        return new BoostersItemBuilder(CATCH_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Catch Rate Boosters")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getExperienceItem() {
        return new BoostersItemBuilder(EXPERIENCE_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Experience Rate Boosters")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getShinyItem() {
        return new BoostersItemBuilder(SHINY_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Shiny Rate Boosters")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getQueueItemForSubscreen(String name) {
        return new BoostersItemBuilder(QUEUE_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("View " + name + " Queue")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getQueueItem(String name, boolean multiple) {

        Item queueItem = QUEUE_ITEM;
        switch (name) {
            case "Spawn Bucket" -> queueItem = getBucketItem().getItem();
            case "Catch" -> queueItem = getCatchItem().getItem();
            case "Experience" -> queueItem = getExperienceItem().getItem();
            case "Shiny" -> queueItem = getShinyItem().getItem();
        }

        return new BoostersItemBuilder(queueItem)
                .hideAdditional()
                .setCustomName(
                        Component.literal("View " + name + " Queue" + (multiple ? "s" : ""))
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getQueueItem(String name) {
        return getQueueItem(name, false);
    }

    public static ItemStack getAdminItem() {
        return new BoostersItemBuilder(ADMIN_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Admin Menu")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getReloadItem() {
        return new BoostersItemBuilder(RELOAD_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Reload Config")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getClearQueueItem() {
        return new BoostersItemBuilder(CLEAR_QUEUE_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Clear All Queues")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getNoItem() {
        return new BoostersItemBuilder(NO_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Cancel")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getYesItem() {
        return new BoostersItemBuilder(YES_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Confirm")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.GREEN)
                                )
                )
                .build();
    }

    public static ItemStack getStopButton(boolean active) {
        return new BoostersItemBuilder(STOP_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Stop Active Boost")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .setEnchanted(active)
                .build();
    }

    public static ItemStack getPageItem(int currentPage, int pageLength) {
        return new BoostersItemBuilder(PAGE_PLACEHOLDER)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Page " + currentPage + "/" + pageLength).withStyle(style -> style.withColor(ChatFormatting.GOLD))
                )
                .build();
    }

    public static ItemStack getNavItem(String label) {
        return new BoostersItemBuilder(NAV_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(label)
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getStatusItem(boolean active) {
        return new BoostersItemBuilder(STATUS_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("View Active Boost")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .setEnchanted(active)
                .build();
    }

    public static ItemStack getCreateNewBoosterItem(String label) {
        return new BoostersItemBuilder(CREATE_NEW_BOOSTER_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Start New " + label)
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.GREEN)
                                )
                )
                .build();
    }
}
