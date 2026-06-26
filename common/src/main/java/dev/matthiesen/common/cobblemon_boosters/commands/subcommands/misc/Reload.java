package dev.matthiesen.common.cobblemon_boosters.commands.subcommands.misc;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class Reload implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        var permissions = PermissionRegistry.getPermissions();
        return Commands.literal("reload")
                .requires(src -> PermissionRegistry.checkPermission(
                        src,
                        permissions.RELOAD_PERMISSION)
                )
                .executes(this::cmd);
    }

    public int cmd(CommandContext<CommandSourceStack> ctx) {
        CobblemonBoosters.INSTANCE.reload(true);
        Util.sendMessage(ctx, CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.commandReload);
        return 1;
    }
}