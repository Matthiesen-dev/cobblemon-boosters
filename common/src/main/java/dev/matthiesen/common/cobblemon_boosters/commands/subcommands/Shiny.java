package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.cobblemon.mod.common.Cobblemon;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.managers.MetricManager;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class Shiny implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        var permissions = PermissionRegistry.getPermissions();
        return Util.newBasicMultiplierBoosterCommand(
                "shiny",
                permissions.SHINY_PERMISSION,
                this::openGUI,
                this::startCommand,
                maxMultiplier(),
                permissions.SHINY_START_PERMISSION,
                this::stopCommand,
                permissions.SHINY_STOP_PERMISSION,
                this::statusCommand,
                permissions.SHINY_STATUS_PERMISSION
        );
    }

    public static Float maxMultiplier() {
        return Cobblemon.config.getShinyRate();
    }

    public int openGUI(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            CobblemonBoosters.INSTANCE.guiAdapter.openShinyBoosterGUI(player);
        }
        return 1;
    }

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        int totalSeconds = Helpers.parseTotalSeconds(duration, unit);

        BoostManager.IBoostManager<ShinyBoost> manager = BoostManager.getShinyBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages;

        ShinyBoost boost = new ShinyBoost(multiplier, totalSeconds);
        manager.appendToQueue(boost);
        Util.sendMessage(ctx, messages.boostAddedToQueued, boost);
        CacheConfig.setGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        try {
            var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages;
            Util.handleStopCommand(
                    ctx,
                    BoostManager.getShinyBoostManager().getActive(),
                    messages.boostStopped,
                    messages.noActiveBoosts
            );
        } catch (RuntimeException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.LOGGER.error("Failed to stop shiny boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages;
        Util.handleStatusCommand(
                ctx,
                BoostManager.getShinyBoostManager().getActive(),
                messages.boostInfo,
                messages.noActiveBoosts
        );
        return 1;
    }
}
