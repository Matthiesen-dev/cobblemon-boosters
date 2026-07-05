package dev.matthiesen.cobblemon_boosters.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.config.MessagesConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.TextUtils;
import dev.matthiesen.common.matthiesen_lib_api.permission.Permission;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;

public final class Util {
    public static void handleStopCommand(CommandContext<CommandSourceStack> ctx, IBoost active, MessagesConfig.BoostMessagesConfig messagesConfig) {
        if (active == null) {
            sendMessage(ctx, messagesConfig.noActiveBoosts);
            return;
        }
        active.setTimeRemaining(1);
        sendMessage(ctx, messagesConfig.boostStopped, active);
    }

    public static void handleStatusCommand(CommandContext<CommandSourceStack> ctx, IBoost active, MessagesConfig.BoostMessagesConfig messagesConfig) {
        if (active != null) {
            sendMessage(ctx, messagesConfig.boostInfo, active);
        } else {
            sendMessage(ctx, messagesConfig.noActiveBoosts);
        }
    }

    public static void handleQueueClear(CommandContext<CommandSourceStack> ctx, Queue<? extends IBoost> queue, String clearedMessage) {
        queue.clear();
        sendMessage(ctx, clearedMessage);
    }

    public static void handleQueueResponse(CommandContext<CommandSourceStack> ctx, Queue<? extends IBoost> queue, MessagesConfig.BoostMessagesConfig messagesConfig) {
        if (queue.isEmpty()) {
            sendMessage(ctx, messagesConfig.noQueuedBoosts);
        } else {
            for (IBoost iBoost : queue) {
                sendMessage(ctx, messagesConfig.boostInfo, iBoost);
            }
        }
    }

    public static void handleSendMessage(CommandContext<CommandSourceStack> ctx, String parsedMessage) {
        ctx.getSource().sendSystemMessage(TextUtils.deserialize(parsedMessage));
    }

    public static void sendMessage(CommandContext<CommandSourceStack> ctx, String rawMessage) {
        String parsedMessage = TextUtils.parse(rawMessage);
        handleSendMessage(ctx, parsedMessage);
    }

    public static void sendMessage(CommandContext<CommandSourceStack> ctx, String rawMessage, @NotNull IBoost boost) {
        String parsedMessage = TextUtils.parse(rawMessage, boost);
        handleSendMessage(ctx, parsedMessage);
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
            Permission rootPermission,
            Command<CommandSourceStack> gui,
            Command<CommandSourceStack> startCommand,
            float maxMultiplier,
            Permission startPermission,
            Command<CommandSourceStack> stopCommand,
            Permission stopPermission,
            Command<CommandSourceStack> statusCommand,
            Permission statusPermission
    ) {
        return Commands.literal(rootCommandName)
                .requires(src -> PermissionRegistry.checkPermission(src, rootPermission))
                .executes(gui)
                .then(Commands.literal("start")
                        .requires(src -> PermissionRegistry.checkPermission(src, startPermission))
                        .then(Commands.argument("multiplier", FloatArgumentType.floatArg(1, maxMultiplier))
                                .then(newDurationAndUnitArgs(startCommand))
                        )
                )
                .then(Commands.literal("stop")
                        .requires(src -> PermissionRegistry.checkPermission(src, stopPermission))
                        .executes(stopCommand)
                )
                .then(Commands.literal("status")
                        .requires(src -> PermissionRegistry.checkPermission(src, statusPermission))
                        .executes(statusCommand)
                );
    }

    public static LiteralArgumentBuilder<CommandSourceStack> newBucketBoosterCommand(
            Permission rootPermission,
            Command<CommandSourceStack> gui,
            Command<CommandSourceStack> startCommand,
            Permission startPermission,
            Command<CommandSourceStack> stopCommand,
            Permission stopPermission,
            Command<CommandSourceStack> statusCommand,
            Permission statusPermission
    ) {
        return Commands.literal("bucket")
                .requires(src -> PermissionRegistry.checkPermission(src, rootPermission))
                .executes(gui)
                .then(Commands.literal("start")
                        .requires(src -> PermissionRegistry.checkPermission(src, startPermission))
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
                        .requires(src -> PermissionRegistry.checkPermission(src, stopPermission))
                        .executes(stopCommand)
                )
                .then(Commands.literal("status")
                        .requires(src -> PermissionRegistry.checkPermission(src, statusPermission))
                        .executes(statusCommand)
                );
    }
}
