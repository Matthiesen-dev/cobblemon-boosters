package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class Catch implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Util.newBasicMultiplierBoosterCommand(
                "catch",
                CobblemonBoosters.INSTANCE.permissions.CATCH_PERMISSION,
                this::openGUI,
                this::startCommand,
                maxMultiplier,
                CobblemonBoosters.INSTANCE.permissions.CATCH_START_PERMISSION,
                this::stopCommand,
                CobblemonBoosters.INSTANCE.permissions.CATCH_STOP_PERMISSION,
                this::statusCommand,
                CobblemonBoosters.INSTANCE.permissions.CATCH_STATUS_PERMISSION
        );
    }

    public static final Float maxMultiplier = 100F;

    public int openGUI(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            CobblemonBoosters.INSTANCE.guiAdapter.openCatchBoosterGUI(player);
        }
        return 1;
    }

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        ServerPlayer player = ctx.getSource().getPlayer();
        int totalSeconds = Util.parseTotalSeconds(duration, unit);

        if (CobblemonBoosters.INSTANCE.activeCatchBoost == null) {
            CobblemonBoosters.INSTANCE.activeCatchBoost = new CatchBoost(multiplier, totalSeconds);
            Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeCatchBoost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    CobblemonBoosters.INSTANCE.config.discordWebhookConfig.catchEventStartEmbed,
                    CobblemonBoosters.INSTANCE.activeCatchBoost
            );
            CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeCatchBoost.getBossBar());
        } else {
            CatchBoost boost = new CatchBoost(multiplier, totalSeconds);
            CobblemonBoosters.INSTANCE.queuedCatchBoosts.add(boost);
            Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostAddedToQueued, boost);
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
                    CobblemonBoosters.INSTANCE.activeCatchBoost,
                    CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostStopped
            );
        } catch (RuntimeException e) {
            Constants.LOGGER.error("Failed to stop catch boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        Util.handleStatusCommand(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.config.messages.catchBoostMessages.noActiveBoosts
        );
        return 1;
    }
}
