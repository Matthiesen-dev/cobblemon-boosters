package dev.matthiesen.common.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.config.*;
import dev.matthiesen.common.cobblemon_boosters.gui.FallbackGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.gui.gooey.GooeyGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IGUIAdapter;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IWebhookService;
import dev.matthiesen.common.cobblemon_boosters.managers.BoostManager;
import dev.matthiesen.common.cobblemon_boosters.managers.TickManager;
import dev.matthiesen.common.cobblemon_boosters.registry.CommandRegistry;
import dev.matthiesen.common.cobblemon_boosters.registry.PermissionRegistry;
import dev.matthiesen.common.cobblemon_boosters.webhook.DiscordWebhookService;
import dev.matthiesen.common.cobblemon_boosters.webhook.NoOpWebhookService;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import net.minecraft.server.level.ServerPlayer;


public class CobblemonBoosters {
    public static CobblemonBoosters INSTANCE;
    public IGUIAdapter guiAdapter;
    public PermissionRegistry.Permissions permissions;
    public IWebhookService discordWebhookService;
    public boolean COBBREEDING_AVAILABLE;
    public BoostManager boostManager;

    // Configs
    public ConfigManager<CacheConfig> CACHE_CONFIG_MANAGER =
            new ConfigManager<>(CacheConfig.class, "cache", Constants.MOD_ID);
    public ConfigManager<MessagesConfig> MESSAGES_CONFIG_MANAGER =
            new ConfigManager<>(MessagesConfig.class, "messages", Constants.MOD_ID);
    public ConfigManager<PermissionsConfig> PERMISSIONS_CONFIG_MANAGER =
            new ConfigManager<>(PermissionsConfig.class, "permissions", Constants.MOD_ID);
    public ConfigManager<WebhooksConfig> WEBHOOKS_CONFIG_MANAGER =
            new ConfigManager<>(WebhooksConfig.class, "webhooks", Constants.MOD_ID);

    public CobblemonBoosters() {}

    public void initialize() {
        INSTANCE = this;
        this.reload(false);
        PermissionRegistry.init();
        this.permissions = PermissionRegistry.getPermissions();
        CommandRegistry.init();

        this.loadCompat();
        this.boostManager = new BoostManager();
        Constants.createInfoLog("Initialized");
    }

    public void loadCompat() {
        if (MatthiesenLibApi.isModLoaded(Constants.COMPAT.GOOEYLIBS)) {
            this.guiAdapter = new GooeyGUIAdapter();
            Constants.createInfoLog("GooeyLibs detected, using GooeyLibs for GUI");
        } else {
            this.guiAdapter = new FallbackGUIAdapter();
        }
        if (MatthiesenLibApi.isModLoaded(Constants.COMPAT.MATTHIESEN_LIB_WEBHOOKS)) {
            this.discordWebhookService = new DiscordWebhookService();
            Constants.createInfoLog("Matthiesen Lib Webhooks detected, using it for Discord Webhook integration");
        } else {
            this.discordWebhookService = new NoOpWebhookService();
        }

        this.COBBREEDING_AVAILABLE = MatthiesenLibApi.isModLoaded(Constants.COMPAT.COBBREEDING);
    }

    public void onStartup() {
        Constants.createInfoLog("Server starting, Setting up");
    }

    public void onServerStarted() {
        this.boostManager.setupSubscriptions();
    }

    public void onShutdown() {
        Constants.createInfoLog("Server stopping, shutting down");

        CacheConfig.setGlobalBoostData();
        this.CACHE_CONFIG_MANAGER.saveConfig();
        this.MESSAGES_CONFIG_MANAGER.saveConfig();
        this.PERMISSIONS_CONFIG_MANAGER.saveConfig();
        this.WEBHOOKS_CONFIG_MANAGER.saveConfig();

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

    public void onPlayerJoin(ServerPlayer player) {
        this.boostManager.appendPlayer(player);
    }

    public void onPlayerLeave(ServerPlayer player) {
        this.boostManager.clearPlayer(player);
    }

    public void reload(boolean fromCommand) {
        if (fromCommand) {
            CacheConfig.setGlobalBoostData();
            this.CACHE_CONFIG_MANAGER.saveConfig();
        }
        this.CACHE_CONFIG_MANAGER.loadConfig();
        this.MESSAGES_CONFIG_MANAGER.loadConfig();
        this.PERMISSIONS_CONFIG_MANAGER.loadConfig();
        this.WEBHOOKS_CONFIG_MANAGER.loadConfig();
        Constants.createInfoLog("Reloaded Cobblemon Boosters configs");
    }
}
