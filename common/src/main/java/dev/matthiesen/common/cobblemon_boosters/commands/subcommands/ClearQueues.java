package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ClearQueues implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Commands.literal("clear-queues")
                .requires(src -> PermissionRegistry.checkPermission(
                        src,
                        CobblemonBoosters.INSTANCE.permissions.CLEAR_QUEUES_PERMISSION
                ))
                .executes(this::command);
    }

    public int command(CommandContext<CommandSourceStack> ctx) {
        BoostManager bm = CobblemonBoosters.INSTANCE.boostManager;
        Util.handleQueueClear(
                ctx,
                bm.getShinyBoostManager().getQueue(),
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                bm.getCatchBoostManager().getQueue(),
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.catchBoostMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                bm.getExperienceBoostManager().getQueue(),
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.experienceBoostMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                bm.getSpawnBucketBoostManager().getQueue(),
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.spawnBucketBoostMessages.boostQueueCleared
        );
        CacheConfig.setGlobalBoostData();
        return 1;
    }
}
