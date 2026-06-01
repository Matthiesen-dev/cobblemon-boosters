package dev.matthiesen.common.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.config.*;
import dev.matthiesen.common.cobblemon_boosters.gui.FallbackGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.GooeyGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IWebhookService;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.event_handlers.PlayerEventHandler;
import dev.matthiesen.common.cobblemon_boosters.managers.TickManager;
import dev.matthiesen.common.cobblemon_boosters.registry.CommandRegistry;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import dev.matthiesen.common.cobblemon_boosters.webhook.DiscordWebhookService;
import dev.matthiesen.common.cobblemon_boosters.webhook.NoOpWebhookService;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;

public class CobblemonBoosters {
    public static CobblemonBoosters INSTANCE;
    public IGUIAdapter guiAdapter;
    public PermissionRegistry.Permissions permissions;
    public IWebhookService discordWebhookService;
    public boolean COBBREEDING_AVAILABLE;
    public BoostManager boostManager;

    public CobblemonBoosters() {
        BoostersConfigManager.registerConfigs();
    }

    public void initialize() {
        INSTANCE = this;
        this.reload(false);
        PermissionRegistry.init();
        this.permissions = PermissionRegistry.getPermissions();
        CommandRegistry.init();
        this.loadCompat();
        this.boostManager = new BoostManager();
        MatthiesenLibApi.registerReloadRunnable(Constants.MOD_ID, () -> reload(true));
        MatthiesenLibApi.registerPlayerEventHandler(Constants.MOD_ID, new PlayerEventHandler());
        Constants.createInfoLog("Initialized");
    }

    public void loadCompat() {
        if (MatthiesenLibApi.isModLoaded(Constants.COMPAT.GOOEYLIBS)) {
            this.guiAdapter = new GooeyGUIAdapter();
        } else {
            this.guiAdapter = new FallbackGUIAdapter();
        }

        if (MatthiesenLibApi.isModLoaded(Constants.COMPAT.MATTHIESEN_LIB_WEBHOOKS)) {
            this.discordWebhookService = new DiscordWebhookService();
        } else {
            this.discordWebhookService = new NoOpWebhookService();
        }

        this.COBBREEDING_AVAILABLE = MatthiesenLibApi.isModLoaded(Constants.COMPAT.COBBREEDING);
    }

    public void onServerStarted() {
        this.boostManager.setupSubscriptions();
    }

    public void onShutdown() {
        Constants.createInfoLog("Server stopping, shutting down");

        CacheConfig.setGlobalBoostData();
        BoostersConfigManager.saveAll();

        this.boostManager.teardownSubscriptions();
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
            CacheConfig.setGlobalBoostData();
            getCacheConfigManager().saveConfig();
        }
        BoostersConfigManager.loadAll();
        Constants.createInfoLog("Reloaded Cobblemon Boosters configs");
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
