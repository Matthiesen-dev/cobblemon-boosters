package dev.matthiesen.neoforge.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
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
    public void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        core.onStartup(server);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        core.onServerStarted();
    }

    @SubscribeEvent
    public void onCommandRegistration(RegisterCommandsEvent event) {
        core.registerCommands(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerStopping(ServerStoppingEvent event) {
        core.onShutdown();
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        core.onEndTick();
    }
}
