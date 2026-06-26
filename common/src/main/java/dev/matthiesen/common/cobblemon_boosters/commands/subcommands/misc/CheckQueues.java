package dev.matthiesen.common.cobblemon_boosters.commands.subcommands.misc;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class CheckQueues implements ISubCommand {
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
