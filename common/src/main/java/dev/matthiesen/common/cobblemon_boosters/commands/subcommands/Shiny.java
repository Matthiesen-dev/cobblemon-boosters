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
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class Shiny implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Util.newBasicMultiplierBoosterCommand(
                "shiny",
                CobblemonBoosters.INSTANCE.permissions.SHINY_PERMISSION,
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

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        ServerPlayer player = ctx.getSource().getPlayer();
        int totalSeconds = Util.parseTotalSeconds(duration, unit);

        if (CobblemonBoosters.INSTANCE.activeShinyBoost == null) {
            CobblemonBoosters.INSTANCE.activeShinyBoost = new ShinyBoost(multiplier, totalSeconds);
            Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostStarted, CobblemonBoosters.INSTANCE.activeShinyBoost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    CobblemonBoosters.INSTANCE.config.discordWebhookConfig.shinyEventStartEmbed,
                    CobblemonBoosters.INSTANCE.activeShinyBoost
            );
            CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeShinyBoost.getBossBar());
        } else {
            ShinyBoost boost = new ShinyBoost(multiplier, totalSeconds);
            CobblemonBoosters.INSTANCE.queuedShinyBoosts.add(boost);
            Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostAddedToQueued, boost);
        }
        CobblemonBoosters.INSTANCE.config.saveGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        try {
            Util.handleStopCommand(
                    ctx,
                    player,
                    CobblemonBoosters.INSTANCE.activeShinyBoost,
                    CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostStopped
            );
        } catch (RuntimeException e) {
            Constants.LOGGER.error("Failed to stop shiny boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        Util.handleStatusCommand(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.activeShinyBoost,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.boostInfo,
                CobblemonBoosters.INSTANCE.config.messages.shinyMessages.noActiveBoosts
        );
        return 1;
    }
}
