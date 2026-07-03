package dev.matthiesen.common.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.config.*;
import dev.matthiesen.common.cobblemon_boosters.display.BoostDisplayMode;
import dev.matthiesen.common.cobblemon_boosters.display.BoostDisplayService;
import dev.matthiesen.common.cobblemon_boosters.display.BossBarDisplay;
import dev.matthiesen.common.cobblemon_boosters.display.NoOpDisplay;
import dev.matthiesen.common.cobblemon_boosters.display.SidebarDisplay;
import dev.matthiesen.common.cobblemon_boosters.event_handlers.ServerEventHandler;
import dev.matthiesen.common.cobblemon_boosters.gui.FallbackGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.GooeyGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IWebhookService;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import net.minecraft.server.MinecraftServer;
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
    public BoostDisplayService displayService;
    private BoostDisplayMode displayMode;
    public boolean COBBREEDING_AVAILABLE;

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
        BoostManager.init();
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
        applyDisplayMode();
        BoostManager.reapplyQueuePriorities();
        CacheConfig.setGlobalBoostData();
        Constants.createInfoLog("Reloaded Cobblemon Boosters configs");
    }

    /**
     * Selects the display service from {@code CoreConfig.displayMode}. Called on every config
     * load, so {@code /boosters reload} switches the display live. When the mode actually
     * changes, the previous service is shut down (clearing its visuals) and any currently
     * active boosts are re-shown under the new one.
     */
    public void applyDisplayMode() {
        BoostDisplayMode mode = BoostDisplayMode.fromString(getCoreConfigManager().getConfig().displayMode);
        if (this.displayService != null && mode == this.displayMode) {
            return;
        }

        MinecraftServer server = MatthiesenLibApi.getMinecraftServer();
        if (this.displayService != null) {
            this.displayService.shutdown(server);
        }

        this.displayService = switch (mode) {
            case BOSSBAR -> new BossBarDisplay();
            case SIDEBAR -> new SidebarDisplay();
            case NONE -> new NoOpDisplay();
        };
        this.displayMode = mode;

        if (server != null) {
            for (IBoost boost : BoostManager.getActiveBoosts()) {
                this.displayService.onBoostActivated(boost);
            }
        }
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
