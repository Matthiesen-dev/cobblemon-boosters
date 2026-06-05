package dev.matthiesen.common.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.config.*;
import dev.matthiesen.common.cobblemon_boosters.event_handlers.ServerEventHandler;
import dev.matthiesen.common.cobblemon_boosters.gui.FallbackGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.GooeyGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IWebhookService;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.event_handlers.PlayerEventHandler;
import dev.matthiesen.common.cobblemon_boosters.managers.MetricManager;
import dev.matthiesen.common.cobblemon_boosters.registry.CommandRegistry;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import dev.matthiesen.common.cobblemon_boosters.webhook.DiscordWebhookService;
import dev.matthiesen.common.cobblemon_boosters.webhook.NoOpWebhookService;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;

public final class CobblemonBoosters {
    public static CobblemonBoosters INSTANCE;
    public IGUIAdapter guiAdapter;
    public IWebhookService discordWebhookService;
    public boolean COBBREEDING_AVAILABLE;
    public BoostManager boostManager;

    public CobblemonBoosters() {
        BoostersConfigManager.registerConfigs();
    }

    public void initialize() {
        INSTANCE = this;
        MetricManager.init();
        this.reload(false);
        PermissionRegistry.init();
        CommandRegistry.init();
        this.loadCompat();
        this.boostManager = new BoostManager();
        this.registerHandlers();
        Constants.createInfoLog("Initialized");
    }

    public void registerHandlers() {
        MatthiesenLibApi.registerReloadRunnable(Constants.MOD_ID, () -> reload(true));
        MatthiesenLibApi.registerPlayerEventHandler(Constants.MOD_ID, new PlayerEventHandler());
        MatthiesenLibApi.registerServerEventHandler(Constants.MOD_ID, new ServerEventHandler());
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
