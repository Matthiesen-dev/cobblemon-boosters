package dev.matthiesen.common.cobblemon_boosters.commands;

import com.cobblemon.mod.common.Cobblemon;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ICommand;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.TextUtils;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class BoostersCommand implements ICommand {
    public BoostersCommand() {}

    // '/boosters reload' - Reload config
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
                                        )
                                                .then(
                                                        Commands.literal("stop")
                                                                .requires(src -> ModPermissions.checkPermission(
                                                                        src,
                                                                        CobblemonBoosters.INSTANCE.permissions.SHINY_STOP_PERMISSION
                                                                ))
                                                                .executes(this::shinyStopCommand)
                                                        )
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

    private int reload(CommandContext<CommandSourceStack> ctx) {
        CobblemonBoosters.INSTANCE.reload(true);
        ServerPlayer player = ctx.getSource().getPlayer();
        assert player != null;
        CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.commandReload))
        );
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

        if (CobblemonBoosters.INSTANCE.globalBoost == null) {
            CobblemonBoosters.INSTANCE.globalBoost = new ShinyBoost(multiplier, totalSeconds);
            if (player != null) {
                CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostStarted, CobblemonBoosters.INSTANCE.globalBoost))
                );
            } else {
                CobblemonBoosters.INSTANCE.getAdventure().console().sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostStarted, CobblemonBoosters.INSTANCE.globalBoost))
                );
            }
            CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.globalBoost.bossBar);
        } else {
            ShinyBoost boost = new ShinyBoost(multiplier, totalSeconds);
            CobblemonBoosters.INSTANCE.queuedShinyBoosts.add(boost);
            if (player != null) {
                CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostAddedToQueued, boost))
                );
            } else {
                CobblemonBoosters.INSTANCE.getAdventure().console().sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostAddedToQueued, boost))
                );
            }
        }
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();

        return 1;
    }

    private int shinyStopCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                    TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostStopped, CobblemonBoosters.INSTANCE.globalBoost))
            );
        } else {
            CobblemonBoosters.INSTANCE.getAdventure().console().sendMessage(
                    TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostStopped, CobblemonBoosters.INSTANCE.globalBoost))
            );
        }
        CobblemonBoosters.INSTANCE.globalBoost.timeRemaining = 1;
        return 1;
    }

    private int shinyStatusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (CobblemonBoosters.INSTANCE.globalBoost != null)
            if (player != null) {
                CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostInfo, CobblemonBoosters.INSTANCE.globalBoost))
                );
            } else {
                CobblemonBoosters.INSTANCE.getAdventure().console().sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostInfo, CobblemonBoosters.INSTANCE.globalBoost))
                );
            }
        else
            if (player != null) {
                CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.noActiveBoosts))
                );
            } else {
                CobblemonBoosters.INSTANCE.getAdventure().console().sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.noActiveBoosts))
                );
            }
        return 1;
    }

    private int clearQueuesCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        CobblemonBoosters.INSTANCE.queuedShinyBoosts.clear();
        if (player != null) {
            CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                    TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostQueueCleared))
            );
        } else {
            CobblemonBoosters.INSTANCE.getAdventure().console().sendMessage(
                    TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostQueueCleared))
            );
        }
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
        return 1;
    }

    private int checkQueuesCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (CobblemonBoosters.INSTANCE.queuedShinyBoosts.isEmpty()) {
            if (player != null) {
                CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.noQueuedBoosts))
                );
            } else {
                CobblemonBoosters.INSTANCE.getAdventure().console().sendMessage(
                        TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.noQueuedBoosts))
                );
            }
        } else {
            for (ShinyBoost queuedBoost : CobblemonBoosters.INSTANCE.queuedShinyBoosts) {
                if (player != null) {
                    CobblemonBoosters.INSTANCE.getAdventure().player(player.getUUID()).sendMessage(
                            TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostInfo, queuedBoost))
                    );
                } else {
                    CobblemonBoosters.INSTANCE.getAdventure().console().sendMessage(
                            TextUtils.deserialize(TextUtils.parse(CobblemonBoosters.INSTANCE.config.messages.shinyBoostInfo, queuedBoost))
                    );
                }
            }
        }
        return 1;
    }
}