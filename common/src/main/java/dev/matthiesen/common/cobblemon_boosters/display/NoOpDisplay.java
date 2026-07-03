package dev.matthiesen.common.cobblemon_boosters.display;

import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/** Display service used when {@code displayMode = NONE}: shows nothing. */
public final class NoOpDisplay implements BoostDisplayService {
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
