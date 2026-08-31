package dev.matthiesen.cobblemon_boosters.common.services.controllers;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.interfaces.CmdArgsParser;
import dev.matthiesen.cobblemon_boosters.common.interfaces.SupportedBoosterTypes;
import dev.matthiesen.cobblemon_boosters.common.services.boosts.ShinyBoost;
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

public final class ShinyBoostController implements Booster<ShinyBoost> {
    public static final String BOOSTER_ID = "shiny";
    public static final ShinyBoostController INSTANCE = new ShinyBoostController();
    public static final ShinyBoostCMD INSTANCE_CMD = new ShinyBoostCMD();

    private volatile ObservableSubscription<ShinyChanceCalculationEvent> subscription;

    private volatile ShinyBoost activeBoost;
    private final Queue<ShinyBoost> queue = new LinkedList<>();

    public static void register() {
        CobblemonBoostersCommon.INSTANCE.createInfoLog("Registering Shiny Boost Controller");
        BoostController.registerBooster(INSTANCE);
        BoostController.registerGuiDefinition(getGuiDefinition());
        BoostersCommand.registerSubCommand(INSTANCE_CMD);
        BoostersCommand.registerQueueResponseHandler(BOOSTER_ID, ShinyBoostController::queueResponseHandler);
        BoostersCommand.registerQueueClearHandler(BOOSTER_ID, ShinyBoostController::queueClearHandler);
    }

    public static BoosterGuiDefinition<ShinyBoost> getGuiDefinition() {
        var permissions = PermissionRegistry.getPermissions();
        return new BoosterGuiDefinition<>(
                BOOSTER_ID,
                "Shiny",
                "&6Shiny Boosts&r",
                MenuUtils::getShinyItem,
                () -> INSTANCE,
                BoostersConfig::getShinyMessages,
                permissions.SHINY_PERMISSION,
                permissions.SHINY_START_PERMISSION,
                permissions.SHINY_STOP_PERMISSION,
                permissions.SHINY_STATUS_PERMISSION,
                permissions.CHECK_QUEUE_PERMISSION,
                BoosterGuiDefinition.BuilderType.MULTIPLIER,
                ShinyBoost.class,
                INSTANCE::appendToQueue
        );
    }

    public static void queueResponseHandler(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueResponse(ctx, BoostController.getShinyBoostManager().getBoostQueue(), BoostersConfig.getShinyMessages());
    }

    public static void queueClearHandler(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueClear(ctx, BoostController.getShinyBoostManager().getBoostQueue(), BoostersConfig.getShinyMessages().boostQueueCleared());
    }

    @Override
    public SupportedBoosterTypes getType() {
        return SupportedBoosterTypes.SHINY;
    }

    @Override
    public void setupSubscriber() {
        subscription = CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(event -> {
            ShinyBoost activeBoost = getActiveBoost();
            if (activeBoost == null) return;
            event.addModificationFunction(((rate, player, pokemon) ->
                    Math.max(rate / activeBoost.getMultiplier(), 1)));
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
    public ShinyBoost getActiveBoost() {
        if (activeBoost == null) {
            // If there is no current active boost check the config to see if there is a default boost that should be active
            var defaultBoost = BoostersConfig.getActiveShinyBoost();
            if (defaultBoost != null) {
                setActiveBoost(defaultBoost);
            }
        }
        return activeBoost;
    }

    @Override
    public void setActiveBoost(ShinyBoost boost) {
        this.activeBoost = boost;
    }

    @Override
    public Queue<ShinyBoost> getBoostQueue() {
        if (queue.isEmpty()) {
            // If the queue is empty check the config to see if there is a default boost that should be queued
            var defaultBoost = BoostersConfig.getQueuedShinyBoosts();
            if (defaultBoost != null) {
                setBoostQueue(new LinkedList<>(defaultBoost));
            }
        }
        return queue;
    }

    @Override
    public void setBoostQueue(Queue<ShinyBoost> boostQueue) {
        this.queue.clear();
        this.queue.addAll(boostQueue);
    }

    @Override
    public void internal_addToQueue(ShinyBoost boost) {
        this.queue.add(boost);
    }

    @Override
    public DiscordEmbed getBoostStartEmbed() {
        return BoostersConfig.getShinyEventStartEmbed();
    }

    @Override
    public DiscordEmbed getBoostEndEmbed() {
        return BoostersConfig.getShinyEventEndEmbed();
    }

    // '/boosters shiny start <multiplier> <duration> <seconds/minutes/hours/days>'
    // '/boosters shiny stop'
    // '/boosters shiny status'
    public static final class ShinyBoostCMD implements ISubCommand {
        @Override
        public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
            var permissions = PermissionRegistry.getPermissions();
            return Util.newBasicMultiplierBoosterCommand(
                    BOOSTER_ID,
                    permissions.SHINY_PERMISSION,
                    this::openGUI,
                    this::startCommand,
                    maxMultiplier(),
                    permissions.SHINY_START_PERMISSION,
                    this::stopCommand,
                    permissions.SHINY_STOP_PERMISSION,
                    this::statusCommand,
                    permissions.SHINY_STATUS_PERMISSION
            );
        }

        public static Float maxMultiplier() {
            return Cobblemon.config.getShinyRate();
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
            int totalSeconds = GuiCmdHelpers.parseTotalSeconds(duration, unit);
            var manager = BoostController.getShinyBoostManager();
            var messages = BoostersConfig.getShinyMessages();
            ShinyBoost boost = new ShinyBoost(multiplier, totalSeconds);
            manager.appendToQueue(boost);
            Util.sendMessage(ctx, messages.boostAddedToQueue(), boost);
            CacheServerConfig.setGlobalBoostData();
            return 1;
        }

        public int stopCommand(CommandContext<CommandSourceStack> ctx) {
            try {
                var messages = BoostersConfig.getShinyMessages();
                Util.handleStopCommand(ctx, BoostController.getShinyBoostManager().getActiveBoost(), messages);
            } catch (RuntimeException e) {
                CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to stop shiny boost", e);
            }
            return 1;
        }

        public int statusCommand(CommandContext<CommandSourceStack> ctx) {
            var messages = BoostersConfig.getShinyMessages();
            Util.handleStatusCommand(ctx, BoostController.getShinyBoostManager().getActiveBoost(), messages);
            return 1;
        }
    }
}
