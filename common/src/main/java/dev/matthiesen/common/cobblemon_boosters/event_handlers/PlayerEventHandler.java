package dev.matthiesen.common.cobblemon_boosters.event_handlers;

import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiPlayerEventsManager;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerEventHandler implements MatthiesenLibApiPlayerEventsManager.IPlayerEventHandler {
    @Override
    public void onPlayerJoin(ServerPlayer player) {
        CobblemonBoosters.INSTANCE.boostManager.appendPlayer(player);
    }

    @Override
    public void onPlayerLeave(ServerPlayer player) {
        CobblemonBoosters.INSTANCE.boostManager.clearPlayer(player);
    }
}
