package dev.matthiesen.common.cobblemon_boosters.commands;

import com.cobblemon.mod.common.Cobblemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ICommand;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermission;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;

public class BoostersCommand implements ICommand {
    public BoostersCommand() {}

    // '/boosters reload' - Reload config
    // '/boosters catch start <multiplier> <duration> <seconds/minutes/hours/days>'
    // '/boosters catch stop'
    // '/boosters catch status'
    // '/boosters shiny start <multiplier> <duration> <seconds/minutes/hours/days>'
    // '/boosters shiny stop'
    // '/boosters shiny status'
    // '/boosters clear-queues'
    // '/boosters check-queues'

    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        dispatcher.register(Commands.literal("boosters")
                .then(Commands.literal("reload")
                        .requires(src -> ModPermissions.checkPermission(
                            src,
                            CobblemonBoosters.INSTANCE.permissions.RELOAD_PERMISSION)
                        )
                        .executes(this::reload)
                )
                .then(newBoosterCommand(
                        "catch",
                        CobblemonBoosters.INSTANCE.permissions.CATCH_PERMISSION,
                        this::catchStartCommand,
                        100F,
                        CobblemonBoosters.INSTANCE.permissions.CATCH_START_PERMISSION,
                        this::catchStopCommand,
                        CobblemonBoosters.INSTANCE.permissions.CATCH_STOP_PERMISSION,
                        this::catchStatusCommand,
                        CobblemonBoosters.INSTANCE.permissions.CATCH_STATUS_PERMISSION
                ))
                .then(newBoosterCommand(
                        "shiny",
                        CobblemonBoosters.INSTANCE.permissions.SHINY_PERMISSION,
                        this::shinyStartCommand,
                        Cobblemon.config.getShinyRate(),
                        CobblemonBoosters.INSTANCE.permissions.SHINY_START_PERMISSION,
                        this::shinyStopCommand,
                        CobblemonBoosters.INSTANCE.permissions.SHINY_STOP_PERMISSION,
                        this::shinyStatusCommand,
                        CobblemonBoosters.INSTANCE.permissions.SHINY_STATUS_PERMISSION
                ))
                .then(Commands.literal("clear-queues")
                        .requires(src -> ModPermissions.checkPermission(
                                 src,
                                 CobblemonBoosters.INSTANCE.permissions.CLEAR_QUEUES_PERMISSION
                        ))
                        .executes(this::clearQueuesCommand)
                )
                .then(Commands.literal("check-queues")
                        .requires(src -> ModPermissions.checkPermission(
                                src,
                                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION
                        ))
                        .executes(this::checkQueuesCommand)
                )
        );
    }

    private LiteralArgumentBuilder<CommandSourceStack> newBoosterCommand(
            String rootCommandName,
            ModPermission rootPermission,
            Command<CommandSourceStack> startCommand,
            float maxMultiplier,
            ModPermission startPermission,
            Command<CommandSourceStack> stopCommand,
            ModPermission stopPermission,
            Command<CommandSourceStack> statusCommand,
            ModPermission statusPermission
    ) {
        return Commands.literal(rootCommandName)
                .requires(src -> ModPermissions.checkPermission(src, rootPermission))
                .then(Commands.literal("start")
                        .requires(src -> ModPermissions.checkPermission(src, startPermission))
                        .then(Commands.argument("multiplier", FloatArgumentType.floatArg(1, maxMultiplier))
                               .then(Commands.argument("duration", IntegerArgumentType.integer(1))
                                      .then(Commands.argument("unit", StringArgumentType.string())
                                            .suggests((ctx, builder) -> {
                                                builder.suggest("seconds");
                                                builder.suggest("minutes");
                                                builder.suggest("hours");
                                                builder.suggest("days");
                                                return builder.buildFuture();
                                            })
                                            .executes(startCommand)
                                      )
                               )
                        )
                )
                .then(Commands.literal("stop")
                        .requires(src -> ModPermissions.checkPermission(src, stopPermission))
                        .executes(stopCommand)
                )
                .then(Commands.literal("status")
                        .requires(src -> ModPermissions.checkPermission(src, statusPermission))
                        .executes(statusCommand)
                );
    }

    private int parseTotalSeconds(int duration, String unit) {
        int totalSeconds;
        switch (unit) {
            case "minutes" -> totalSeconds = duration * 60;
            case "hours" -> totalSeconds = duration * 3600;
            case "days" -> totalSeconds = duration * 86400;
            default -> totalSeconds = duration;
        }
        return totalSeconds;
    }

    private void handleStopCommand(CommandContext<CommandSourceStack> ctx, ServerPlayer player, IBoost active, String message) {
        active.setTimeRemaining(1);
        sendMessage(ctx, player, message, active);
    }

    private void handleStatusCommand(CommandContext<CommandSourceStack> ctx, ServerPlayer player, IBoost active, String activeMessage, String noActiveMessage) {
        if (active != null) {
            sendMessage(ctx, player, activeMessage, active);
        } else {
            sendMessage(ctx, player, noActiveMessage);
        }
    }

    private void handleQueueClear(CommandContext<CommandSourceStack> ctx, ServerPlayer player, Queue<? extends IBoost> queue, String clearedMessage) {
        queue.clear();
        sendMessage(ctx, player, clearedMessage);
    }

    private void handleQueueResponse(CommandContext<CommandSourceStack> ctx, ServerPlayer player, Queue<? extends IBoost> queue, String noQueue, String withQueue) {
        if (queue.isEmpty()) {
            sendMessage(ctx, player, noQueue);
        } else {
            for (IBoost iBoost : queue) {
                sendMessage(ctx, player, withQueue, iBoost);
            }
        }
    }

    private void handleSendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String parsedMessage) {
        if (player != null) {
            CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(TextUtils.deserialize(parsedMessage));
        } else {
            ctx.getSource().sendSystemMessage(TextUtils.deserializeMC(parsedMessage));
        }
    }

    private void sendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String rawMessage) {
        String parsedMessage = TextUtils.parse(rawMessage);
        handleSendMessage(ctx, player, parsedMessage);
    }

    private void sendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String rawMessage, @NotNull IBoost boost) {
        String parsedMessage = TextUtils.parse(rawMessage, boost);
        handleSendMessage(ctx, player, parsedMessage);
    }

    private int reload(CommandContext<CommandSourceStack> ctx) {
        CobblemonBoosters.INSTANCE.reload(true);
        ServerPlayer player = ctx.getSource().getPlayer();
        sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.commandReload);
        return 1;
    }

    private int catchStartCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        ServerPlayer player = ctx.getSource().getPlayer();
        int totalSeconds = parseTotalSeconds(duration, unit);

        if (CobblemonBoosters.INSTANCE.activeCatchBoost == null) {
            CobblemonBoosters.INSTANCE.activeCatchBoost = new CatchBoost(multiplier, totalSeconds);
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostStarted, CobblemonBoosters.INSTANCE.activeCatchBoost);
            CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeCatchBoost.getBossBar());
        } else {
            CatchBoost boost = new CatchBoost(multiplier, totalSeconds);
            CobblemonBoosters.INSTANCE.queuedCatchBoosts.add(boost);
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostAddedToQueued, boost);
        }
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
        return 1;
    }

    private int catchStopCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        handleStopCommand(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostStopped
        );
        return 1;
    }

    private int catchStatusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        handleStatusCommand(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostInfo,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noActiveBoosts
        );
        return 1;
    }

    private int shinyStartCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        ServerPlayer player = ctx.getSource().getPlayer();
        int totalSeconds = parseTotalSeconds(duration, unit);

        if (CobblemonBoosters.INSTANCE.activeShinyBoost == null) {
            CobblemonBoosters.INSTANCE.activeShinyBoost = new ShinyBoost(multiplier, totalSeconds);
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostStarted, CobblemonBoosters.INSTANCE.activeShinyBoost);
            CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeShinyBoost.getBossBar());
        } else {
            ShinyBoost boost = new ShinyBoost(multiplier, totalSeconds);
            CobblemonBoosters.INSTANCE.queuedShinyBoosts.add(boost);
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostAddedToQueued, boost);
        }
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
        return 1;
    }

    private int shinyStopCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        handleStopCommand(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.activeShinyBoost,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostStopped
        );
        return 1;
    }

    private int shinyStatusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        handleStatusCommand(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostInfo,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noActiveBoosts
        );
        return 1;
    }

    private int clearQueuesCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        handleQueueClear(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostQueueCleared
        );
        handleQueueClear(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostQueueCleared
        );
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
        return 1;
    }

    private int checkQueuesCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        handleQueueResponse(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.noQueuedBoosts,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostInfo
        );
        handleQueueResponse(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noQueuedBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostInfo
        );
        return 1;
    }
}