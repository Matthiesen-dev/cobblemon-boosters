package dev.matthiesen.fabric.cobblemon_boosters;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class CobblemonBoostersFabric implements ModInitializer {
    CobblemonBoosters core = new CobblemonBoosters();

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        core.initialize();
        CommandRegistrationCallback.EVENT.register(core::registerCommands);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            MinecraftServer runningServer = server.createCommandSourceStack().getServer();
            core.onStartup(runningServer);
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> core.onServerStarted());
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> core.onShutdown());
        ServerTickEvents.END_SERVER_TICK.register(server -> core.onEndTick());
    }
}
