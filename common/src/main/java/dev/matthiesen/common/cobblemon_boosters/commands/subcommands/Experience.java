package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class Experience implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Util.newBasicMultiplierBoosterCommand(
                "experience",
                CobblemonBoosters.INSTANCE.permissions.EXPERIENCE_PERMISSION,
                this::openGUI,
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

    public int openGUI(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            CobblemonBoosters.INSTANCE.guiAdapter.openExperienceBoosterGUI(player);
        }
        return 1;
    }

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        int totalSeconds = Helpers.parseTotalSeconds(duration, unit);

        BoostManager.IBoostManager<ExperienceBoost> manager = CobblemonBoosters.INSTANCE.boostManager.getExperienceBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages;
        var webhooks = CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;

        if (manager.getActive() == null) {
            ExperienceBoost boost = new ExperienceBoost(multiplier, totalSeconds);
            manager.setActive(boost);
            Util.sendMessage(ctx, messages.boostStarted, boost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    webhooks.experienceEventStartEmbed,
                    boost
            );
            boost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
        } else {
            ExperienceBoost boost = new ExperienceBoost(multiplier, totalSeconds);
            manager.appendToQueue(boost);
            Util.sendMessage(ctx, messages.boostAddedToQueued, boost);
        }
        CacheConfig.setGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        try {
            var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages;
            Util.handleStopCommand(
                    ctx,
                    CobblemonBoosters.INSTANCE.boostManager.getExperienceBoostManager().getActive(),
                    messages.boostStopped
            );
        } catch (RuntimeException e) {
            Constants.LOGGER.error("Failed to stop experience boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages;
        Util.handleStatusCommand(
                ctx,
                CobblemonBoosters.INSTANCE.boostManager.getExperienceBoostManager().getActive(),
                messages.boostInfo,
                messages.noActiveBoosts
        );
        return 1;
    }
}
