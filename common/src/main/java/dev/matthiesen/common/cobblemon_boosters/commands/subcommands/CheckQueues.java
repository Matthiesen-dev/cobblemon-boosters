package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class CheckQueues implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Commands.literal("check-queues")
                .requires(src -> ModPermissions.checkPermission(
                        src,
                        CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION
                ))
                .executes(this::openGUI)
                .then(Commands.argument("booster", StringArgumentType.string())
                        .requires(src -> ModPermissions.checkPermission(
                                src,
                                CobblemonBoosters.INSTANCE.permissions.CHECK_QUEUE_PERMISSION
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
        ServerPlayer player = ctx.getSource().getPlayer();
        String booster = StringArgumentType.getString(ctx, "booster").toLowerCase();
        switch (booster) {
            case "bucket" -> Util.handleQueueResponse(
                    ctx,
                    player,
                    CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts,
                    CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.noQueuedBoosts,
                    CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostInfo
            );
            case "catch" -> Util.handleQueueResponse(
                    ctx,
                    player,
                    CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                    CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noQueuedBoosts,
                    CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostInfo
            );
            case "experience" -> Util.handleQueueResponse(
                    ctx,
                    player,
                    CobblemonBoosters.INSTANCE.queuedExperienceBoosts,
                    CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.noQueuedBoosts,
                    CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostInfo
            );
            case "shiny" -> Util.handleQueueResponse(
                    ctx,
                    player,
                    CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                    CobblemonBoosters.INSTANCE.config.messages.shinyMessages.noQueuedBoosts,
                    CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostInfo
            );
            default -> Util.sendMessage(ctx, player, "%prefix% <red>Unknown booster type. Valid types are: " + String.join(", ", Constants.CURRENT_BOOSTERS) + ".");
        }
        return 1;
    }
}
