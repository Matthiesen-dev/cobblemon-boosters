package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
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
                .executes(this::command);
    }

    public int command(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        Util.handleQueueResponse(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.noQueuedBoosts,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostInfo
        );
        Util.handleQueueResponse(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noQueuedBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostInfo
        );
        Util.handleQueueResponse(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.queuedExperienceBoosts,
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.noQueuedBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostInfo
        );
        Util.handleQueueResponse(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts,
                CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.noQueuedBoosts,
                CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostInfo
        );
        return 1;
    }
}
