package dev.matthiesen.cobblemon_boosters.common.commands.subcommands.boosters;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.commands.Util;
import dev.matthiesen.cobblemon_boosters.common.config.CacheConfig;
import dev.matthiesen.cobblemon_boosters.common.boosts.ExperienceBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.utils.GuiCmdHelpers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class Experience implements ISubCommand {
    public static final Experience CMD = new Experience();

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        var permissions = PermissionRegistry.getPermissions();
        return Util.newBasicMultiplierBoosterCommand(
                "experience",
                permissions.EXPERIENCE_PERMISSION,
                this::openGUI,
                this::startCommand,
                maxMultiplier,
                permissions.EXPERIENCE_START_PERMISSION,
                this::stopCommand,
                permissions.EXPERIENCE_STOP_PERMISSION,
                this::statusCommand,
                permissions.EXPERIENCE_STATUS_PERMISSION
        );
    }

    public static final Float maxMultiplier = 100F;

    public int openGUI(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            ServiceManager.getGuiAdapter().openExperienceBoosterGUI(player);
        }
        return 1;
    }

    public int startCommand(CommandContext<CommandSourceStack> ctx) {
        float multiplier = FloatArgumentType.getFloat(ctx, "multiplier");
        int duration = IntegerArgumentType.getInteger(ctx, "duration");
        String unit = StringArgumentType.getString(ctx, "unit");
        int totalSeconds = GuiCmdHelpers.parseTotalSeconds(duration, unit);
        BoostManager.IBoostManager<ExperienceBoost> manager = BoostManager.getExperienceBoostManager();
        var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages;
        ExperienceBoost boost = new ExperienceBoost(multiplier, totalSeconds);
        manager.appendToQueue(boost);
        Util.sendMessage(ctx, messages.boostAddedToQueued, boost);
        CacheConfig.setGlobalBoostData();
        return 1;
    }

    public int stopCommand(CommandContext<CommandSourceStack> ctx) {
        try {
            var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages;
            Util.handleStopCommand(ctx, BoostManager.getExperienceBoostManager().getActive(), messages);
        } catch (RuntimeException e) {
            CobblemonBoostersCommon.INSTANCE.createErrorLog("Failed to stop experience boost", e);
        }
        return 1;
    }

    public int statusCommand(CommandContext<CommandSourceStack> ctx) {
        var messages = CobblemonBoostersCommon.INSTANCE.getMessagesConfigManager().getConfig().messages.experienceBoostMessages;
        Util.handleStatusCommand(ctx, BoostManager.getExperienceBoostManager().getActive(), messages);
        return 1;
    }
}
