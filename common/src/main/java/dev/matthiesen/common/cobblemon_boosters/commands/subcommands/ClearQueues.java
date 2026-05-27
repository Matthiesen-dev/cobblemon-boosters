package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ClearQueues implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Commands.literal("clear-queues")
                .requires(src -> ModPermissions.checkPermission(
                        src,
                        CobblemonBoosters.INSTANCE.permissions.CLEAR_QUEUES_PERMISSION
                ))
                .executes(this::command);
    }

    public int command(CommandContext<CommandSourceStack> ctx) {
        Util.handleQueueClear(
                ctx,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                CobblemonBoosters.INSTANCE.queuedExperienceBoosts,
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts,
                CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostQueueCleared
        );
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
        return 1;
    }
}
