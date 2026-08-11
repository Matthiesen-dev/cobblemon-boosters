package dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.commands.Util;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfig;
import dev.matthiesen.cobblemon_boosters.common.config.CacheServerConfig;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class ClearQueues implements ISubCommand {
    public static final ClearQueues CMD = new ClearQueues();

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
                BoostersConfig.getShinyMessages().boostQueueCleared()
        );
        Util.handleQueueClear(
                ctx,
                BoostManager.getCatchBoostManager().getQueue(),
                BoostersConfig.getCatchMessages().boostQueueCleared()
        );
        Util.handleQueueClear(
                ctx,
                BoostManager.getExperienceBoostManager().getQueue(),
                BoostersConfig.getExperienceMessages().boostQueueCleared()
        );
        Util.handleQueueClear(
                ctx,
                BoostManager.getSpawnBucketBoostManager().getQueue(),
                BoostersConfig.getSpawnBucketMessages().boostQueueCleared()
        );
        CacheServerConfig.setGlobalBoostData();
        return 1;
    }
}
