package dev.matthiesen.cobblemon_boosters.common;

import dev.matthiesen.cobblemon_boosters.common.commands.BoostersCommand;
import dev.matthiesen.cobblemon_boosters.common.config.*;
import dev.matthiesen.cobblemon_boosters.common.services.BoostController;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.services.controllers.CatchBoostController;
import dev.matthiesen.cobblemon_boosters.common.services.controllers.ExperienceBoostController;
import dev.matthiesen.cobblemon_boosters.common.services.controllers.ShinyBoostController;
import dev.matthiesen.cobblemon_boosters.common.services.controllers.SpawnBucketBoostController;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import dev.matthiesen.matthiesen_core.common.api.events.server.ServerEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import org.jetbrains.annotations.NotNull;

public final class CobblemonBoostersCommon extends AbstractCommonMod {
    public static final String MOD_ID = "cobblemon_boosters";
    public static final String MOD_NAME = "Cobblemon Boosters";
    private static @Token final String METRICS_TOKEN = "7f05d25cd8de7eb6d5b01f47dffee788";

    public static final CobblemonBoostersCommon INSTANCE = new CobblemonBoostersCommon();

    public CobblemonBoostersCommon() {
        super(MOD_ID, MOD_NAME);
    }

    public static String modConfig(String key) {
        return MOD_ID + "/" + key + ".toml";
    }

    @Override
    public void initialize() {
        super.initialize();

        createInfoLog("Registering Cobblemon Boosters Mod Configs");

        registerModConfig(MOD_ID, ModConfigType.STARTUP, BoostersConfig.PERMISSIONS_STARTUP_SPEC, modConfig("permissions"));
        registerModConfig(MOD_ID, ModConfigType.SERVER, BoostersConfig.CORE_SERVER_SPEC, modConfig("core"));
        registerModConfig(MOD_ID, ModConfigType.SERVER, BoostersConfig.CACHE_SERVER_SPEC, modConfig("cache"));
        registerModConfig(MOD_ID, ModConfigType.SERVER, BoostersConfig.WEBHOOKS_SERVER_SPEC, modConfig("webhooks"));

        createInfoLog("Registering Cobblemon Boosters Permissions");

        PermissionRegistry.init();

        createInfoLog("Registering Cobblemon Boosters Boost Controllers");

        CatchBoostController.register();
        ShinyBoostController.register();
        ExperienceBoostController.register();
        SpawnBucketBoostController.register();

        createInfoLog("Registering Cobblemon Boosters Commands");

        getCommandsRegistryManager().registerCommand(BoostersCommand.CMD);

        createInfoLog("Registering Cobblemon Boosters Services");

        PlatformEvents.CONFIG_LOADING(MOD_ID).subscribe(BoostersConfig::onConfigLoad);
        PlatformEvents.SERVER_STARTED.subscribe(this::onServerStarted);
        PlatformEvents.SERVER_RELOAD.subscribe(this::onServerReload);
        PlatformEvents.SERVER_END_TICK.subscribe(this::onServerEndTick);
        PlatformEvents.SERVER_STOPPING.subscribe(this::onServerStopping);
        PlatformEvents.PLAYER_JOIN.subscribe(this::onPlayerJoin);
        PlatformEvents.PLAYER_LEAVE.subscribe(this::onPlayerLeave);

        createInfoLog("Finished initializing Cobblemon Boosters, waiting for server...");
    }

    private boolean isServerRunning = false;

    public void onServerStarted(ServerEvent.Started event) {
        createInfoLog("Server starting, initializing Cobblemon Boosters");
        isServerRunning = true;
        reloadTask();
        BoostController.setupSubscribers();
        ServiceManager.init();
    }

    public void onServerReload(ServerEvent.Reload event) {
        if (!isServerRunning) return;
        CacheServerConfig.setGlobalBoostData();
        CacheServerConfig.loadFromConfig();
        BoostController.refreshQueuePriorities();
        CacheServerConfig.setGlobalBoostData();
        reloadTask();
    }

    public void onServerStopping(ServerEvent.Stopping event) {
        createInfoLog("Server stopping, shutting down");
        if (!isServerRunning) return;
        CacheServerConfig.setGlobalBoostData();
        BoostController.teardownSubscribers();
    }

    public static int tickCounter = 0;

    public void onServerEndTick(ServerEvent.EndTick event) {
        try {
            BoostController.tickBoosts();
            ServiceManager.getDisplayService().tick(event.server());
            tickCounter++;
            var saveInterval = BoostersConfig.CORE_SERVER_CONFIG.saveIntervalTicks.getAsInt();
            if (tickCounter >= saveInterval) {
                tickCounter = 0;
                if (BoostersConfig.CORE_SERVER_CONFIG.verboseCacheLogging.get()) {
                    createInfoLog("Saving Boosters to Cache...");
                }
                BoostersConfig.saveCacheToConfig();
            }
        } catch (IllegalArgumentException e) {
            createErrorLog("Caught BossBar exception! ", e);
        }
    }

    public void onPlayerJoin(PlayerEvent.Join event) {
        try {
            ServiceManager.getDisplayService().onPlayerJoin(event.player());
        } catch (RuntimeException e) {
            createErrorLog("Error appending player to boosts in BoostManager", e);
        }
    }

    public void onPlayerLeave(PlayerEvent.Leave event) {
        try {
            ServiceManager.getDisplayService().onPlayerLeave(event.player());
        } catch (RuntimeException e) {
            createErrorLog("Error clearing player from boosts in BoostManager", e);
        }
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public void reloadTask() {
        if (ServiceManager.isInitialized) {
            ServiceManager.applyDisplayMode();
        }
        createInfoLog("Reloaded Cobblemon Boosters Services");
    }
}
