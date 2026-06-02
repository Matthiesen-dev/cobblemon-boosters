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
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.screens.utils.Helpers;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.managers.MetricManager;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class Bucket implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Util.newBucketBoosterCommand(
                CobblemonBoosters.INSTANCE.permissions.BUCKET_PERMISSION,
                this::openGUI,
                this::startCommand,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_START_PERMISSION,
                this::stopCommand,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_STOP_PERMISSION,
                this::statusCommand,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_STATUS_PERMISSION
        );
    }

    public int openGUI(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            CobblemonBoosters.INSTANCE.guiAdapter.openBucketBoosterGUI(player);
        }
        return 1;
    }

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        String bucket = StringArgumentType.getString(ctx, "bucket");
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        int totalSeconds = Helpers.parseTotalSeconds(duration, unit);

        BoostManager.IBoostManager<SpawnBucketBoost> manager = CobblemonBoosters.INSTANCE.boostManager.getSpawnBucketBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages;
        var webhooks = CobblemonBoosters.INSTANCE.getWebhooksConfigManager().getConfig().discordWebhookConfig;

        if (manager.getActive() == null) {
            SpawnBucketBoost boost = new SpawnBucketBoost(multiplier, totalSeconds).setBucket(bucket);
            manager.setActive(boost);
            Util.sendMessage(ctx, messages.boostStarted, boost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    webhooks.spawnBucketEventStartEmbed,
                    boost
            );
            boost.getBossBar().showBossBarFromPlayerList(MatthiesenLibApi.getMinecraftServer().getPlayerList());
        } else {
            SpawnBucketBoost boost = new SpawnBucketBoost(multiplier, totalSeconds).setBucket(bucket);
            manager.appendToQueue(boost);
            Util.sendMessage(ctx, messages.boostAddedToQueued, boost);
        }
        CacheConfig.setGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        try {
            var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages;
            Util.handleStopCommand(
                    ctx,
                    CobblemonBoosters.INSTANCE.boostManager.getSpawnBucketBoostManager().getActive(),
                    messages.boostStopped
            );
        } catch (RuntimeException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.LOGGER.error("Failed to stop bucket boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages;
        Util.handleStatusCommand(
                ctx,
                CobblemonBoosters.INSTANCE.boostManager.getSpawnBucketBoostManager().getActive(),
                messages.boostInfo,
                messages.noActiveBoosts
        );
        return 1;
    }
}
