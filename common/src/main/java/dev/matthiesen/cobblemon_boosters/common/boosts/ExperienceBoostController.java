package dev.matthiesen.cobblemon_boosters.common.boosts;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.Constants;
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
import dev.matthiesen.cobblemon_boosters.common.utils.GuiCmdHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedList;
import java.util.Queue;

public final class ExperienceBoostController implements Booster<ExperienceBoost> {
    public static final ExperienceBoostController INSTANCE = new ExperienceBoostController();

    private volatile ObservableSubscription<ExperienceGainedEvent.Pre> subscription;

    private volatile ExperienceBoost activeBoost;
    private final Queue<ExperienceBoost> queue = new LinkedList<>();

    public static void register() {
        BoostController.registerBooster(INSTANCE);
        BoostersCommand.registerSubCommand(new ExperienceBoostCMD());
        BoostersCommand.registerQueueResponseHandler("experience", ExperienceBoostController::queueResponseHandler);
        BoostersCommand.registerQueueClearHandler("experience", ExperienceBoostController::queueClearHandler);
    }

    public static void queueResponseHandler(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueResponse(ctx, BoostController.getExperienceBoostManager().getBoostQueue(), BoostersConfig.getExperienceMessages());
    }

    public static void queueClearHandler(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueClear(ctx, BoostController.getExperienceBoostManager().getBoostQueue(), BoostersConfig.getExperienceMessages().boostQueueCleared());
    }

    @Override
    public Constants.SupportedBoosterTypes getType() {
        return Constants.SupportedBoosterTypes.EXPERIENCE;
    }

    @Override
    public void setupSubscriber() {
        subscription = CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe(event -> {
            ExperienceBoost activeBoost = getActiveBoost();
            if (activeBoost == null) return;
            int exp = event.getExperience();
            event.setExperience(Math.round(exp * activeBoost.getMultiplier()));
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
    public ExperienceBoost getActiveBoost() {
        if (activeBoost == null) {
            // If there is no current active boost check the config to see if there is a default boost that should be active
            var defaultBoost = BoostersConfig.getActiveExperienceBoost();
            if (defaultBoost != null) {
                setActiveBoost(defaultBoost);
            }
        }
        return activeBoost;
    }

    @Override
    public void setActiveBoost(ExperienceBoost boost) {
        this.activeBoost = boost;
    }

    @Override
    public Queue<ExperienceBoost> getBoostQueue() {
        if (queue.isEmpty()) {
            // If the queue is empty check the config to see if there is a default boost that should be queued
            var defaultBoost = BoostersConfig.getQueuedExperienceBoosts();
            if (defaultBoost != null) {
                setBoostQueue(new LinkedList<>(defaultBoost));
            }
        }
        return queue;
    }

    @Override
    public void setBoostQueue(Queue<ExperienceBoost> boostQueue) {
        this.queue.clear();
        this.queue.addAll(boostQueue);
    }

    @Override
    public void clearBoostQueue() {
        this.queue.clear();
    }

    @Override
    public void internal_addToQueue(ExperienceBoost boost) {
        this.queue.add(boost);
    }

    @Override
    public DiscordEmbed getBoostStartEmbed() {
        return BoostersConfig.getExperienceEventStartEmbed();
    }

    @Override
    public DiscordEmbed getBoostEndEmbed() {
        return BoostersConfig.getExperienceEventEndEmbed();
    }

    // '/boosters experience start <multiplier> <duration> <seconds/minutes/hours/days>'
    // '/boosters experience stop'
    // '/boosters experience status'
    public static final class ExperienceBoostCMD implements ISubCommand {
        @Override
        public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
            var permissions = PermissionRegistry.getPermissions();
            return Util.newBasicMultiplierBoosterCommand(
                    "experience",
                    permissions.EXPERIENCE_PERMISSION,
                    this::openGUI,
                    this::startCommand,
                    maxMultiplier,
                    permissions.EXPERIENCE_START_PERMISSION,
                    this::stopCommand,
                    permissions.EXPERIENCE_STOP_PERMISSION,
                    this::statusCommand,
                    permissions.EXPERIENCE_STATUS_PERMISSION
            );
        }

        public static final Float maxMultiplier = 100F;

        public int openGUI(CommandContext<CommandSourceStack> ctx) {
            ServerPlayer player = ctx.getSource().getPlayer();
            if (player != null) {
                ServiceManager.getGuiAdapter().openExperienceBoosterGUI(player);
            }
            return 1;
        }

        public int startCommand(CommandContext<CommandSourceStack> ctx) {
            float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
            int duration = IntegerArgumentType.getInteger(ctx, "duration");
            String unit = StringArgumentType.getString(ctx, "unit");
            int totalSeconds = GuiCmdHelpers.parseTotalSeconds(duration, unit);
            var manager = BoostController.getExperienceBoostManager();
            var messages = BoostersConfig.getExperienceMessages();
            ExperienceBoost boost = new ExperienceBoost(multiplier, totalSeconds);
            manager.appendToQueue(boost);
            Util.sendMessage(ctx, messages.boostAddedToQueue(), boost);
            CacheServerConfig.setGlobalBoostData();
            return 1;
        }

        public int stopCommand(CommandContext<CommandSourceStack> ctx) {
            try {
                var messages = BoostersConfig.getExperienceMessages();
                Util.handleStopCommand(ctx, BoostController.getExperienceBoostManager().getActiveBoost(), messages);
            } catch (RuntimeException e) {
                CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to stop experience boost", e);
            }
            return 1;
        }

        public int statusCommand(CommandContext<CommandSourceStack> ctx) {
            var messages = BoostersConfig.getExperienceMessages();
            Util.handleStatusCommand(ctx, BoostController.getExperienceBoostManager().getActiveBoost(), messages);
            return 1;
        }
    }
}
