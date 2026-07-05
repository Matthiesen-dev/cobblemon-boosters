package dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.commands.Util;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class Reload implements ISubCommand {
    public static final Reload CMD = new Reload();

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
        CobblemonBoostersCommon.INSTANCE.reloadTask(true);
        Util.sendMessage(ctx, CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.commandReload);
        return 1;
    }
}