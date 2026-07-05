package dev.matthiesen.cobblemon_boosters.common;

import dev.matthiesen.cobblemon_boosters.common.commands.BoostersCommand;
import dev.matthiesen.cobblemon_boosters.common.config.*;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.services.managers.TickManager;
import dev.matthiesen.cobblemon_boosters.common.registry.PermissionRegistry;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.common.matthiesen_lib_api.abstracts.AbstractCommonMod;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibPlayerEventHandler;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibServerEventHandler;
import dev.matthiesen.libs.faststats.Token;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
        registerCommand(BoostersCommand.CMD);

        ServiceManager.init();
        registerPlayerEventHandler(PlayerEventsHandler.INSTANCE);
        registerServerEventHandler(ServerEventHandler.INSTANCE);
    }

    @Override
    public @Token @NotNull String getMetricsToken() {
        return METRICS_TOKEN;
    }

    @Override
    public Runnable reload() {
        return () -> reloadTask(false);
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

    public static class PlayerEventsHandler implements MatthiesenLibPlayerEventHandler {
        public static PlayerEventsHandler INSTANCE = new PlayerEventsHandler();

        @Override
        public void onPlayerJoin(ServerPlayer player) {
            BoostManager.appendPlayer(player);
        }

        @Override
        public void onPlayerLeave(ServerPlayer player) {
            BoostManager.clearPlayer(player);
        }
    }

    public static class ServerEventHandler implements MatthiesenLibServerEventHandler {
        public static ServerEventHandler INSTANCE = new ServerEventHandler();

        @Override
        public void onServerStart(MinecraftServer server) {
            CobblemonBoostersCommon.INSTANCE.createInfoLog("Server started, initializing Cobblemon Boosters");
            BoostManager.setupSubscriptions();
        }

        @Override
        public void onServerTick(MinecraftServer server) {
            TickManager.tick();
        }

        @Override
        public void onServerStop(MinecraftServer server) {
            CobblemonBoostersCommon.INSTANCE.createInfoLog("Server stopping, shutting down");
            CacheConfig.setGlobalBoostData();
            BoostersConfigManager.saveAll();
            BoostManager.teardownSubscriptions();
        }
    }
}
