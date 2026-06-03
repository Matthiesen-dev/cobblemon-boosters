package dev.matthiesen.common.cobblemon_boosters.event_handlers;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.config.BoostersConfigManager;
import dev.matthiesen.common.cobblemon_boosters.config.CacheConfig;
import dev.matthiesen.common.cobblemon_boosters.managers.TickManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibServerEventHandler;
import net.minecraft.server.MinecraftServer;

public final class ServerEventHandler implements MatthiesenLibServerEventHandler {
    @Override
    public void onServerStart(MinecraftServer server) {
        Constants.createInfoLog("Server started, initializing Cobblemon Boosters");
        CobblemonBoosters.INSTANCE.boostManager.setupSubscriptions();
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
        CobblemonBoosters.INSTANCE.boostManager.teardownSubscriptions();
    }
}
