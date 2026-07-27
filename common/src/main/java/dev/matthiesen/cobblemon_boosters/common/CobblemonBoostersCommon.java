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
import dev.matthiesen.matthiesen_core.common.utility.config.ConfigManager;
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
        BoostersConfigManager.registerConfigs();

        reloadTask(false);
        PermissionRegistry.init();
        getCommandsRegistryManager().registerCommand(BoostersCommand.CMD);

        ServiceManager.init();

        PlatformEvents.SERVER_RELOAD.subscribe(event -> {
            reloadTask(true);
            createInfoLog("Reloaded Cobblemon Boosters configs via /reload");
        });

        PlatformEvents.SERVER_STARTING.subscribe(event -> {
            createInfoLog("Server starting, initializing Cobblemon Boosters");
            BoostManager.setupSubscriptions();
        });

        PlatformEvents.SERVER_END_TICK.subscribe(event -> TickManager.tick());

        PlatformEvents.SERVER_STOPPING.subscribe(event -> {
            createInfoLog("Server stopping, shutting down");
            CacheConfig.setGlobalBoostData();
            BoostersConfigManager.saveAll();
            BoostManager.teardownSubscriptions();
        });

        PlatformEvents.PLAYER_JOIN.subscribe(event -> BoostManager.appendPlayer(event.player()));

        PlatformEvents.PLAYER_LEAVE.subscribe(event -> BoostManager.clearPlayer(event.player()));
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public void reloadTask(boolean fromCommand) {
        if (fromCommand) {
            CacheConfig.setGlobalBoostData();
            getCacheConfigManager().saveConfig();
        }
        BoostersConfigManager.loadAll();
        if (ServiceManager.isInitialized) {
            ServiceManager.applyDisplayMode();
        }
        BoostManager.reapplyQueuePriorities();
        CacheConfig.setGlobalBoostData();
        createInfoLog("Reloaded Cobblemon Boosters configs");
    }

    public ConfigManager<CoreConfig> getCoreConfigManager() {
        return BoostersConfigManager.getCoreConfigManager();
    }

    public ConfigManager<CacheConfig> getCacheConfigManager() {
        return BoostersConfigManager.getCacheConfigManager();
    }

    public ConfigManager<MessagesConfig> getMessagesConfigManager() {
        return BoostersConfigManager.getMessagesConfigManager();
    }

    public ConfigManager<PermissionsConfig> getPermissionsConfigManager() {
        return BoostersConfigManager.getPermissionsConfigManager();
    }

    public ConfigManager<WebhooksConfig> getWebhooksConfigManager() {
        return BoostersConfigManager.getWebhooksConfigManager();
    }
}
