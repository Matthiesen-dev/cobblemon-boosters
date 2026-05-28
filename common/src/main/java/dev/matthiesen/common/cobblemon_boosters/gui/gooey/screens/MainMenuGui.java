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
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class MainMenuGui extends BaseMenuGuiTemplate {

    public MainMenuGui(ServerPlayer player) {
        super(player);
    }

    public static void sendServerPlayerMessage(ServerPlayer player, String rawMessage, IBoost boost) {
        player.sendSystemMessage(TextUtils.deserialize(TextUtils.parse(rawMessage, boost)));
    }

    public static void openBucketGui(ServerPlayer player) {
        String boostType = "Spawn Bucket";
        new BoostersGuiTemplate(
                "&bSpawn Bucket Boosts&r",
                boostType,
                player,
                CobblemonBoosters.INSTANCE.activeSpawnBucketBoost,
                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.noActiveBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.boostStopped,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_START_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_STOP_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_STATUS_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION,
                () -> new BucketBoostBuilderGui(
                        player,
                        boostType,
                        boost -> {
                            if (CobblemonBoosters.INSTANCE.activeSpawnBucketBoost == null) {
                                CobblemonBoosters.INSTANCE.activeSpawnBucketBoost = new SpawnBucketBoost(boost.getMultiplier(), boost.getDuration()).setBucket(boost.getBucket());
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeSpawnBucketBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        CobblemonBoosters.INSTANCE.WEBHOOKS_CONFIG_MANAGER.getConfig().discordWebhookConfig.spawnBucketEventStartEmbed,
                                        CobblemonBoosters.INSTANCE.activeSpawnBucketBoost
                                );
                                CobblemonBoosters.INSTANCE.activeSpawnBucketBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
                            } else {
                                SpawnBucketBoost newBoost = new SpawnBucketBoost(boost.getMultiplier(), boost.getDuration()).setBucket(boost.getBucket());
                                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts.add(newBoost);
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.boostAddedToQueued, newBoost);
                            }
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openCatchGUI(ServerPlayer player) {
        String boostType = "Catch";
        new BoostersGuiTemplate(
                "&dCatch Boosts&r",
                boostType,
                player,
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.catchBoostMessages.noActiveBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.catchBoostMessages.boostStopped,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.catchBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.permissions.CATCH_START_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CATCH_STOP_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CATCH_STATUS_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        CatchBoost.class,
                        boost -> {
                            if (CobblemonBoosters.INSTANCE.activeCatchBoost == null) {
                                CobblemonBoosters.INSTANCE.activeCatchBoost = new CatchBoost(boost.getMultiplier(), boost.getDuration());
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.catchBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeCatchBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        CobblemonBoosters.INSTANCE.WEBHOOKS_CONFIG_MANAGER.getConfig().discordWebhookConfig.catchEventStartEmbed,
                                        CobblemonBoosters.INSTANCE.activeCatchBoost
                                );
                                CobblemonBoosters.INSTANCE.activeCatchBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
                            } else {
                                CatchBoost newBoost = new CatchBoost(boost.getMultiplier(), boost.getDuration());
                                CobblemonBoosters.INSTANCE.queuedCatchBoosts.add(newBoost);
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.catchBoostMessages.boostAddedToQueued, newBoost);
                            }
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openExperienceGUI(ServerPlayer player) {
        String boostType = "Experience";
        new BoostersGuiTemplate(
                "&aExperience Boosts&r",
                boostType,
                player,
                CobblemonBoosters.INSTANCE.activeExperienceBoost,
                CobblemonBoosters.INSTANCE.queuedExperienceBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.experienceBoostMessages.noActiveBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.experienceBoostMessages.boostStopped,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.experienceBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_START_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_STOP_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_STATUS_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        ExperienceBoost.class,
                        boost -> {
                            if (CobblemonBoosters.INSTANCE.activeExperienceBoost == null) {
                                CobblemonBoosters.INSTANCE.activeExperienceBoost = new ExperienceBoost(boost.getMultiplier(), boost.getDuration());
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.experienceBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeExperienceBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        CobblemonBoosters.INSTANCE.WEBHOOKS_CONFIG_MANAGER.getConfig().discordWebhookConfig.experienceEventStartEmbed,
                                        CobblemonBoosters.INSTANCE.activeExperienceBoost
                                );
                                CobblemonBoosters.INSTANCE.activeExperienceBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
                            } else {
                                ExperienceBoost newBoost = new ExperienceBoost(boost.getMultiplier(), boost.getDuration());
                                CobblemonBoosters.INSTANCE.queuedExperienceBoosts.add(newBoost);
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.experienceBoostMessages.boostAddedToQueued, newBoost);
                            }
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openShinyGUI(ServerPlayer player) {
        String boostType = "Shiny";
        new BoostersGuiTemplate(
                "&6Shiny Boosts&r",
                boostType,
                player,
                CobblemonBoosters.INSTANCE.activeShinyBoost,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.noActiveBoosts,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostStopped,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostInfo,
                CobblemonBoosters.INSTANCE.permissions.SHINY_START_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.SHINY_STOP_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.SHINY_STATUS_PERMISSION,
                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION,
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        ShinyBoost.class,
                        boost -> {
                            if (CobblemonBoosters.INSTANCE.activeShinyBoost == null) {
                                CobblemonBoosters.INSTANCE.activeShinyBoost = new ShinyBoost(boost.getMultiplier(), boost.getDuration());
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostStarted, CobblemonBoosters.INSTANCE.activeShinyBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        CobblemonBoosters.INSTANCE.WEBHOOKS_CONFIG_MANAGER.getConfig().discordWebhookConfig.shinyEventStartEmbed,
                                        CobblemonBoosters.INSTANCE.activeShinyBoost
                                );
                                CobblemonBoosters.INSTANCE.activeShinyBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
                            } else {
                                ShinyBoost newBoost = new ShinyBoost(boost.getMultiplier(), boost.getDuration());
                                CobblemonBoosters.INSTANCE.queuedShinyBoosts.add(newBoost);
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostAddedToQueued, newBoost);
                            }
                            CacheConfig.setGlobalBoostData();
                        }
                ).open()
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
