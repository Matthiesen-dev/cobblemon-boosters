package dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoosters;
import dev.matthiesen.cobblemon_boosters.common.config.CacheConfig;
import dev.matthiesen.cobblemon_boosters.common.data.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.data.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.data.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.data.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.subscreens.BoostBuilderGui;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.subscreens.BucketBoostBuilderGui;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.gui.gooey.screens.templates.BoostersGuiTemplate;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class MainMenuGui extends BaseMenuGuiTemplate {

    public MainMenuGui(ServerPlayer player) {
        super(player);
    }

    public static void sendServerPlayerMessage(ServerPlayer player, String rawMessage, IBoost boost) {
        player.sendSystemMessage(TextUtils.deserialize(TextUtils.parse(rawMessage, boost)));
    }

    public static void openBucketGui(ServerPlayer player) {
        String boostType = "Spawn Bucket";
        var spawnBucketManager = BoostManager.getSpawnBucketBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages;
        var permissions = PermissionRegistry.getPermissions();
        new BoostersGuiTemplate(
                "&bSpawn Bucket Boosts&r",
                boostType,
                player,
                spawnBucketManager,
                messages,
                permissions.BUCKET_START_PERMISSION,
                permissions.BUCKET_STOP_PERMISSION,
                permissions.BUCKET_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                () -> new BucketBoostBuilderGui(
                        player,
                        boostType,
                        boost -> {
                            SpawnBucketBoost newBoost = new SpawnBucketBoost(boost.getMultiplier(), boost.getDuration()).setBucket(boost.getBucket());
                            spawnBucketManager.appendToQueue(newBoost);
                            sendServerPlayerMessage(player, messages.boostAddedToQueued, newBoost);
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openCatchGUI(ServerPlayer player) {
        String boostType = "Catch";
        var catchBoostManager = BoostManager.getCatchBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.catchBoostMessages;
        var permissions = PermissionRegistry.getPermissions();
        new BoostersGuiTemplate(
                "&dCatch Boosts&r",
                boostType,
                player,
                catchBoostManager,
                messages,
                permissions.CATCH_START_PERMISSION,
                permissions.CATCH_STOP_PERMISSION,
                permissions.CATCH_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        CatchBoost.class,
                        boost -> {
                            CatchBoost newBoost = new CatchBoost(boost.getMultiplier(), boost.getDuration());
                            catchBoostManager.appendToQueue(newBoost);
                            sendServerPlayerMessage(player, messages.boostAddedToQueued, newBoost);
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openExperienceGUI(ServerPlayer player) {
        String boostType = "Experience";
        var experienceBoostManager = BoostManager.getExperienceBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages;
        var permissions = PermissionRegistry.getPermissions();
        new BoostersGuiTemplate(
                "&aExperience Boosts&r",
                boostType,
                player,
                experienceBoostManager,
                messages,
                permissions.EXPERIENCE_START_PERMISSION,
                permissions.EXPERIENCE_STOP_PERMISSION,
                permissions.EXPERIENCE_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        ExperienceBoost.class,
                        boost -> {
                            ExperienceBoost newBoost = new ExperienceBoost(boost.getMultiplier(), boost.getDuration());
                            experienceBoostManager.appendToQueue(newBoost);
                            sendServerPlayerMessage(player, messages.boostAddedToQueued, newBoost);
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openShinyGUI(ServerPlayer player) {
        String boostType = "Shiny";
        var shinyBoostManager = BoostManager.getShinyBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages;
        var permissions = PermissionRegistry.getPermissions();
        new BoostersGuiTemplate(
                "&6Shiny Boosts&r",
                boostType,
                player,
                shinyBoostManager,
                messages,
                permissions.SHINY_START_PERMISSION,
                permissions.SHINY_STOP_PERMISSION,
                permissions.SHINY_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        ShinyBoost.class,
                        boost -> {
                            ShinyBoost newBoost = new ShinyBoost(boost.getMultiplier(), boost.getDuration());
                            shinyBoostManager.appendToQueue(newBoost);
                            sendServerPlayerMessage(player, messages.boostAddedToQueued, newBoost);
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    @Override
    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        var permissions = PermissionRegistry.getPermissions();

        // Bucket Booster
        if (PermissionRegistry.checkPermission(player, permissions.BUCKET_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getBucketItem())
                    .onClick(() -> openBucketGui(player))
                    .build()
            );

        // Catch Booster
        if (PermissionRegistry.checkPermission(player, permissions.CATCH_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getCatchItem())
                    .onClick(() -> openCatchGUI(player))
                    .build()
            );

        // Experience Booster
        if (PermissionRegistry.checkPermission(player, permissions.EXPERIENCE_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getExperienceItem())
                    .onClick(() -> openExperienceGUI(player))
                    .build()
            );

        // Shiny Booster
        if (PermissionRegistry.checkPermission(player, permissions.SHINY_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getShinyItem())
                    .onClick(() -> openShinyGUI(player))
                    .build()
            );

        // Check Queues
        if (PermissionRegistry.checkPermission(player, permissions.CHECK_QUEUE_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getQueueItem("All", true))
                    .onClick(() -> new CheckQueuesGui(player).open())
                    .build()
            );

        // Admin Options
        if (PermissionRegistry.checkPermission(player, permissions.RELOAD_PERMISSION) ||
                PermissionRegistry.checkPermission(player, permissions.CLEAR_QUEUES_PERMISSION))
            buttons.add(GooeyButton.builder()
                    .display(MenuUtils.getAdminItem())
                    .onClick(() -> new AdminGui(player).open())
                    .build()
            );

        return buttons;
    }
}
