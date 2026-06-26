package dev.matthiesen.common.cobblemon_boosters.commands.subcommands.misc;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.config.CoreConfig;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class QueuePriority implements ISubCommand {
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
        CoreConfig config = CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages;
        Util.sendMessage(ctx, String.format(
                messages.queuePriorityStatus,
                config.queuePriorityEnabled,
                config.queuePriorityMode,
                config.timePriorityDirection,
                config.activePreemptionEnabled
        ));
        return 1;
    }

    private int enable(CommandContext<CommandSourceStack> ctx) {
        boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
        CoreConfig config = CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig();
        config.queuePriorityEnabled = enabled;
        persistAndApplyChanges();

        var message = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.queuePriorityUpdated;
        Util.sendMessage(ctx, String.format(message, "enabled=" + enabled));
        return status(ctx);
    }

    private int mode(CommandContext<CommandSourceStack> ctx) {
        String value = StringArgumentType.getString(ctx, "mode");
        String normalized = switch (value.toLowerCase()) {
            case "fifo" -> BoostManager.QueuePriorityMode.FIFO.name();
            case "multiplier" -> BoostManager.QueuePriorityMode.MULTIPLIER.name();
            case "time" -> BoostManager.QueuePriorityMode.TIME_REMAINING.name();
            default -> null;
        };

        if (normalized == null) {
            Util.sendMessage(ctx, "%prefix% &cInvalid mode. Valid values: fifo, multiplier, time");
            return 0;
        }

        CoreConfig config = CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig();
        config.queuePriorityMode = normalized;
        persistAndApplyChanges();

        var message = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.queuePriorityUpdated;
        Util.sendMessage(ctx, String.format(message, "mode=" + normalized));
        return status(ctx);
    }

    private int preemption(CommandContext<CommandSourceStack> ctx) {
        boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
        CoreConfig config = CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig();
        config.activePreemptionEnabled = enabled;
        persistAndApplyChanges();

        var message = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.queuePriorityUpdated;
        Util.sendMessage(ctx, String.format(message, "activePreemptionEnabled=" + enabled));
        return status(ctx);
    }

    private int timeDirection(CommandContext<CommandSourceStack> ctx) {
        String value = StringArgumentType.getString(ctx, "direction");
        String normalized = switch (value.toLowerCase()) {
            case "shortest" -> BoostManager.TimePriorityDirection.SHORTEST_FIRST.name();
            case "longest" -> BoostManager.TimePriorityDirection.LONGEST_FIRST.name();
            default -> null;
        };

        if (normalized == null) {
            Util.sendMessage(ctx, "%prefix% &cInvalid direction. Valid values: shortest, longest");
            return 0;
        }

        CoreConfig config = CobblemonBoosters.INSTANCE.getCoreConfigManager().getConfig();
        config.timePriorityDirection = normalized;
        persistAndApplyChanges();

        var message = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.queuePriorityUpdated;
        Util.sendMessage(ctx, String.format(message, "timePriorityDirection=" + normalized));
        return status(ctx);
    }

    private void persistAndApplyChanges() {
        CobblemonBoosters.INSTANCE.getCoreConfigManager().saveConfig();
        BoostManager.reapplyQueuePriorities();
        CacheConfig.setGlobalBoostData();
    }
}
