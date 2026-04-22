package dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.boosters.*;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.BoostBuilderGui;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.subscreens.BucketBoostBuilderGui;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.templates.BaseMenuGuiTemplate;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.MenuUtils;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class MainMenuGui extends BaseMenuGuiTemplate {

    public MainMenuGui(ServerPlayer player) {
        super(player);
    }

    public static void sendServerPlayerMessage(ServerPlayer player, String rawMessage, IBoost boost) {
        CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(TextUtils.deserialize(TextUtils.parse(rawMessage, boost)));
    }

    public static void openBucketGui(ServerPlayer player) {
        String boostType = "Spawn Bucket";
        new BucketGui(
                boostType,
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
                () -> new BucketBoostBuilderGui(
                        player,
                        boostType,
                        boost -> {
                            if (CobblemonBoosters.INSTANCE.activeSpawnBucketBoost == null) {
                                CobblemonBoosters.INSTANCE.activeSpawnBucketBoost = new SpawnBucketBoost(boost.getMultiplier(), boost.getDuration()).setBucket(boost.getBucket());
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeSpawnBucketBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        CobblemonBoosters.INSTANCE.config.discordWebhookConfig.spawnBucketEventStartEmbed,
                                        CobblemonBoosters.INSTANCE.activeSpawnBucketBoost
                                );
                                CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeSpawnBucketBoost.getBossBar());
                            } else {
                                SpawnBucketBoost newBoost = new SpawnBucketBoost(boost.getMultiplier(), boost.getDuration()).setBucket(boost.getBucket());
                                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts.add(newBoost);
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostAddedToQueued, newBoost);
                            }
                            CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openCatchGUI(ServerPlayer player) {
        String boostType = "Catch";
        new CatchGui(
                boostType,
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
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        CatchBoost.class,
                        boost -> {
                            if (CobblemonBoosters.INSTANCE.activeCatchBoost == null) {
                                CobblemonBoosters.INSTANCE.activeCatchBoost = new CatchBoost(boost.getMultiplier(), boost.getDuration());
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeCatchBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        CobblemonBoosters.INSTANCE.config.discordWebhookConfig.catchEventStartEmbed,
                                        CobblemonBoosters.INSTANCE.activeCatchBoost
                                );
                                CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeCatchBoost.getBossBar());
                            } else {
                                CatchBoost newBoost = new CatchBoost(boost.getMultiplier(), boost.getDuration());
                                CobblemonBoosters.INSTANCE.queuedCatchBoosts.add(newBoost);
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostAddedToQueued, newBoost);
                            }
                            CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openExperienceGUI(ServerPlayer player) {
        String boostType = "Experience";
        new ExperienceGui(
                boostType,
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
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        ExperienceBoost.class,
                        boost -> {
                            if (CobblemonBoosters.INSTANCE.activeExperienceBoost == null) {
                                CobblemonBoosters.INSTANCE.activeExperienceBoost = new ExperienceBoost(boost.getMultiplier(), boost.getDuration());
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeExperienceBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        CobblemonBoosters.INSTANCE.config.discordWebhookConfig.experienceEventStartEmbed,
                                        CobblemonBoosters.INSTANCE.activeExperienceBoost
                                );
                                CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeExperienceBoost.getBossBar());
                            } else {
                                ExperienceBoost newBoost = new ExperienceBoost(boost.getMultiplier(), boost.getDuration());
                                CobblemonBoosters.INSTANCE.queuedExperienceBoosts.add(newBoost);
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostAddedToQueued, newBoost);
                            }
                            CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
                        }
                ).open()
        ).open();
    }

    public static void openShinyGUI(ServerPlayer player) {
        String boostType = "Shiny";
        new ShinyGui(
                boostType,
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
                () -> new BoostBuilderGui(
                        player,
                        boostType,
                        ShinyBoost.class,
                        boost -> {
                            if (CobblemonBoosters.INSTANCE.activeShinyBoost == null) {
                                CobblemonBoosters.INSTANCE.activeShinyBoost = new ShinyBoost(boost.getMultiplier(), boost.getDuration());
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostStarted, CobblemonBoosters.INSTANCE.activeShinyBoost);
                                CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                                        CobblemonBoosters.INSTANCE.config.discordWebhookConfig.shinyEventStartEmbed,
                                        CobblemonBoosters.INSTANCE.activeShinyBoost
                                );
                                CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeShinyBoost.getBossBar());
                            } else {
                                ShinyBoost newBoost = new ShinyBoost(boost.getMultiplier(), boost.getDuration());
                                CobblemonBoosters.INSTANCE.queuedShinyBoosts.add(newBoost);
                                sendServerPlayerMessage(player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostAddedToQueued, newBoost);
                            }
                            CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
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
