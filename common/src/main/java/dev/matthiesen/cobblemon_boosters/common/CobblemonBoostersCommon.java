package dev.matthiesen.cobblemon_boosters.common;

import dev.matthiesen.cobblemon_boosters.common.commands.BoostersCommand;
import dev.matthiesen.cobblemon_boosters.common.config.*;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.services.managers.TickManager;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
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

    @Override
    public void initialize() {
        super.initialize();

        registerModConfig(MOD_ID, ModConfigType.STARTUP, BoostersConfig.PERMISSIONS_STARTUP_SPEC, "cobblemon_boosters/permissions.toml");
        registerModConfig(MOD_ID, ModConfigType.SERVER, BoostersConfig.CORE_SERVER_SPEC, "cobblemon_boosters/core.toml");
        registerModConfig(MOD_ID, ModConfigType.SERVER, BoostersConfig.CACHE_SERVER_SPEC, "cobblemon_boosters/cache.toml");
        registerModConfig(MOD_ID, ModConfigType.SERVER, BoostersConfig.WEBHOOKS_SERVER_SPEC, "cobblemon_boosters/webhooks.toml");

        PermissionRegistry.init();
        getCommandsRegistryManager().registerCommand(BoostersCommand.CMD);

        PlatformEvents.CONFIG_LOADING(MOD_ID).subscribe(BoostersConfig::onConfigLoad);
        PlatformEvents.SERVER_STARTED.subscribe(this::onServerStarted);
        PlatformEvents.SERVER_RELOAD.subscribe(this::onServerReload);
        PlatformEvents.SERVER_END_TICK.subscribe(TickManager::onEndTick);
        PlatformEvents.SERVER_STOPPING.subscribe(this::onServerStopping);
        PlatformEvents.PLAYER_JOIN.subscribe(BoostManager::onPlayerJoin);
        PlatformEvents.PLAYER_LEAVE.subscribe(BoostManager::onPlayerLeave);
    }

    public void onServerStarted(ServerEvent.Started event) {
        createInfoLog("Server starting, initializing Cobblemon Boosters");
        reloadTask();
        BoostManager.setupSubscriptions();
        ServiceManager.init();
    }

    public void onServerReload(ServerEvent.Reload event) {
        CacheServerConfig.setGlobalBoostData();
        CacheServerConfig.loadFromConfig();
        BoostManager.reapplyQueuePriorities();
        CacheServerConfig.setGlobalBoostData();
        reloadTask();
    }

    public void onServerStopping(ServerEvent.Stopping event) {
        createInfoLog("Server stopping, shutting down");
        CacheServerConfig.setGlobalBoostData();
        BoostManager.teardownSubscriptions();
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
