package dev.matthiesen.common.cobblemon_boosters.commands;

import com.cobblemon.mod.common.Cobblemon;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ICommand;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        dispatcher.register(
                Commands.literal("boosters")
                        .then(
                                Commands.literal("reload").requires(src -> ModPermissions.checkPermission(
                                        src,
                                        CobblemonBoosters.INSTANCE.permissions.RELOAD_PERMISSION
                                )).executes(this::reload)
                        )
                        .then(
                                Commands.literal("catch")
                                        .requires(src -> ModPermissions.checkPermission(
                                                src,
                                                CobblemonBoosters.INSTANCE.permissions.CATCH_PERMISSION
                                        ))
                                        .then(
                                                Commands.literal("start")
                                                        .requires(src -> ModPermissions.checkPermission(
                                                                src,
                                                                CobblemonBoosters.INSTANCE.permissions.CATCH_START_PERMISSION
                                                        ))
                                                        .then(
                                                                Commands.argument("multiplier", FloatArgumentType.floatArg(1, Cobblemon.config.getShinyRate()))
                                                                        .then(
                                                                                Commands.argument("duration", IntegerArgumentType.integer(1))
                                                                                        .then(
                                                                                                Commands.argument("unit", StringArgumentType.string())
                                                                                                        .suggests((ctx, builder) -> {
                                                                                                            builder.suggest("seconds");
                                                                                                            builder.suggest("minutes");
                                                                                                            builder.suggest("hours");
                                                                                                            builder.suggest("days");
                                                                                                            return builder.buildFuture();
                                                                                                        })
                                                                                                        .executes(this::catchStartCommand)
                                                                                        )
                                                                        )
                                                        ))
                                        .then(
                                                Commands.literal("stop")
                                                        .requires(src -> ModPermissions.checkPermission(
                                                                src,
                                                                CobblemonBoosters.INSTANCE.permissions.CATCH_STOP_PERMISSION
                                                        ))
                                                        .executes(this::catchStopCommand)
                                        )
                                        .then(
                                                Commands.literal("status")
                                                        .requires(src -> ModPermissions.checkPermission(
                                                                src,
                                                                CobblemonBoosters.INSTANCE.permissions.CATCH_STATUS_PERMISSION
                                                        ))
                                                        .executes(this::catchStatusCommand)
                                        )
                        )
                        .then(
                                Commands.literal("shiny")
                                        .requires(src -> ModPermissions.checkPermission(
                                                src,
                                                CobblemonBoosters.INSTANCE.permissions.SHINY_PERMISSION
                                        ))
                                        .then(
                                                Commands.literal("start")
                                                        .requires(src -> ModPermissions.checkPermission(
                                                                src,
                                                                CobblemonBoosters.INSTANCE.permissions.SHINY_START_PERMISSION
                                                        ))
                                                        .then(
                                                                Commands.argument("multiplier", FloatArgumentType.floatArg(1, Cobblemon.config.getShinyRate()))
                                                                        .then(
                                                                                Commands.argument("duration", IntegerArgumentType.integer(1))
                                                                                        .then(
                                                                                                Commands.argument("unit", StringArgumentType.string())
                                                                                                        .suggests((ctx, builder) -> {
                                                                                                            builder.suggest("seconds");
                                                                                                            builder.suggest("minutes");
                                                                                                            builder.suggest("hours");
                                                                                                            builder.suggest("days");
                                                                                                            return builder.buildFuture();
                                                                                                        })
                                                                                                        .executes(this::shinyStartCommand)
                                                                                        )
                                                                        )
                                        ))
                                        .then(
                                                Commands.literal("stop")
                                                        .requires(src -> ModPermissions.checkPermission(
                                                                src,
                                                                CobblemonBoosters.INSTANCE.permissions.SHINY_STOP_PERMISSION
                                                        ))
                                                        .executes(this::shinyStopCommand)
                                        )
                                        .then(
                                                Commands.literal("status")
                                                        .requires(src -> ModPermissions.checkPermission(
                                                                src,
                                                                CobblemonBoosters.INSTANCE.permissions.SHINY_STATUS_PERMISSION
                                                        ))
                                                        .executes(this::shinyStatusCommand)
                                        )
                        )
                        .then(
                                Commands.literal("clear-queues")
                                        .requires(src -> ModPermissions.checkPermission(
                                                src,
                                                CobblemonBoosters.INSTANCE.permissions.CLEAR_QUEUES_PERMISSION
                                        ))
                                        .executes(this::clearQueuesCommand)
                        )
                        .then(
                                Commands.literal("check-queues")
                                        .requires(src -> ModPermissions.checkPermission(
                                                src,
                                                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION
                                        ))
                                        .executes(this::checkQueuesCommand)
                        )
        );
    }

    private void _sendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String parsedMessage) {
        if (player != null) {
            CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(TextUtils.deserialize(parsedMessage));
        } else {
            ctx.getSource().sendSystemMessage(TextUtils.deserializeMC(parsedMessage));
        }
    }

    private void sendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String rawMessage) {
        String parsedMessage = TextUtils.parse(rawMessage);
        _sendMessage(ctx, player, parsedMessage);
    }

    private void sendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String rawMessage, @NotNull IBoost boost) {
        String parsedMessage = TextUtils.parse(rawMessage, boost);
        _sendMessage(ctx, player, parsedMessage);
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

        int totalSeconds;
        switch (unit) {
            case "minutes" -> totalSeconds = duration * 60;
            case "hours" -> totalSeconds = duration * 3600;
            case "days" -> totalSeconds = duration * 86400;
            default -> totalSeconds = duration;
        }

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
        sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostStopped, CobblemonBoosters.INSTANCE.activeCatchBoost);
        CobblemonBoosters.INSTANCE.activeCatchBoost.timeRemaining = 1;
        return 1;
    }

    private int catchStatusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (CobblemonBoosters.INSTANCE.activeCatchBoost != null)
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostInfo, CobblemonBoosters.INSTANCE.activeCatchBoost);
        else
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noActiveBoosts);
        return 1;
    }

    private int shinyStartCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        ServerPlayer player = ctx.getSource().getPlayer();

        int totalSeconds;
        switch (unit) {
            case "minutes" -> totalSeconds = duration * 60;
            case "hours" -> totalSeconds = duration * 3600;
            case "days" -> totalSeconds = duration * 86400;
            default -> totalSeconds = duration;
        }

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
        sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostStopped, CobblemonBoosters.INSTANCE.activeShinyBoost);
        CobblemonBoosters.INSTANCE.activeShinyBoost.timeRemaining = 1;
        return 1;
    }

    private int shinyStatusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (CobblemonBoosters.INSTANCE.activeShinyBoost != null)
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostInfo, CobblemonBoosters.INSTANCE.activeShinyBoost);
        else
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.noActiveBoosts);
        return 1;
    }

    private int clearQueuesCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        CobblemonBoosters.INSTANCE.queuedShinyBoosts.clear();
        CobblemonBoosters.INSTANCE.queuedCatchBoosts.clear();
        sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostQueueCleared);
        sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostQueueCleared);
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
        return 1;
    }

    private int checkQueuesCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (CobblemonBoosters.INSTANCE.queuedShinyBoosts.isEmpty()) {
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.noQueuedBoosts);
        } else {
            for (ShinyBoost queuedBoost : CobblemonBoosters.INSTANCE.queuedShinyBoosts) {
                sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.shinyBoostInfo, queuedBoost);
            }
        }
        if (CobblemonBoosters.INSTANCE.queuedCatchBoosts.isEmpty()) {
            sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noQueuedBoosts);
        } else {
            for (CatchBoost queuedBoost : CobblemonBoosters.INSTANCE.queuedCatchBoosts) {
                sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.catchBoostInfo, queuedBoost);
            }
        }
        return 1;
    }
}