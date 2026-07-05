package dev.matthiesen.cobblemon_boosters.common.commands.subcommands.boosters;

import com.cobblemon.mod.common.Cobblemon;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.commands.Util;
import dev.matthiesen.cobblemon_boosters.common.config.CacheConfig;
import dev.matthiesen.cobblemon_boosters.common.boosts.ShinyBoost;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class Shiny implements ISubCommand {
    public static final Shiny CMD = new Shiny();

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
            ServiceManager.getGuiAdapter().openShinyBoosterGUI(player);
        }
        return 1;
    }

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        int totalSeconds = Helpers.parseTotalSeconds(duration, unit);
        BoostManager.IBoostManager<ShinyBoost> manager = BoostManager.getShinyBoostManager();
        var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages;
        ShinyBoost boost = new ShinyBoost(multiplier, totalSeconds);
        manager.appendToQueue(boost);
        Util.sendMessage(ctx, messages.boostAddedToQueued, boost);
        CacheConfig.setGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        try {
            var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages;
            Util.handleStopCommand(ctx, BoostManager.getShinyBoostManager().getActive(), messages);
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to stop shiny boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.shinyMessages;
        Util.handleStatusCommand(ctx, BoostManager.getShinyBoostManager().getActive(), messages);
        return 1;
    }
}
