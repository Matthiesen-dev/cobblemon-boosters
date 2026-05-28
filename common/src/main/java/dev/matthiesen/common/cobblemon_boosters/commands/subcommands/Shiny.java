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
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class Shiny implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Util.newBasicMultiplierBoosterCommand(
                "shiny",
                CobblemonBoosters.INSTANCE.permissions.SHINY_PERMISSION,
                this::openGUI,
                this::startCommand,
                maxMultiplier(),
                CobblemonBoosters.INSTANCE.permissions.SHINY_START_PERMISSION,
                this::stopCommand,
                CobblemonBoosters.INSTANCE.permissions.SHINY_STOP_PERMISSION,
                this::statusCommand,
                CobblemonBoosters.INSTANCE.permissions.SHINY_STATUS_PERMISSION
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
        int totalSeconds = Util.parseTotalSeconds(duration, unit);

        if (CobblemonBoosters.INSTANCE.activeShinyBoost == null) {
            CobblemonBoosters.INSTANCE.activeShinyBoost = new ShinyBoost(multiplier, totalSeconds);
            Util.sendMessage(ctx, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostStarted, CobblemonBoosters.INSTANCE.activeShinyBoost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    CobblemonBoosters.INSTANCE.WEBHOOKS_CONFIG_MANAGER.getConfig().discordWebhookConfig.shinyEventStartEmbed,
                    CobblemonBoosters.INSTANCE.activeShinyBoost
            );
            CobblemonBoosters.INSTANCE.activeShinyBoost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
        } else {
            ShinyBoost boost = new ShinyBoost(multiplier, totalSeconds);
            CobblemonBoosters.INSTANCE.queuedShinyBoosts.add(boost);
            Util.sendMessage(ctx, CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostAddedToQueued, boost);
        }
        CacheConfig.setGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        try {
            Util.handleStopCommand(
                    ctx,
                    CobblemonBoosters.INSTANCE.activeShinyBoost,
                    CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostStopped
            );
        } catch (RuntimeException e) {
            Constants.LOGGER.error("Failed to stop shiny boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        Util.handleStatusCommand(
                ctx,
                CobblemonBoosters.INSTANCE.activeShinyBoost,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.boostInfo,
                CobblemonBoosters.INSTANCE.MESSAGES_CONFIG_MANAGER.getConfig().messages.shinyMessages.noActiveBoosts
        );
        return 1;
    }
}
