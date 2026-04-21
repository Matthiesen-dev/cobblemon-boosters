package dev.matthiesen.common.cobblemon_boosters.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermission;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;

public class Util {
    public static void handleStopCommand(CommandContext<CommandSourceStack> ctx, ServerPlayer player, IBoost active, String message) {
        active.setTimeRemaining(1);
        sendMessage(ctx, player, message, active);
    }

    public static void handleStatusCommand(CommandContext<CommandSourceStack> ctx, ServerPlayer player, IBoost active, String activeMessage, String noActiveMessage) {
        if (active != null) {
            sendMessage(ctx, player, activeMessage, active);
        } else {
            sendMessage(ctx, player, noActiveMessage);
        }
    }

    public static void handleQueueClear(CommandContext<CommandSourceStack> ctx, ServerPlayer player, Queue<? extends IBoost> queue, String clearedMessage) {
        queue.clear();
        sendMessage(ctx, player, clearedMessage);
    }

    public static void handleQueueResponse(CommandContext<CommandSourceStack> ctx, ServerPlayer player, Queue<? extends IBoost> queue, String noQueue, String withQueue) {
        if (queue.isEmpty()) {
            sendMessage(ctx, player, noQueue);
        } else {
            for (IBoost iBoost : queue) {
                sendMessage(ctx, player, withQueue, iBoost);
            }
        }
    }

    public static void handleSendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String parsedMessage) {
        if (player != null) {
            CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(TextUtils.deserialize(parsedMessage));
        } else {
            ctx.getSource().sendSystemMessage(TextUtils.deserializeMC(parsedMessage));
        }
    }

    public static void sendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String rawMessage) {
        String parsedMessage = TextUtils.parse(rawMessage);
        handleSendMessage(ctx, player, parsedMessage);
    }

    public static void sendMessage(CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, String rawMessage, @NotNull IBoost boost) {
        String parsedMessage = TextUtils.parse(rawMessage, boost);
        handleSendMessage(ctx, player, parsedMessage);
    }

    public static int parseTotalSeconds(int duration, String unit) {
        int totalSeconds;
        switch (unit) {
            case "minutes" -> totalSeconds = duration * 60;
            case "hours" -> totalSeconds = duration * 3600;
            case "days" -> totalSeconds = duration * 86400;
            default -> totalSeconds = duration;
        }
        return totalSeconds;
    }

    public static RequiredArgumentBuilder<CommandSourceStack, Integer> newDurationAndUnitArgs(
            Command<CommandSourceStack> executes
    ) {
        return Commands.argument("duration", IntegerArgumentType.integer(1))
                .then(Commands.argument("unit", StringArgumentType.string())
                        .suggests((ctx, builder) -> {
                            builder.suggest("seconds");
                            builder.suggest("minutes");
                            builder.suggest("hours");
                            builder.suggest("days");
                            return builder.buildFuture();
                        })
                        .executes(executes)
                );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> newBasicMultiplierBoosterCommand(
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
                                .then(newDurationAndUnitArgs(startCommand))
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

    public static LiteralArgumentBuilder<CommandSourceStack> newBucketBoosterCommand(
            ModPermission rootPermission,
            Command<CommandSourceStack> startCommand,
            ModPermission startPermission,
            Command<CommandSourceStack> stopCommand,
            ModPermission stopPermission,
            Command<CommandSourceStack> statusCommand,
            ModPermission statusPermission
    ) {
        return Commands.literal("bucket")
                .requires(src -> ModPermissions.checkPermission(src, rootPermission))
                .then(Commands.literal("start")
                        .requires(src -> ModPermissions.checkPermission(src, startPermission))
                        .then(Commands.argument("bucket", StringArgumentType.string())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("common");
                                    builder.suggest("uncommon");
                                    builder.suggest("rare");
                                    builder.suggest("ultra-rare");
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("multiplier", FloatArgumentType.floatArg(1, (float) 100.0))
                                        .then(newDurationAndUnitArgs(startCommand))
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
}
