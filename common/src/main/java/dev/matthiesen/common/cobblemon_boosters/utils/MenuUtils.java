package dev.matthiesen.common.cobblemon_boosters.utils;

import com.cobblemon.mod.common.CobblemonItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MenuUtils {
    public static final Item BACKGROUND = Items.GRAY_STAINED_GLASS_PANE;
    public static final Item PAGE_PLACEHOLDER = Items.PAPER;
    public static final Item NAV_ITEM = Items.ARROW;
    public static final Item BUCKET_ITEM = Items.SPAWNER;
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

    public static ItemBuilder getQueueEntryBuilder() {
        return new ItemBuilder(QUEUE_ENTRY_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal("Boost Queue Entry")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                );
    }

    public static ItemStack getFrameItem() {
        return new ItemBuilder(BACKGROUND)
                .setCustomName(Component.literal(" "))
                .build();
    }

    public static ItemStack getBucketItem() {
        return new ItemBuilder(BUCKET_ITEM)
                .setCustomName(
                        Component.literal("Spawn Bucket Boosters")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getCatchItem() {
        return new ItemBuilder(CATCH_ITEM)
                .setCustomName(
                        Component.literal("Catch Rate Boosters")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getExperienceItem() {
        return new ItemBuilder(EXPERIENCE_ITEM)
                .setCustomName(
                        Component.literal("Experience Rate Boosters")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getShinyItem() {
        return new ItemBuilder(SHINY_ITEM)
                .setCustomName(
                        Component.literal("Shiny Rate Boosters")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .build();
    }

    public static ItemStack getQueueItem(String name, boolean multiple) {
        return new ItemBuilder(QUEUE_ITEM)
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
        return new ItemBuilder(ADMIN_ITEM)
                .setCustomName(
                        Component.literal("Admin Menu")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getReloadItem() {
        return new ItemBuilder(RELOAD_ITEM)
                .setCustomName(
                        Component.literal("Reload Config")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getClearQueueItem() {
        return new ItemBuilder(CLEAR_QUEUE_ITEM)
                .setCustomName(
                        Component.literal("Clear All Queues")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getNoItem() {
        return new ItemBuilder(NO_ITEM)
                .setCustomName(
                        Component.literal("Cancel")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.RED)
                                )
                )
                .build();
    }

    public static ItemStack getYesItem() {
        return new ItemBuilder(YES_ITEM)
                .setCustomName(
                        Component.literal("Confirm")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.GREEN)
                                )
                )
                .build();
    }

    public static ItemStack getStopButton(boolean active) {
        return new ItemBuilder(STOP_ITEM)
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
        return new ItemBuilder(PAGE_PLACEHOLDER)
                .setCustomName(
                        Component.literal("Page " + currentPage + "/" + pageLength).withStyle(style -> style.withColor(ChatFormatting.GOLD))
                )
                .build();
    }

    public static ItemStack getNavItem(String label) {
        return new ItemBuilder(NAV_ITEM)
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
        return new ItemBuilder(STATUS_ITEM)
                .setCustomName(
                        Component.literal("View Active Boost")
                                .withStyle(
                                        style -> style.withColor(ChatFormatting.AQUA)
                                )
                )
                .setEnchanted(active)
                .build();
    }
}
