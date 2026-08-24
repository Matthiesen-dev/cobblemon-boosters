package dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.commands.Util;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.cobblemon_boosters.common.interfaces.queue.QueuePriorityMode;
import dev.matthiesen.cobblemon_boosters.common.interfaces.queue.TimePriorityDirection;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class QueuePriority implements ISubCommand {
    public static final QueuePriority CMD = new QueuePriority();

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        var permissions = PermissionRegistry.getPermissions();
        return Commands.literal("queue-priority")
                .requires(src -> PermissionRegistry.checkPermission(src, permissions.QUEUE_PRIORITY_PERMISSION))
                .executes(this::status)
                .then(Commands.literal("enable")
                        .then(Commands.argument("enabled", BoolArgumentType.bool())
                                .executes(this::enable)
                        )
                )
                .then(Commands.literal("mode")
                        .then(Commands.argument("mode", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("fifo");
                                    builder.suggest("multiplier");
                                    builder.suggest("time");
                                    return builder.buildFuture();
                                })
                                .executes(this::mode)
                        )
                )
                .then(Commands.literal("preemption")
                        .then(Commands.argument("enabled", BoolArgumentType.bool())
                                .executes(this::preemption)
                        )
                )
                .then(Commands.literal("time-direction")
                        .then(Commands.argument("direction", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("shortest");
                                    builder.suggest("longest");
                                    return builder.buildFuture();
                                })
                                .executes(this::timeDirection)
                        )
                );
    }

    private int status(CommandContext<CommandSourceStack> ctx) {
        var config = BoostersConfig.CORE_SERVER_CONFIG;
        Util.sendMessage(ctx, String.format(
                config.messages_queuePriorityStatus.get(),
                config.queuePriorityEnabled.getAsBoolean(),
                config.queuePriorityMode.get(),
                config.timePriorityDirection.get(),
                config.activePreemptionEnabled.getAsBoolean()
        ));
        return 1;
    }

    private int enable(CommandContext<CommandSourceStack> ctx) {
        boolean enabled = BoolArgumentType.getBool(ctx, "enabled");

        BoostersConfig.CORE_SERVER_CONFIG.queuePriorityEnabled.set(enabled);
        BoostersConfig.CORE_SERVER_CONFIG.queuePriorityEnabled.save();

        var message = BoostersConfig.CORE_SERVER_CONFIG.messages_queuePriorityUpdated.get();
        Util.sendMessage(ctx, String.format(message, "enabled=" + enabled));
        return status(ctx);
    }

    private int mode(CommandContext<CommandSourceStack> ctx) {
        String value = StringArgumentType.getString(ctx, "mode");
        QueuePriorityMode normalized = switch (value.toLowerCase()) {
            case "fifo" -> QueuePriorityMode.FIFO;
            case "multiplier" -> QueuePriorityMode.MULTIPLIER;
            case "time" -> QueuePriorityMode.TIME_REMAINING;
            default -> null;
        };

        if (normalized == null) {
            Util.sendMessage(ctx, "%prefix% &cInvalid mode. Valid values: fifo, multiplier, time");
            return 0;
        }

        BoostersConfig.CORE_SERVER_CONFIG.queuePriorityMode.set(normalized);
        BoostersConfig.CORE_SERVER_CONFIG.queuePriorityMode.save();

        var message = BoostersConfig.CORE_SERVER_CONFIG.messages_queuePriorityUpdated.get();
        Util.sendMessage(ctx, String.format(message, "mode=" + normalized));
        return status(ctx);
    }

    private int preemption(CommandContext<CommandSourceStack> ctx) {
        boolean enabled = BoolArgumentType.getBool(ctx, "enabled");

        BoostersConfig.CORE_SERVER_CONFIG.activePreemptionEnabled.set(enabled);
        BoostersConfig.CORE_SERVER_CONFIG.activePreemptionEnabled.save();

        var message = BoostersConfig.CORE_SERVER_CONFIG.messages_queuePriorityUpdated.get();
        Util.sendMessage(ctx, String.format(message, "activePreemptionEnabled=" + enabled));
        return status(ctx);
    }

    private int timeDirection(CommandContext<CommandSourceStack> ctx) {
        String value = StringArgumentType.getString(ctx, "direction");
        TimePriorityDirection normalized = switch (value.toLowerCase()) {
            case "shortest" -> TimePriorityDirection.SHORTEST_FIRST;
            case "longest" -> TimePriorityDirection.LONGEST_FIRST;
            default -> null;
        };

        if (normalized == null) {
            Util.sendMessage(ctx, "%prefix% &cInvalid direction. Valid values: shortest, longest");
            return 0;
        }

        BoostersConfig.CORE_SERVER_CONFIG.timePriorityDirection.set(normalized);
        BoostersConfig.CORE_SERVER_CONFIG.timePriorityDirection.save();

        var message = BoostersConfig.CORE_SERVER_CONFIG.messages_queuePriorityUpdated.get();
        Util.sendMessage(ctx, String.format(message, "timePriorityDirection=" + normalized));
        return status(ctx);
    }
}
