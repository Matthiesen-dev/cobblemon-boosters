package dev.matthiesen.cobblemon_boosters.common.services;

import dev.matthiesen.cobblemon_boosters.common.CobblemonBoostersCommon;
import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.interfaces.BoostDisplayMode;
import dev.matthiesen.cobblemon_boosters.common.services.display.BossBarDisplay;
import dev.matthiesen.cobblemon_boosters.common.services.display.NoOpDisplay;
import dev.matthiesen.cobblemon_boosters.common.services.display.SidebarDisplay;
import dev.matthiesen.cobblemon_boosters.common.services.gui.FallbackGUIAdapter;
import dev.matthiesen.cobblemon_boosters.common.services.gui.gooey.GooeyGUIAdapter;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoostDisplayService;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IGUIAdapter;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IWebhookService;
import dev.matthiesen.cobblemon_boosters.common.services.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.services.webhook.DiscordWebhookService;
import dev.matthiesen.cobblemon_boosters.common.services.webhook.NoOpWebhookService;
import net.minecraft.server.MinecraftServer;

public final class ServiceManager {
    private static IGUIAdapter guiAdapter;
    private static IWebhookService discordWebhookService;
    private static IBoostDisplayService displayService;
    private static BoostDisplayMode displayMode;
    private static boolean cobbreedingAvailable;

    private static CobblemonBoostersCommon modInstance;

    public static volatile boolean isInitialized = false;

    public static void init(CobblemonBoostersCommon mod) {
        if (isInitialized) return;

        modInstance = mod;
        loadCompat();
        BoostManager.init();

        isInitialized = true;
    }

    public static IGUIAdapter getGuiAdapter() {
        return guiAdapter;
    }

    public static IBoostDisplayService getDisplayService() {
        return displayService;
    }

    public static IWebhookService getDiscordWebhookService() {
        return discordWebhookService;
    }

    public static boolean isCobbreedingAvailable() {
        return cobbreedingAvailable;
    }

    public static void loadCompat() {
        if (modInstance.isModLoaded(Constants.COMPAT.GOOEYLIBS)) {
            guiAdapter = new GooeyGUIAdapter();
        } else {
            guiAdapter = new FallbackGUIAdapter();
        }

        if (modInstance.isModLoaded(Constants.COMPAT.MATTHIESEN_LIB_WEBHOOKS)) {
            discordWebhookService = new DiscordWebhookService();
        } else {
            discordWebhookService = new NoOpWebhookService();
        }

        cobbreedingAvailable = modInstance.isModLoaded(Constants.COMPAT.COBBREEDING);
    }

    /**
     * Selects the display service from {@code CoreConfig.displayMode}. Called on every config
     * load, so {@code /boosters reload} switches the display live. When the mode actually
     * changes, the previous service is shut down (clearing its visuals) and any currently
     * active boosts are re-shown under the new one.
     */
    public static void applyDisplayMode() {
        BoostDisplayMode mode = BoostDisplayMode.fromString(modInstance.getCoreConfigManager().getConfig().displayMode);
        if (displayService != null && mode == displayMode) {
            return;
        }

        MinecraftServer server = modInstance.getMinecraftServer();
        if (displayService != null) {
            displayService.shutdown(server);
        }

        displayService = switch (mode) {
            case BOSSBAR -> new BossBarDisplay();
            case SIDEBAR -> new SidebarDisplay();
            case NONE -> new NoOpDisplay();
        };
        displayMode = mode;

        if (server != null) {
            for (IBoost boost : BoostManager.getActiveBoosts()) {
                displayService.onBoostActivated(boost);
            }
        }
    }
}
