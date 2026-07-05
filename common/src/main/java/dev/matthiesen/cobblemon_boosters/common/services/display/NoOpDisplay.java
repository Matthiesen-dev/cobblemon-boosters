package dev.matthiesen.cobblemon_boosters.common.services.display;

import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoost;
import dev.matthiesen.cobblemon_boosters.common.interfaces.IBoostDisplayService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/** Display service used when {@code displayMode = NONE}: shows nothing. */
public final class NoOpDisplay implements IBoostDisplayService {
    @Override
    public void onPlayerJoin(ServerPlayer player) {}

    @Override
    public void onPlayerLeave(ServerPlayer player) {}

    @Override
    public void onBoostActivated(IBoost boost) {}

    @Override
    public void onBoostDeactivated(IBoost boost) {}

    @Override
    public void tick(MinecraftServer server) {}

    @Override
    public void shutdown(MinecraftServer server) {}
}
