package dev.matthiesen.common.cobblemon_boosters.commands.subcommands.boosters;

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
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class Bucket implements ISubCommand {
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        var permissions = PermissionRegistry.getPermissions();
        return Util.newBucketBoosterCommand(
                permissions.BUCKET_PERMISSION,
                this::openGUI,
                this::startCommand,
                permissions.BUCKET_START_PERMISSION,
                this::stopCommand,
                permissions.BUCKET_STOP_PERMISSION,
                this::statusCommand,
                permissions.BUCKET_STATUS_PERMISSION
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
        BoostManager.IBoostManager<SpawnBucketBoost> manager = BoostManager.getSpawnBucketBoostManager();
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages;
        SpawnBucketBoost boost = new SpawnBucketBoost(multiplier, totalSeconds).setBucket(bucket);
        manager.appendToQueue(boost);
        Util.sendMessage(ctx, messages.boostAddedToQueued, boost);
        CacheConfig.setGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        try {
            var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages;
            Util.handleStopCommand(ctx, BoostManager.getSpawnBucketBoostManager().getActive(), messages);
        } catch (RuntimeException e) {
            MetricManager.ERROR_TRACKER.trackError(e);
            Constants.LOGGER.error("Failed to stop bucket boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        var messages = CobblemonBoosters.INSTANCE.getMessagesConfigManager().getConfig().messages.spawnBucketBoostMessages;
        Util.handleStatusCommand(ctx, BoostManager.getSpawnBucketBoostManager().getActive(), messages);
        return 1;
    }
}
