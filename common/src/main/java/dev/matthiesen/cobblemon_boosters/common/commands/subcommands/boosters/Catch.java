package dev.matthiesen.cobblemon_boosters.common.commands.subcommands.boosters;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.commands.Util;
import dev.matthiesen.cobblemon_boosters.common.config.CacheConfig;
import dev.matthiesen.cobblemon_boosters.common.boosts.CatchBoost;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class Catch implements ISubCommand {
    public static final Catch CMD = new Catch();

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        var permissions = PermissionRegistry.getPermissions();
        return Util.newBasicMultiplierBoosterCommand(
                "catch",
                permissions.CATCH_PERMISSION,
                this::openGUI,
                this::startCommand,
                maxMultiplier,
                permissions.CATCH_START_PERMISSION,
                this::stopCommand,
                permissions.CATCH_STOP_PERMISSION,
                this::statusCommand,
                permissions.CATCH_STATUS_PERMISSION
        );
    }

    public static final Float maxMultiplier = 100F;

    public int openGUI(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            ServiceManager.getGuiAdapter().openCatchBoosterGUI(player);
        }
        return 1;
    }

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        int totalSeconds = Helpers.parseTotalSeconds(duration, unit);
        BoostManager.IBoostManager<CatchBoost> manager = BoostManager.getCatchBoostManager();
        var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.catchBoostMessages;
        CatchBoost boost = new CatchBoost(multiplier, totalSeconds);
        manager.appendToQueue(boost);
        Util.sendMessage(ctx, messages.boostAddedToQueued, boost);
        CacheConfig.setGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        try {
            var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.catchBoostMessages;
            Util.handleStopCommand(ctx, BoostManager.getCatchBoostManager().getActive(), messages);
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to stop catch boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.catchBoostMessages;
        Util.handleStatusCommand(ctx, BoostManager.getCatchBoostManager().getActive(), messages);
        return 1;
    }
}
