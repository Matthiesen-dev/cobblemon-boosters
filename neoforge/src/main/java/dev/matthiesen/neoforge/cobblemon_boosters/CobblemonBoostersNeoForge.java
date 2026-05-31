package dev.matthiesen.neoforge.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(Constants.MOD_ID)
public class CobblemonBoostersNeoForge {
    CobblemonBoosters core = new CobblemonBoosters();

    public CobblemonBoostersNeoForge() {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        core.initialize();
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        core.onServerStarted();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerStopping(ServerStoppingEvent event) {
        core.onShutdown();
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        core.onEndTick();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            if (event.getEntity().level().isClientSide) return;
            ServerPlayer player = event.getEntity() instanceof ServerPlayer ? (ServerPlayer) event.getEntity() : null;
            if (player == null) return;
            core.onPlayerJoin(player);
        } catch (RuntimeException e) {
            Constants.LOGGER.error("Error handling player join event for player {}", event.getEntity().getName().getString(), e);
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        try {
            if (event.getEntity().level().isClientSide) return;
            ServerPlayer player = event.getEntity() instanceof ServerPlayer ? (ServerPlayer) event.getEntity() : null;
            if (player == null) return;
            core.onPlayerLeave(player);
        } catch (RuntimeException e) {
            Constants.LOGGER.error("Error handling player leave event for player {}", event.getEntity().getName().getString(), e);
        }
    }
}
