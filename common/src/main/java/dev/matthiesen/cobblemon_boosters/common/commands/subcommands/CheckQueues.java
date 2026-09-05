package dev.matthiesen.cobblemon_boosters.common.commands.subcommands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.commands.BoostersCommand;
import dev.matthiesen.cobblemon_boosters.common.commands.Util;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.services.BoostControllerServiceManager;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class CheckQueues implements ISubCommand {
    public static final CheckQueues CMD = new CheckQueues();

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
                            for (String entry : getRegisteredBoosterIds()) {
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
            ServiceManager.getGuiAdapter().openQueuesGUI(player);
        }
        return 1;
    }

    public int command(CommandContext<CommandSourceStack> ctx) {
        String booster = StringArgumentType.getString(ctx, "booster").toLowerCase();
        var handler = BoostersCommand.QUEUE_RESPONSE_HANDLERS.get(booster);
        if (handler != null) {
            handler.accept(ctx);
        } else {
            Util.sendMessage(ctx, "%prefix% &cUnknown booster type. Valid types are: " + String.join(", ", getRegisteredBoosterIds()) + ".");
        }
        return 1;
    }

    private List<String> getRegisteredBoosterIds() {
        return BoostControllerServiceManager.getGuiDefinitionIds();
    }
}
