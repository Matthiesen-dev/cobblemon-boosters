package dev.matthiesen.cobblemon_boosters.common.utils;

import com.cobblemon.mod.common.CobblemonItems;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
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
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_confirmBoost.get())
                                .withStyle(
                                        style -> style.withColor(
                                                BoostersConfig.TEXT_CONFIG.gui_buttons_confirmBoost_color.get().getFormat()
                                        )
                                )
                )
                .build();
    }

    public static BoostersItemBuilder getQueueEntryBuilder() {
        return new BoostersItemBuilder(QUEUE_ENTRY_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_boostQueueEntry.get())
                                .withStyle(
                                        style -> style.withColor(
                                                BoostersConfig.TEXT_CONFIG.gui_buttons_boostQueueEntry_color.get().getFormat()
                                        )
                                )
                );
    }

    public static BoostersItemBuilder getDetailsItemBuilder() {
        return new BoostersItemBuilder(STATUS_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_viewBoostDetails.get())
                                .withStyle(
                                        style -> style.withColor(
                                                BoostersConfig.TEXT_CONFIG.gui_buttons_viewBoostDetails_color.get().getFormat()
                                        )
                                )
                );
    }

    public static ItemStack getPlusItem() {
        return new BoostersItemBuilder(PLUS_ITEM)
                .hideAdditional()
                .setCustomName(Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_increment.get())
                        .withStyle(
                                style -> style.withColor(
                                        BoostersConfig.TEXT_CONFIG.gui_buttons_increment_color.get().getFormat()
                                )
                        )
                )
                .build();
    }

    public static ItemStack getMinusItem() {
        return new BoostersItemBuilder(MINUS_ITEM)
                .hideAdditional()
                .setCustomName(Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_decrement.get())
                        .withStyle(
                                style -> style.withColor(
                                        BoostersConfig.TEXT_CONFIG.gui_buttons_decrement_color.get().getFormat()
                                )
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

        if (ServiceManager.isCobbreedingAvailable()) {
            item = BuiltInRegistries.ITEM.getOptional(ServiceManager.COMPAT.COBBREEDING_EGG).orElse(BUCKET_ITEM);
        }

        return new BoostersItemBuilder(item)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_spawnBucketBoosters.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_spawnBucketBoosters_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getCatchItem() {
        return new BoostersItemBuilder(CATCH_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_catchBoosters.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_catchBoosters_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getExperienceItem() {
        return new BoostersItemBuilder(EXPERIENCE_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_experienceBoosters.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_experienceBoosters_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getShinyItem() {
        return new BoostersItemBuilder(SHINY_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_shinyBoosters.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_shinyBoosters_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getQueueItemForSubscreen(String name) {
        String text = String.format(BoostersConfig.TEXT_CONFIG.gui_buttons_viewQueue.get(), name);
        return new BoostersItemBuilder(QUEUE_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(text)
                                .withStyle(
                                        style -> style.withColor(
                                                BoostersConfig.TEXT_CONFIG.gui_buttons_viewQueue_color.get().getFormat()
                                        )
                                )
                )
                .build();
    }

    public static ItemStack getQueueItem(Item item, String name, boolean multiple) {
        String text = String.format(
                multiple ? BoostersConfig.TEXT_CONFIG.gui_buttons_viewQueue_multiple.get() : BoostersConfig.TEXT_CONFIG.gui_buttons_viewQueue.get(),
                name
        );
        return new BoostersItemBuilder(item)
                .hideAdditional()
                .setCustomName(
                        Component.literal(text)
                                .withStyle(style -> style.withColor(multiple
                                        ? BoostersConfig.TEXT_CONFIG.gui_buttons_viewQueue_multiple_color.get().getFormat()
                                        : BoostersConfig.TEXT_CONFIG.gui_buttons_viewQueue_color.get().getFormat()
                                ))
                )
                .build();
    }

    public static ItemStack getQueueItem(String name, boolean multiple) {
        Item queueItem = QUEUE_ITEM;
        var config = BoostersConfig.TEXT_CONFIG;

        if (name.equals(config.gui_boosterTitles_spawnBucket_displayName.get())) {
            queueItem = getBucketItem().getItem();
        } else if (name.equals(config.gui_boosterTitles_catch_displayName.get())) {
            queueItem = getCatchItem().getItem();
        } else if (name.equals(config.gui_boosterTitles_experience_displayName.get())) {
            queueItem = getExperienceItem().getItem();
        } else if (name.equals(config.gui_boosterTitles_shiny_displayName.get())) {
            queueItem = getShinyItem().getItem();
        }

        return getQueueItem(queueItem, name, multiple);
    }

    public static ItemStack getAdminItem() {
        return new BoostersItemBuilder(ADMIN_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_adminMenu.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_adminMenu_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getReloadItem() {
        return new BoostersItemBuilder(RELOAD_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_reloadConfig.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_reloadConfig_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getClearQueueItem() {
        return new BoostersItemBuilder(CLEAR_QUEUE_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_clearAllQueues.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_clearAllQueues_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getNoItem() {
        return new BoostersItemBuilder(NO_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_cancel.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_cancel_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getYesItem() {
        return new BoostersItemBuilder(YES_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_confirm.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_confirm_color.get().getFormat())
                                )
                )
                .build();
    }

    public static ItemStack getStopButton(boolean active) {
        return new BoostersItemBuilder(STOP_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_stopActiveBoost.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_stopActiveBoost_color.get().getFormat())
                                )
                )
                .setEnchanted(active)
                .build();
    }

    public static ItemStack getPageItem(int currentPage, int pageLength) {
        String text = String.format(BoostersConfig.TEXT_CONFIG.gui_buttons_pageText.get(), currentPage, pageLength);
        return new BoostersItemBuilder(PAGE_PLACEHOLDER)
                .hideAdditional()
                .setCustomName(
                        Component.literal(text)
                                .withStyle(style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_pageText_color.get().getFormat()))
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
                        Component.literal(BoostersConfig.TEXT_CONFIG.gui_buttons_viewActiveBoost.get())
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_viewActiveBoost_color.get().getFormat())
                                )
                )
                .setEnchanted(active)
                .build();
    }

    public static ItemStack getCreateNewBoosterItem(String label) {
        String text = String.format(BoostersConfig.TEXT_CONFIG.gui_buttons_startBoost.get(), label);
        return new BoostersItemBuilder(CREATE_NEW_BOOSTER_ITEM)
                .hideAdditional()
                .setCustomName(
                        Component.literal(text)
                                .withStyle(
                                        style -> style.withColor(BoostersConfig.TEXT_CONFIG.gui_buttons_startBoost_color.get().getFormat())
                                )
                )
                .build();
    }
}
