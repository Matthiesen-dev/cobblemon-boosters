package dev.matthiesen.cobblemon_boosters.common.services.controllers;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.CatchBoost;
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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedList;
import java.util.Queue;

public final class CatchBoostController implements Booster<CatchBoost> {
    public static final String BOOSTER_ID = "catch";
    public static final CatchBoostController INSTANCE = new CatchBoostController();

    private volatile ObservableSubscription<PokemonCatchRateEvent> subscription;

    private volatile CatchBoost activeBoost;
    private final Queue<CatchBoost> queue = new LinkedList<>();

    public static void register() {
        BoostController.registerBooster(INSTANCE);
        BoostController.registerGuiDefinition(getGuiDefinition());
        BoostersCommand.registerSubCommand(new CatchBoostCMD());
        BoostersCommand.registerQueueResponseHandler(BOOSTER_ID, CatchBoostController::queueResponseHandler);
        BoostersCommand.registerQueueClearHandler(BOOSTER_ID, CatchBoostController::queueClearHandler);
    }

    public static BoosterGuiDefinition<CatchBoost> getGuiDefinition() {
        var permissions = PermissionRegistry.getPermissions();
        return new BoosterGuiDefinition<>(
                BOOSTER_ID,
                "Catch",
                "&dCatch Boosts&r",
                MenuUtils::getCatchItem,
                () -> INSTANCE,
                BoostersConfig::getCatchMessages,
                permissions.CATCH_PERMISSION,
                permissions.CATCH_START_PERMISSION,
                permissions.CATCH_STOP_PERMISSION,
                permissions.CATCH_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                BoosterGuiDefinition.BuilderType.MULTIPLIER,
                CatchBoost.class,
                INSTANCE::appendToQueue
        );
    }

    public static void queueResponseHandler(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueResponse(ctx, BoostController.getCatchBoostManager().getBoostQueue(), BoostersConfig.getCatchMessages());
    }

    public static void queueClearHandler(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueClear(ctx, BoostController.getCatchBoostManager().getBoostQueue(), BoostersConfig.getCatchMessages().boostQueueCleared());
    }

    @Override
    public Constants.SupportedBoosterTypes getType() {
        return Constants.SupportedBoosterTypes.CATCH;
    }

    @Override
    public void setupSubscriber() {
        subscription = CobblemonEvents.POKEMON_CATCH_RATE.subscribe(event -> {
            CatchBoost activeBoost = getActiveBoost();
            if (activeBoost == null) return;
            float baseCatchRate = event.getCatchRate();
            event.setCatchRate(Math.min(baseCatchRate * activeBoost.getMultiplier(), 255F));
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
    public CatchBoost getActiveBoost() {
        if (activeBoost == null) {
            // If there is no current active boost check the config to see if there is a default boost that should be active
            var defaultBoost = BoostersConfig.getActiveCatchBoost();
            if (defaultBoost != null) {
                setActiveBoost(defaultBoost);
            }
        }
        return activeBoost;
    }

    @Override
    public void setActiveBoost(CatchBoost boost) {
        this.activeBoost = boost;
    }

    @Override
    public Queue<CatchBoost> getBoostQueue() {
        if (queue.isEmpty()) {
            // If the queue is empty check the config to see if there is a default boost that should be queued
            var defaultBoost = BoostersConfig.getQueuedCatchBoosts();
            if (defaultBoost != null) {
                setBoostQueue(new LinkedList<>(defaultBoost));
            }
        }
        return queue;
    }

    @Override
    public void setBoostQueue(Queue<CatchBoost> boostQueue) {
        this.queue.clear();
        this.queue.addAll(boostQueue);
    }

    @Override
    public void internal_addToQueue(CatchBoost boost) {
        this.queue.add(boost);
    }

    @Override
    public DiscordEmbed getBoostStartEmbed() {
        return BoostersConfig.getCatchEventStartEmbed();
    }

    @Override
    public DiscordEmbed getBoostEndEmbed() {
        return BoostersConfig.getCatchEventEndEmbed();
    }


    // '/boosters catch start <multiplier> <duration> <seconds/minutes/hours/days>'
    // '/boosters catch stop'
    // '/boosters catch status'
    public static final class CatchBoostCMD implements ISubCommand {
        @Override
        public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
            var permissions = PermissionRegistry.getPermissions();
            return Util.newBasicMultiplierBoosterCommand(
                    BOOSTER_ID,
                    permissions.CATCH_PERMISSION,
                    this::openGUI,
                    this::startCommand,
                    maxMultiplier,
                    permissions.CATCH_START_PERMISSION,
                    this::stopCommand,
                    permissions.CATCH_STOP_PERMISSION,
                    this::statusCommand,
                    permissions.CATCH_STATUS_PERMISSION
            );
        }

        public static final Float maxMultiplier = 100F;

        public int openGUI(CommandContext<CommandSourceStack> ctx) {
            ServerPlayer player = ctx.getSource().getPlayer();
            if (player != null) {
                ServiceManager.getGuiAdapter().openBoosterGUI(player, BOOSTER_ID);
            }
            return 1;
        }

        public int startCommand(CommandContext<CommandSourceStack> ctx) {
            float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
            int duration = IntegerArgumentType.getInteger(ctx, "duration");
            String unit = StringArgumentType.getString(ctx, "unit");
            int totalSeconds = GuiCmdHelpers.parseTotalSeconds(duration, unit);
            var manager = BoostController.getCatchBoostManager();
            var messages = BoostersConfig.getCatchMessages();
            CatchBoost boost = new CatchBoost(multiplier, totalSeconds);
            manager.appendToQueue(boost);
            Util.sendMessage(ctx, messages.boostAddedToQueue(), boost);
            CacheServerConfig.setGlobalBoostData();
            return 1;
        }

        public int stopCommand(CommandContext<CommandSourceStack> ctx) {
            try {
                var messages = BoostersConfig.getCatchMessages();
                Util.handleStopCommand(ctx, BoostController.getCatchBoostManager().getActiveBoost(), messages);
            } catch (RuntimeException e) {
                CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to stop catch boost", e);
            }
            return 1;
        }

        public int statusCommand(CommandContext<CommandSourceStack> ctx) {
            var messages = BoostersConfig.getCatchMessages();
            Util.handleStatusCommand(ctx, BoostController.getCatchBoostManager().getActiveBoost(), messages);
            return 1;
        }

    }
}
