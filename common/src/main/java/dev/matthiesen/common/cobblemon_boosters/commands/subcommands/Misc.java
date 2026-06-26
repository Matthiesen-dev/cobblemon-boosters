package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.config.CoreConfig;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class Misc {
    public static Reload ReloadCMD = new Reload();
    public static ClearQueues ClearQueuesCMD = new ClearQueues();
    public static CheckQueues CheckQueuesCMD = new CheckQueues();
    public static QueuePriority QueuePriorityCMD = new QueuePriority();

    public static List<ISubCommand> getSubCommands() {
        return List.of(
                ReloadCMD,
                ClearQueuesCMD,
                CheckQueuesCMD,
                QueuePriorityCMD
        );
    }

    public static class QueuePriority implements ISubCommand {
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

    public static class Reload implements ISubCommand {
        @Override
        public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
            var permissions = PermissionRegistry.getPermissions();
            return Commands.literal("reload")
                    .requires(src -> PermissionRegistry.checkPermission(
                            src,
                            permissions.RELOAD_PERMISSION)
                    )
                    .executes(this::cmd);
        }

        public int cmd(CommandContext<CommandSourceStack> ctx) {
            CobblemonBoosters.INSTANCE.reload(true);
            Util.sendMessage(ctx, CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.commandReload);
            return 1;
        }
    }

    public static class ClearQueues implements ISubCommand {
        @Override
        public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
            var permissions = PermissionRegistry.getPermissions();
            return Commands.literal("clear-queues")
                    .requires(src -> PermissionRegistry.checkPermission(
                            src,
                            permissions.CLEAR_QUEUES_PERMISSION
                    ))
                    .executes(this::command);
        }

        public int command(CommandContext<CommandSourceStack> ctx) {
            Util.handleQueueClear(
                    ctx,
                    BoostManager.getShinyBoostManager().getQueue(),
                    CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages.boostQueueCleared
            );
            Util.handleQueueClear(
                    ctx,
                    BoostManager.getCatchBoostManager().getQueue(),
                    CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.catchBoostMessages.boostQueueCleared
            );
            Util.handleQueueClear(
                    ctx,
                    BoostManager.getExperienceBoostManager().getQueue(),
                    CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages.boostQueueCleared
            );
            Util.handleQueueClear(
                    ctx,
                    BoostManager.getSpawnBucketBoostManager().getQueue(),
                    CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages.boostQueueCleared
            );
            CacheConfig.setGlobalBoostData();
            return 1;
        }
    }

    public static class CheckQueues implements ISubCommand {
        @Override
        public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
            var permissions = PermissionRegistry.getPermissions();
            return Commands.literal("check-queues")
                    .requires(src -> PermissionRegistry.checkPermission(
                            src,
                            permissions.CHECK_QUEUE_PERMISSION
                    ))
                    .executes(this::openGUI)
                    .then(Commands.argument("booster", StringArgumentType.string())
                            .requires(src -> PermissionRegistry.checkPermission(
                                    src,
                                    permissions.CHECK_QUEUE_PERMISSION
                            ))
                            .suggests((ctx, builder) -> {
                                for (String entry : Constants.CURRENT_BOOSTERS) {
                                    builder.suggest(entry);
                                }
                                return builder.buildFuture();
                            })
                            .executes(this::command)
                    );
        }

        public int openGUI(CommandContext<CommandSourceStack> ctx) {
            ServerPlayer player = ctx.getSource().getPlayer();
            if (player != null) {
                CobblemonBoosters.INSTANCE.guiAdapter.openQueuesGUI(player);
            }
            return 1;
        }

        public int command(CommandContext<CommandSourceStack> ctx) {
            String booster = StringArgumentType.getString(ctx, "booster").toLowerCase();
            switch (booster) {
                case "bucket" -> Util.handleQueueResponse(
                        ctx,
                        BoostManager.getSpawnBucketBoostManager().getQueue(),
                        CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages
                );
                case "catch" -> Util.handleQueueResponse(
                        ctx,
                        BoostManager.getCatchBoostManager().getQueue(),
                        CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.catchBoostMessages
                );
                case "experience" -> Util.handleQueueResponse(
                        ctx,
                        BoostManager.getExperienceBoostManager().getQueue(),
                        CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages
                );
                case "shiny" -> Util.handleQueueResponse(
                        ctx,
                        BoostManager.getShinyBoostManager().getQueue(),
                        CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages
                );
                default -> Util.sendMessage(ctx, "%prefix% &cUnknown booster type. Valid types are: " + String.join(", ", Constants.CURRENT_BOOSTERS) + ".");
            }
            return 1;
        }
    }
}
