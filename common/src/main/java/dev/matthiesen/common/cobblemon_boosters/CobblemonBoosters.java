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
import com.n1netails.n1netails.discord.exception.DiscordWebhookException;
import dev.matthiesen.common.cobblemon_boosters.commands.CommandRegistry;
import dev.matthiesen.common.cobblemon_boosters.config.ConfigManager;
import dev.matthiesen.common.cobblemon_boosters.config.ModConfig;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.permissions.ModPermissions;
import dev.matthiesen.common.cobblemon_boosters.utils.DiscordWebhookService;
import dev.matthiesen.common.cobblemon_boosters.utils.SpawnBucketOverrideSelector;
import dev.matthiesen.common.cobblemon_boosters.utils.TickManager;
import kotlin.Unit;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;

import java.util.LinkedList;
import java.util.Queue;

public class CobblemonBoosters {
    public static CobblemonBoosters INSTANCE;
    public ModPermissions permissions;
    public ModConfig config;
    private volatile MinecraftServerAudiences adventure;
    public DiscordWebhookService discordWebhookService = new DiscordWebhookService();

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

    public MinecraftServerAudiences getAdventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure without a running server!");
        }
        return this.adventure;
    }

    public void initialize() {
        INSTANCE = this;
        Constants.createInfoLog("Initialized");
        reload(false);
        this.permissions = new ModPermissions();
    }

    public void onStartup(MinecraftServer server) {
        Constants.createInfoLog("Server starting, Setting up");
        this.adventure = MinecraftServerAudiences.of(server);
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
        new ModConfig().saveGlobalBoostData();
        new ConfigManager().updateConfig(this.config);
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

    public void reload(boolean fromCommand) {
        if (fromCommand) {
            new ModConfig().saveGlobalBoostData();
            new ConfigManager().updateConfig(this.config);
        }
        this.config = new ConfigManager().loadConfig();
    }
}
