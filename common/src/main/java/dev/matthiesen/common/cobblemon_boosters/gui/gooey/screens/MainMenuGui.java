package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.boosters.*;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class MainMenuGui extends BaseMenuGuiTemplate {

    public MainMenuGui(ServerPlayer player) {
        super(player);
    }

    public static void openBucketGui(ServerPlayer player) {
        new BucketGui(
                "Spawn Bucket",
                player,
                CobblemonBoosters.INSTANCE.activeSpawnBucketBoost,
                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts,
                CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.noActiveBoosts,
                CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostStopped,
                CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_START_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_STOP_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_STATUS_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION,
                () -> {}
        ).open();
    }

    public static void openCatchGUI(ServerPlayer player) {
        new CatchGui(
                "Catch",
                player,
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noActiveBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostStopped,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.permissions.CATCH_START_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CATCH_STOP_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CATCH_STATUS_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION,
                () -> {}
        ).open();
    }

    public static void openExperienceGUI(ServerPlayer player) {
        new ExperienceGui(
                "Experience",
                player,
                CobblemonBoosters.INSTANCE.activeExperienceBoost,
                CobblemonBoosters.INSTANCE.queuedExperienceBoosts,
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.noActiveBoosts,
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostStopped,
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_START_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_STOP_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_STATUS_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION,
                () -> {}
        ).open();
    }

    public static void openShinyGUI(ServerPlayer player) {
        new ShinyGui(
                "Shiny",
                player,
                CobblemonBoosters.INSTANCE.activeShinyBoost,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.noActiveBoosts,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostStopped,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostInfo,
                CobblemonBoosters.INSTANCE.permissions.SHINY_START_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.SHINY_STOP_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.SHINY_STATUS_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION,
                () -> {}
        ).open();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        // Bucket Booster
        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.BUCKET_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getBucketItem())
                    .onClick(() -> openBucketGui(player))
                    .build()
            );

        // Catch Booster
        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.CATCH_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getCatchItem())
                    .onClick(() -> openCatchGUI(player))
                    .build()
            );

        // Experience Booster
        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getExperienceItem())
                    .onClick(() -> openExperienceGUI(player))
                    .build()
            );

        // Shiny Booster
        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.SHINY_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getShinyItem())
                    .onClick(() -> openShinyGUI(player))
                    .build()
            );

        // Check Queues
        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getQueueItem("All", true))
                    .onClick(() -> new CheckQueuesGui(player).open())
                    .build()
            );

        // Admin Options
        if (ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.RELOAD_PERMISSION) ||
                ModPermissions.checkPermission(player, CobblemonBoosters.INSTANCE.permissions.CLEAR_QUEUES_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getAdminItem())
                    .onClick(() -> new AdminGui(player).open())
                    .build()
            );

        return buttons;
    }
}
