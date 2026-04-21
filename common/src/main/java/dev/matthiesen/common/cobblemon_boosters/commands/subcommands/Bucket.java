package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.commands.Util;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class Bucket implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Util.newBucketBoosterCommand(
                CobblemonBoosters.INSTANCE.permissions.BUCKET_PERMISSION,
                this::startCommand,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_START_PERMISSION,
                this::stopCommand,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_STOP_PERMISSION,
                this::statusCommand,
                CobblemonBoosters.INSTANCE.permissions.BUCKET_STATUS_PERMISSION
        );
    }

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        String bucket = StringArgumentType.getString(ctx, "bucket");
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        ServerPlayer player = ctx.getSource().getPlayer();
        int totalSeconds = Util.parseTotalSeconds(duration, unit);

        if (CobblemonBoosters.INSTANCE.activeSpawnBucketBoost == null) {
            CobblemonBoosters.INSTANCE.activeSpawnBucketBoost = new SpawnBucketBoost(multiplier, totalSeconds).setBucket(bucket);
            Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostStarted, CobblemonBoosters.INSTANCE.activeSpawnBucketBoost);
            CobblemonBoosters.INSTANCE.discordWebhookService.sendMessage(
                    CobblemonBoosters.INSTANCE.config.discordWebhookConfig.spawnBucketEventStartEmbed,
                    CobblemonBoosters.INSTANCE.activeSpawnBucketBoost
            );
            CobblemonBoosters.INSTANCE.getAdventure().all().showBossBar(CobblemonBoosters.INSTANCE.activeSpawnBucketBoost.getBossBar());
        } else {
            SpawnBucketBoost boost = new SpawnBucketBoost(multiplier, totalSeconds).setBucket(bucket);
            CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts.add(boost);
            Util.sendMessage(ctx, player, CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostAddedToQueued, boost);
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
                    CobblemonBoosters.INSTANCE.activeSpawnBucketBoost,
                    CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostStopped
            );
        } catch (RuntimeException e) {
            Constants.LOGGER.error("Failed to stop bucket boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        Util.handleStatusCommand(
                ctx,
                player,
                CobblemonBoosters.INSTANCE.activeSpawnBucketBoost,
                CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.boostInfo,
                CobblemonBoosters.INSTANCE.config.messages.spawnBucketBoostMessages.noActiveBoosts
        );
        return 1;
    }
}
