package dev.matthiesen.cobblemon_boosters.common.services.controllers;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnBucketChosenEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.interfaces.CmdArgsParser;
import dev.matthiesen.cobblemon_boosters.common.interfaces.SupportedBoosterTypes;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.SpawnBucketBoost;
import dev.matthiesen.cobblemon_boosters.common.commands.BoostersCommand;
import dev.matthiesen.cobblemon_boosters.common.commands.Util;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.CacheServerConfig;
import dev.matthiesen.cobblemon_boosters.common.config.def.DiscordEmbed;
import dev.matthiesen.cobblemon_boosters.common.interfaces.Booster;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.services.BoostController;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.services.gui.BoosterGuiDefinition;
import dev.matthiesen.cobblemon_boosters.common.utils.GuiCmdHelpers;
import dev.matthiesen.cobblemon_boosters.common.utils.MenuUtils;
import dev.matthiesen.cobblemon_boosters.common.utils.SpawnBucketOverrideSelector;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedList;
import java.util.Queue;

public final class SpawnBucketBoostController implements Booster<SpawnBucketBoost> {
    public static final String BOOSTER_ID = "bucket";
    public static final SpawnBucketBoostController INSTANCE = new SpawnBucketBoostController();

    private volatile ObservableSubscription<SpawnBucketChosenEvent> subscription;

    private volatile SpawnBucketBoost activeBoost;
    private final Queue<SpawnBucketBoost> queue = new LinkedList<>();

    public static void register() {
        BoostController.registerBooster(INSTANCE);
        BoostController.registerGuiDefinition(getGuiDefinition());
        BoostersCommand.registerSubCommand(new SpawnBucketCMD());
        BoostersCommand.registerQueueResponseHandler(BOOSTER_ID, SpawnBucketBoostController::queueResponseHandler);
        BoostersCommand.registerQueueClearHandler(BOOSTER_ID, SpawnBucketBoostController::queueClearHandler);
    }

    public static BoosterGuiDefinition<SpawnBucketBoost> getGuiDefinition() {
        var permissions = PermissionRegistry.getPermissions();
        return new BoosterGuiDefinition<>(
                BOOSTER_ID,
                "Spawn Bucket",
                "&bSpawn Bucket Boosts&r",
                MenuUtils::getBucketItem,
                () -> INSTANCE,
                BoostersConfig::getSpawnBucketMessages,
                permissions.BUCKET_PERMISSION,
                permissions.BUCKET_START_PERMISSION,
                permissions.BUCKET_STOP_PERMISSION,
                permissions.BUCKET_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                BoosterGuiDefinition.BuilderType.SPAWN_BUCKET,
                SpawnBucketBoost.class,
                INSTANCE::appendToQueue
        );
    }

    public static void queueResponseHandler(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueResponse(ctx, BoostController.getSpawnBucketBoostManager().getBoostQueue(), BoostersConfig.getSpawnBucketMessages());
    }

    public static void queueClearHandler(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueClear(ctx, BoostController.getSpawnBucketBoostManager().getBoostQueue(), BoostersConfig.getSpawnBucketMessages().boostQueueCleared());
    }

    @Override
    public SupportedBoosterTypes getType() {
        return SupportedBoosterTypes.SPAWN_BUCKET;
    }

    @Override
    public void setupSubscriber() {
        subscription = CobblemonEvents.SPAWN_BUCKET_CHOSEN.subscribe(event -> {
            SpawnBucketBoost activeBoost = getActiveBoost();
            if (activeBoost == null) return;
            SpawnBucket newBucket = SpawnBucketOverrideSelector.recalculateOverrideBucket(event, activeBoost);
            event.setBucket(newBucket);
        });
    }

    @Override
    public void teardownSubscriber() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    @Override
    public SpawnBucketBoost getActiveBoost() {
        if (activeBoost == null) {
            // If there is no current active boost check the config to see if there is a default boost that should be active
            var defaultBoost = BoostersConfig.getActiveSpawnBucketBoost();
            if (defaultBoost != null) {
                setActiveBoost(defaultBoost);
            }
        }
        return activeBoost;
    }

    @Override
    public void setActiveBoost(SpawnBucketBoost boost) {
        this.activeBoost = boost;
    }

    @Override
    public Queue<SpawnBucketBoost> getBoostQueue() {
        if (queue.isEmpty()) {
            // If the queue is empty check the config to see if there is a default boost that should be queued
            var defaultBoost = BoostersConfig.getQueuedSpawnBucketBoosts();
            if (defaultBoost != null) {
                setBoostQueue(new LinkedList<>(defaultBoost));
            }
        }
        return queue;
    }

    @Override
    public void setBoostQueue(Queue<SpawnBucketBoost> boostQueue) {
        this.queue.clear();
        this.queue.addAll(boostQueue);
    }

    @Override
    public void internal_addToQueue(SpawnBucketBoost boost) {
        this.queue.add(boost);
    }

    @Override
    public DiscordEmbed getBoostStartEmbed() {
        return BoostersConfig.getSpawnBucketEventStartEmbed();
    }

    @Override
    public DiscordEmbed getBoostEndEmbed() {
        return BoostersConfig.getSpawnBucketEventEndEmbed();
    }

    // '/boosters bucket start <common/uncommon/rare/ultra-rare> <multiplier> <duration> <seconds/minutes/hours/days>'
    // '/boosters bucket stop'
    // '/boosters bucket status'
    public static final class SpawnBucketCMD implements ISubCommand {

        @Override
        public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
            var permissions = PermissionRegistry.getPermissions();
            return Util.newBucketBoosterCommand(
                    permissions.BUCKET_PERMISSION,
                    this::openGUI,
                    this::startCommand,
                    permissions.BUCKET_START_PERMISSION,
                    this::stopCommand,
                    permissions.BUCKET_STOP_PERMISSION,
                    this::statusCommand,
                    permissions.BUCKET_STATUS_PERMISSION
            );
        }

        public int openGUI(CommandContext<CommandSourceStack> ctx) {
            ServerPlayer player = ctx.getSource().getPlayer();
            if (player != null) {
                ServiceManager.getGuiAdapter().openBoosterGUI(player, BOOSTER_ID);
            }
            return 1;
        }

        public int startCommand(CommandContext<CommandSourceStack> ctx) {
            var context = CmdArgsParser.getBaseArgs(ctx);
            float multiplier = context.multiplier();
            int duration = context.duration();
            String unit = context.unit();
            String bucket = StringArgumentType.getString(ctx, BOOSTER_ID);
            int totalSeconds = GuiCmdHelpers.parseTotalSeconds(duration, unit);
            var manager = BoostController.getSpawnBucketBoostManager();
            var messages = BoostersConfig.getSpawnBucketMessages();
            SpawnBucketBoost boost = new SpawnBucketBoost(multiplier, totalSeconds).setBucket(bucket);
            manager.appendToQueue(boost);
            Util.sendMessage(ctx, messages.boostAddedToQueue(), boost);
            CacheServerConfig.setGlobalBoostData();
            return 1;
        }

        public int stopCommand(CommandContext<CommandSourceStack> ctx) {
            try {
                var messages = BoostersConfig.getSpawnBucketMessages();
                Util.handleStopCommand(ctx, BoostController.getSpawnBucketBoostManager().getActiveBoost(), messages);
            } catch (RuntimeException e) {
                CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to stop bucket boost", e);
            }
            return 1;
        }

        public int statusCommand(CommandContext<CommandSourceStack> ctx) {
            var messages = BoostersConfig.getSpawnBucketMessages();
            Util.handleStatusCommand(ctx, BoostController.getSpawnBucketBoostManager().getActiveBoost(), messages);
            return 1;
        }
    }
}
