package dev.matthiesen.cobblemon_boosters.common.event_handlers;

import dev.matthiesen.cobblemon_boosters.common.Constants;
import dev.matthiesen.cobblemon_boosters.common.config.BoostersConfigManager;
import dev.matthiesen.cobblemon_boosters.common.config.CacheConfig;
import dev.matthiesen.cobblemon_boosters.common.managers.BoostManager;
import dev.matthiesen.cobblemon_boosters.common.managers.TickManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibServerEventHandler;
import net.minecraft.server.MinecraftServer;

public final class ServerEventHandler implements MatthiesenLibServerEventHandler {
    @Override
    public void onServerStart(MinecraftServer server) {
        Constants.createInfoLog("Server started, initializing Cobblemon Boosters");
        BoostManager.setupSubscriptions();
    }

    @Override
    public void onServerTick(MinecraftServer server) {
        TickManager.tick();
    }

    @Override
    public void onServerStop(MinecraftServer server) {
        Constants.createInfoLog("Server stopping, shutting down");
        CacheConfig.setGlobalBoostData();
        BoostersConfigManager.saveAll();
        BoostManager.teardownSubscriptions();
    }
}
