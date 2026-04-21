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

public class Reload implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Commands.literal("reload")
                .requires(src -> ModPermissions.checkPermission(
                        src,
                        CobblemonBoosters.INSTANCE.permissions.RELOAD_PERMISSION)
                )
                .executes(this::cmd);
    }

    public int cmd(CommandContext<CommandSourceStack> ctx) {
        CobblemonBoosters.INSTANCE.reload(true);
        ServerPlayer player = ctx.getSource().getPlayer();
        Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.commandReload);
        return 1;
    }
}
