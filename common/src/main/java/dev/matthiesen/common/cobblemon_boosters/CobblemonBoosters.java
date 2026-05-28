package dev.matthiesen.common.cobblemon_boosters;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.entity.SpawnBucketChosenEvent;
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent;
import com.cobblemon.mod.common.api.events.pokemon.ExperienceGainedEvent;
import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.api.spawning.SpawnBucket;
import com.mojang.brigadier.CommandDispatcher;
import dev.matthiesen.common.cobblemon_boosters.commands.CommandRegistry;
import dev.matthiesen.common.cobblemon_boosters.config.*;
import dev.matthiesen.common.cobblemon_boosters.data.*;
import dev.matthiesen.common.cobblemon_boosters.gui.FallbackGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.GooeyGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IWebhookService;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.*;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import kotlin.Unit;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedList;
import java.util.Queue;

public class CobblemonBoosters {
    public static CobblemonBoosters INSTANCE;
    public IGUIAdapter guiAdapter;
    public ModPermissions permissions;
    public IWebhookService discordWebhookService;

    // Configs
    public ConfigManager<CacheConfig> CACHE_CONFIG_MANAGER =
            new ConfigManager<>(CacheConfig.class, "cache", Constants.MOD_ID);
    public ConfigManager<MessagesConfig> MESSAGES_CONFIG_MANAGER =
            new ConfigManager<>(MessagesConfig.class, "messages", Constants.MOD_ID);
    public ConfigManager<PermissionsConfig> PERMISSIONS_CONFIG_MANAGER =
            new ConfigManager<>(PermissionsConfig.class, "permissions", Constants.MOD_ID);
    public ConfigManager<WebhooksConfig> WEBHOOKS_CONFIG_MANAGER =
            new ConfigManager<>(WebhooksConfig.class, "webhooks", Constants.MOD_ID);

    // Shiny Boost Variables
    public ShinyBoost activeShinyBoost = null;
    public Queue<ShinyBoost> queuedShinyBoosts = new LinkedList<>();
    private ObservableSubscription<ShinyChanceCalculationEvent> shinySubscription = null;

    // Catch Boost Variables
    public CatchBoost activeCatchBoost = null;
    public Queue<CatchBoost> queuedCatchBoosts = new LinkedList<>();
    private ObservableSubscription<PokemonCatchRateEvent> catchSubscription = null;

    // Experience Boost Variables
    public ExperienceBoost activeExperienceBoost = null;
    public Queue<ExperienceBoost> queuedExperienceBoosts = new LinkedList<>();
    private ObservableSubscription<ExperienceGainedEvent.Pre> experienceSubscription = null;

    // Bucket Boost Variables
    public SpawnBucketBoost activeSpawnBucketBoost = null;
    public Queue<SpawnBucketBoost> queuedSpawnBucketBoosts = new LinkedList<>();
    private ObservableSubscription<SpawnBucketChosenEvent> spawnBucketSubscription = null;

    public CobblemonBoosters() {}

    public void initialize() {
        INSTANCE = this;
        Constants.createInfoLog("Initialized");
        reload(false);
        this.permissions = new ModPermissions();
        if (MatthiesenLibApi.isModLoaded("gooeylibs")) {
            this.guiAdapter = new GooeyGUIAdapter();
        } else {
            this.guiAdapter = new FallbackGUIAdapter();
        }
        if (MatthiesenLibApi.isModLoaded("matthiesen_lib_webhooks")) {
            this.discordWebhookService = new DiscordWebhookService();
        } else {
            this.discordWebhookService = new NoOpWebhookService();
        }
    }

    public void onStartup() {
        Constants.createInfoLog("Server starting, Setting up");
    }

    public void onServerStarted() {
        this.shinySubscription = CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe(Priority.NORMAL, event -> {
            if (this.activeShinyBoost != null) {
                event.addModificationFunction(((rate, player, pokemon) -> Math.max(rate / this.activeShinyBoost.getMultiplier(), 1)));
            }
            return Unit.INSTANCE;
        });
        this.catchSubscription = CobblemonEvents.POKEMON_CATCH_RATE.subscribe(Priority.NORMAL, event -> {
            if (this.activeCatchBoost != null) {
                float baseCatchRate = event.getCatchRate();
                event.setCatchRate(Math.min(baseCatchRate * this.activeCatchBoost.getMultiplier(), 1F));
            }
            return Unit.INSTANCE;
        });
        this.experienceSubscription = CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe(Priority.NORMAL, event -> {
            if (this.activeExperienceBoost != null) {
                int exp = event.getExperience();
                event.setExperience(Math.round(exp * this.activeExperienceBoost.getMultiplier()));
            }
            return Unit.INSTANCE;
        });
        this.spawnBucketSubscription = CobblemonEvents.SPAWN_BUCKET_CHOSEN.subscribe(Priority.NORMAL, event -> {
            if (this.activeSpawnBucketBoost != null) {
                SpawnBucket newBucket = SpawnBucketOverrideSelector.recalculateOverrideBucket(event, this.activeSpawnBucketBoost);
                event.setBucket(newBucket);
            }
            return Unit.INSTANCE;
        });
    }

    public void onShutdown() {
        Constants.createInfoLog("Server stopping, shutting down");

        CacheConfig.setGlobalBoostData();
        CACHE_CONFIG_MANAGER.saveConfig();
        this.activeShinyBoost = null;
        this.activeCatchBoost = null;
        this.activeExperienceBoost = null;
        this.activeSpawnBucketBoost = null;
        this.queuedShinyBoosts.clear();
        this.queuedCatchBoosts.clear();
        this.queuedExperienceBoosts.clear();
        this.queuedSpawnBucketBoosts.clear();
        if (this.shinySubscription != null) {
            this.shinySubscription.unsubscribe();
        }
        if (this.catchSubscription != null) {
            this.catchSubscription.unsubscribe();
        }
        if (this.experienceSubscription != null) {
            this.experienceSubscription.unsubscribe();
        }
        if (this.spawnBucketSubscription != null) {
            this.spawnBucketSubscription.unsubscribe();
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

    public void onPlayerJoin(ServerPlayer player) {
        if (this.activeShinyBoost != null) {
            this.activeShinyBoost.getBossBar().addPlayer(player);
        }
        if (this.activeCatchBoost != null) {
            this.activeCatchBoost.getBossBar().addPlayer(player);
        }
        if (this.activeExperienceBoost != null) {
            this.activeExperienceBoost.getBossBar().addPlayer(player);
        }
        if (this.activeSpawnBucketBoost != null) {
            this.activeSpawnBucketBoost.getBossBar().addPlayer(player);
        }
    }

    public void onPlayerLeave(ServerPlayer player) {
        if (this.activeShinyBoost != null) {
            this.activeShinyBoost.getBossBar().removePlayer(player);
        }
        if (this.activeCatchBoost != null) {
            this.activeCatchBoost.getBossBar().removePlayer(player);
        }
        if (this.activeExperienceBoost != null) {
            this.activeExperienceBoost.getBossBar().removePlayer(player);
        }
        if (this.activeSpawnBucketBoost != null) {
            this.activeSpawnBucketBoost.getBossBar().removePlayer(player);
        }
    }

    public void reload(boolean fromCommand) {
        if (fromCommand) {
            CacheConfig.setGlobalBoostData();
            CACHE_CONFIG_MANAGER.saveConfig();
        }
        CACHE_CONFIG_MANAGER.loadConfig();
        MESSAGES_CONFIG_MANAGER.loadConfig();
        PERMISSIONS_CONFIG_MANAGER.loadConfig();
        WEBHOOKS_CONFIG_MANAGER.loadConfig();
        Constants.createInfoLog("Reloaded Cobblemon Boosters configs");
    }
}
