package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class Experience implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Util.newBasicMultiplierBoosterCommand(
                "experience",
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_PERMISSION,
                this::startCommand,
                maxMultiplier,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_START_PERMISSION,
                this::stopCommand,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_STOP_PERMISSION,
                this::statusCommand,
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_STATUS_PERMISSION
        );
    }

    public static final Float maxMultiplier = 100F;

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        ServerPlayer player = ctx.getSource().getPlayer();
        int totalSeconds = Util.parseTotalSeconds(duration, unit);

        if (CobblemonBoosters.INSTANCE.activeExperienceBoost == null) {
            CobblemonBoosters.INSTANCE.activeExperienceBoost = new ExperienceBoost(multiplier, totalSeconds);
            Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeExperienceBoost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    CobblemonBoosters.INSTANCE.config.discordWebhookConfig.experienceEventStartEmbed,
                    CobblemonBoosters.INSTANCE.activeExperienceBoost
            );
            CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeExperienceBoost.getBossBar());
        } else {
            ExperienceBoost boost = new ExperienceBoost(multiplier, totalSeconds);
            CobblemonBoosters.INSTANCE.queuedExperienceBoosts.add(boost);
            Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostAddedToQueued, boost);
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
                    CobblemonBoosters.INSTANCE.activeExperienceBoost,
                    CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostStopped
            );
        } catch (RuntimeException e) {
            Constants.LOGGER.error("Failed to stop experience boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        Util.handleStatusCommand(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.activeExperienceBoost,
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.config.messages.experienceBoostMessages.noActiveBoosts
        );
        return 1;
    }
}
