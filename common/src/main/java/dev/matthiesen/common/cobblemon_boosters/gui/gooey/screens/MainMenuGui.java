package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.BoostBuilderGui;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.BucketBoostBuilderGui;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BoostersGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
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
        var spawnBucketManager = CobblemonBoosters.INSTANCE.boostManager.getSpawnBucketBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages;
        var webhooks = CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;
        var permissions = PermissionRegistry.getPermissions();
        new BoostersGuiTemplate(
                "&bSpawn Bucket Boosts&r",
                boostType,
                player,
                spawnBucketManager.getActive(),
                spawnBucketManager.getQueue(),
                messages.noActiveBoosts,
                messages.boostStopped,
                messages.boostInfo,
                permissions.BUCKET_START_PERMISSION,
                permissions.BUCKET_STOP_PERMISSION,
                permissions.BUCKET_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                () -> new BucketBoostBuilderGui(
                        player,
                        boostType,
                        boost -> {
                            if (spawnBucketManager.getActive() == null) {
                                SpawnBucketBoost activeBoost = new SpawnBucketBoost(boost.getMultiplier(), boost.getDuration()).setBucket(boost.getBucket());
                                spawnBucketManager.setActive(activeBoost);
                                sendServerPlayerMessage(player, messages.boostStarted, activeBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        webhooks.spawnBucketEventStartEmbed,
                                        activeBoost
                                );
                                activeBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
                            } else {
                                SpawnBucketBoost newBoost = new SpawnBucketBoost(boost.getMultiplier(), boost.getDuration()).setBucket(boost.getBucket());
                                spawnBucketManager.appendToQueue(newBoost);
                                sendServerPlayerMessage(player, messages.boostAddedToQueued, newBoost);
                            }
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openCatchGUI(ServerPlayer player) {
        String boostType = "Catch";
        var catchBoostManager = CobblemonBoosters.INSTANCE.boostManager.getCatchBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.catchBoostMessages;
        var webhooks = CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;
        var permissions = PermissionRegistry.getPermissions();
        new BoostersGuiTemplate(
                "&dCatch Boosts&r",
                boostType,
                player,
                catchBoostManager.getActive(),
                catchBoostManager.getQueue(),
                messages.noActiveBoosts,
                messages.boostStopped,
                messages.boostInfo,
                permissions.CATCH_START_PERMISSION,
                permissions.CATCH_STOP_PERMISSION,
                permissions.CATCH_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        CatchBoost.class,
                        boost -> {
                            if (catchBoostManager.getActive() == null) {
                                CatchBoost activeBoost = new CatchBoost(boost.getMultiplier(), boost.getDuration());
                                catchBoostManager.setActive(activeBoost);
                                sendServerPlayerMessage(player, messages.boostStarted, activeBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        webhooks.catchEventStartEmbed,
                                        activeBoost
                                );
                                activeBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
                            } else {
                                CatchBoost newBoost = new CatchBoost(boost.getMultiplier(), boost.getDuration());
                                catchBoostManager.appendToQueue(newBoost);
                                sendServerPlayerMessage(player, messages.boostAddedToQueued, newBoost);
                            }
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openExperienceGUI(ServerPlayer player) {
        String boostType = "Experience";
        var experienceBoostManager = CobblemonBoosters.INSTANCE.boostManager.getExperienceBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages;
        var webhooks = CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;
        var permissions = PermissionRegistry.getPermissions();
        new BoostersGuiTemplate(
                "&aExperience Boosts&r",
                boostType,
                player,
                experienceBoostManager.getActive(),
                experienceBoostManager.getQueue(),
                messages.noActiveBoosts,
                messages.boostStopped,
                messages.boostInfo,
                permissions.EXPERIENCE_START_PERMISSION,
                permissions.EXPERIENCE_STOP_PERMISSION,
                permissions.EXPERIENCE_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        ExperienceBoost.class,
                        boost -> {
                            if (experienceBoostManager.getActive() == null) {
                                ExperienceBoost activeBoost = new ExperienceBoost(boost.getMultiplier(), boost.getDuration());
                                experienceBoostManager.setActive(activeBoost);
                                sendServerPlayerMessage(player, messages.boostStarted, activeBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        webhooks.experienceEventStartEmbed,
                                        activeBoost
                                );
                                activeBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
                            } else {
                                ExperienceBoost newBoost = new ExperienceBoost(boost.getMultiplier(), boost.getDuration());
                                experienceBoostManager.appendToQueue(newBoost);
                                sendServerPlayerMessage(player, messages.boostAddedToQueued, newBoost);
                            }
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openShinyGUI(ServerPlayer player) {
        String boostType = "Shiny";
        var shinyBoostManager = CobblemonBoosters.INSTANCE.boostManager.getShinyBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages;
        var webhooks = CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;
        var permissions = PermissionRegistry.getPermissions();
        new BoostersGuiTemplate(
                "&6Shiny Boosts&r",
                boostType,
                player,
                shinyBoostManager.getActive(),
                shinyBoostManager.getQueue(),
                messages.noActiveBoosts,
                messages.boostStopped,
                messages.boostInfo,
                permissions.SHINY_START_PERMISSION,
                permissions.SHINY_STOP_PERMISSION,
                permissions.SHINY_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        ShinyBoost.class,
                        boost -> {
                            if (shinyBoostManager.getActive() == null) {
                                ShinyBoost activeBoost = new ShinyBoost(boost.getMultiplier(), boost.getDuration());
                                shinyBoostManager.setActive(activeBoost);
                                sendServerPlayerMessage(player, messages.boostStarted, activeBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        webhooks.shinyEventStartEmbed,
                                        activeBoost
                                );
                                activeBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
                            } else {
                                ShinyBoost newBoost = new ShinyBoost(boost.getMultiplier(), boost.getDuration());
                                shinyBoostManager.appendToQueue(newBoost);
                                sendServerPlayerMessage(player, messages.boostAddedToQueued, newBoost);
                            }
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
