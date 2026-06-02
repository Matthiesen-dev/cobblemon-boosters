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

public final class ClearQueues implements ISubCommand {
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
        BoostManager bm = CobblemonBoosters.INSTANCE.boostManager;
        Util.handleQueueClear(
                ctx,
                bm.getShinyBoostManager().getQueue(),
                CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                bm.getCatchBoostManager().getQueue(),
                CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.catchBoostMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                bm.getExperienceBoostManager().getQueue(),
                CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages.boostQueueCleared
        );
        Util.handleQueueClear(
                ctx,
                bm.getSpawnBucketBoostManager().getQueue(),
                CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages.boostQueueCleared
        );
        CacheConfig.setGlobalBoostData();
        return 1;
    }
}
