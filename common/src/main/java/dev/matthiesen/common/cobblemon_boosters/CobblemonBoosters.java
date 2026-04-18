package dev.matthiesen.common.cobblemon_boosters;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.mojang.brigadier.CommandDispatcher;
import dev.matthiesen.common.cobblemon_boosters.commands.CommandRegistry;
import dev.matthiesen.common.cobblemon_boosters.config.ConfigManager;
import dev.matthiesen.common.cobblemon_boosters.config.ModConfig;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.TickManager;
import kotlin.Unit;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedList;
import java.util.Queue;

public class CobblemonBoosters {
    public ModPermissions permissions;
    public ModConfig config;
    public MinecraftServer currentServer;
    private volatile MinecraftServerAudiences adventure;
    public ShinyBoost globalBoost = null;
    public Queue<ShinyBoost> queuedShinyBoosts = new LinkedList<>();
    private ObservableSubscription<ShinyChanceCalculationEvent> shinySubscription = null;

    public static CobblemonBoosters INSTANCE;

    public CobblemonBoosters() {}

    public MinecraftServerAudiences getAdventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure without a running server!");
        }
        return this.adventure;
    }

    public void initialize() {
        Constants.createInfoLog("Initialized");
        reload(false);
        this.permissions = new ModPermissions();
        INSTANCE = this;
    }

    public void onStartup(MinecraftServer server) {
        Constants.createInfoLog("Server starting, Setting up");
        this.currentServer = server;
        this.adventure = MinecraftServerAudiences.of(server);
    }

    public void onServerStarted() {
        this.shinySubscription = CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(Priority.NORMAL, event -> {
            if (this.globalBoost != null) {
                event.addModificationFunction(((rate, player, pokemon) -> Math.max(rate / this.globalBoost.multiplier, 1)));
            }
            return Unit.INSTANCE;
        });
    }

    public void onShutdown() {
        Constants.createInfoLog("Server stopping, shutting down");
        new ModConfig().saveGlobalBoostData();
        new ConfigManager().updateConfig(this.config);
        this.globalBoost = null;
        this.queuedShinyBoosts.clear();
        if (this.shinySubscription != null) {
            this.shinySubscription.unsubscribe();
        }
    }

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        Constants.createInfoLog("Registering Commands");
        CommandRegistry.init(dispatcher, registry, context);
    }

    public void onEndTick() {
        try {
            TickManager.tickBoosts();
            TickManager.updateBossBars();
        } catch (IllegalArgumentException e) {
            Constants.LOGGER.error("Caught BossBar exception! ", e);
        }
    }

    public void reload(boolean fromCommand) {
        if (fromCommand) {
            new ModConfig().saveGlobalBoostData();
            new ConfigManager().updateConfig(this.config);
        }
        this.config = new ConfigManager().loadConfig();
    }
}
